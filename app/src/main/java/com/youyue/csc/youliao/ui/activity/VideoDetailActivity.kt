package com.youyue.csc.youliao.ui.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.shuyu.gsyvideoplayer.GSYVideoPlayer
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.adapter.CommentAdapter
import com.youyue.csc.youliao.base.BaseActivity
import com.youyue.csc.youliao.bean.VideoBean
import com.youyue.csc.youliao.ui.dialog.CommentDialog
import com.youyue.csc.youliao.utils.SPUtils
import com.youyue.csc.youliao.utils.SaveObjectInFileUtils
import com.youyue.csc.youliao.utils.VideoListener
import com.youyue.csc.youliao.utils.showToast
import kotlinx.android.synthetic.main.activity_video_detail.*
import zlc.season.rxdownload2.RxDownload
import java.io.FileInputStream

class VideoDetailActivity : BaseActivity() {
    //lateinit:延迟加载,不加latrinit修饰的话，可以写成: var bean : VideoBean?=null
    // (上一个界面传过来的数据)
    lateinit var bean: VideoBean

    //最终转换的视频时长
    lateinit var realMinute: String
    lateinit var realSecond: String

    //屏幕旋转工具，由GSYPlayer视频播放框架提供
    lateinit var orientationUtils: OrientationUtils

    var isPlayer: Boolean = false
    var isPause: Boolean = false

    //视频的封面
    lateinit var imagView: ImageView

    //Hnalder中的what标识(用来通知更新Video的封面图片)
    companion object {
        var MSG_UPDATE_PHOTO = 10001
    }

    //更新视频封面
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            when (msg?.what) {
                MSG_UPDATE_PHOTO -> {
                    //设置视频的封面
                    gsy_player.setThumbImageView(imagView)
                }

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置全屏显示
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_video_detail)

        //获取Intent传过来的值
        bean = intent.getParcelableExtra("data")

        //实例化数据
        initData()


        //播放视频准备操作
        prepareVideo()

    }



    private fun initData() {
        //设置视频描述
        tv_video_desc.text = bean.description

        //设置评论的RV
        comment_rv.layoutManager=LinearLayoutManager(this)
        var commentAdapter:CommentAdapter=CommentAdapter(this)
        comment_rv.adapter=commentAdapter

        //设置Item回复点击监听
        commentAdapter.setOnReplyListener(object : CommentAdapter.OnReplyListener{
            override fun setReplyListener(position: Int, view: View) {
                CommentDialog("优质评论将会优先展现",object:CommentDialog.SendListener{
                    override fun sendComment(text: String) {

                    }

                }).show(supportFragmentManager,"comment")
            }

        })

        //底部的评论点击监听
        comment.setOnClickListener {
            CommentDialog("优质评论将会优先展现",object:CommentDialog.SendListener{
                override fun sendComment(text: String) {
                    this@VideoDetailActivity.showToast("发表成功!")
                }

            }).show(supportFragmentManager,"comment")
        }




        //设置关注点击监听
        tv_attention.setOnClickListener {
            var b: Boolean = true
            when (b) {
                true -> {
                    b = false
                    this@VideoDetailActivity.showToast("关注成功!")
                }

                false -> {
                    b = true
                    this@VideoDetailActivity.showToast("取消关注!")
                }
            }
        }


        //设置下载监听
        tv_download.setOnClickListener {

            //想了解更多的关于*** RxDownload ***的使用请参考:https://segmentfault.com/a/1190000009937326

            //先看一下共享参数中是否有保存过该下载路径,如果有，就表示已经下载过
            // var url=SPUtils.getInstance(this,"downloads").getString(bean.playUrl!!)//这种写法虽然好理解，但是我还是推荐下面的Lambda表达式
            var url = bean.playUrl?.let { it -> SPUtils.getInstance(this, "downloads").getString(it) }

            if ("".equals(url)) {
                //表示共享参数中没有存过该下载地址，需要去下载

                //获取一下当前已经缓存的视频数量
                var count: Int = SPUtils.getInstance(this, "downloads").getInt("count")
                if (count != -1) {
                    //表示缓存的个数不为0(即有缓存过视频)

                    //在原来缓存的个数上 自加1
                    count = count + 1
                    Logger.e("当前视频的下载个数:" + count)
                } else {
                    //表示没有缓存视频，因为点击了下载，这时视频缓存的 count= 1
                    count = 1
                }
                //在共享参数中保存一下缓存视频个数
                SPUtils.getInstance(this, "downloads").put("count", count)

                //把下载的视频实体写入到文件中(并且保存文件的命名方式为:  "download"+count)
                /***
                 * @param 上下文
                 * @param 对象保存的文件名称
                 * @param 视频实体
                 */
                /* var list:ArrayList<VideoBean> = ArrayList<VideoBean>()
                list.add(bean)
                SaveObjectInFileUtils.saveObject(this,"download_list",list)
                Logger.e("获取到的数据:"+ SaveObjectInFileUtils.getValue(this,"download_list").toString())*/

                var flag: Boolean = SPUtils.getInstance(this, "flag").getBoolean("flag")
                if (!flag) {
                    //判断视频是否为第一次下载，如果为第一次下载的话，需要先创建一个集合,防止报错。注意在VidoCacheActivity中，如果把缓存的视频都删除了，也要修改flag为false
                    SPUtils.getInstance(this, "flag").put("flag", true)
                    var list: ArrayList<VideoBean> = ArrayList<VideoBean>()
                    list.add(bean)
                    SaveObjectInFileUtils.saveObject(this, "download_list", list)
                } else {
                    var beanList: ArrayList<VideoBean> = SaveObjectInFileUtils.getValue(this, "download_list") as ArrayList<VideoBean>
                    beanList.add(bean)
                    SaveObjectInFileUtils.saveObject(this, "download_list", beanList)

                    Logger.e("获取集合:" + beanList.toString())
                }


                /******************使用第三方的下载工具去下载视频(下载路径，保存路径)***********************/
                RxDownload.getInstance(this)
                      //  .retrofit():我们自己的retrofit在这里指定
                        .maxDownloadNumber(5)//service同时下载数量（如果超过了我们定制的数量会进入等待，等其他下完它就会接着下了）
                        .serviceDownload(bean.playUrl, "download:$url").subscribe({
                            showToast("开始下载")

                            //保存下载路径
                            SPUtils.getInstance(this, "download$count").put(bean?.playUrl.toString(), bean?.playUrl.toString())
                            //保存是否下载的状态(true表示正在下载,false表示不在下载状态)
                            SPUtils.getInstance(this, "download_state").put(bean.playUrl.toString(), true)

                        }, {
                            //在这里判断下载失败的操作
                            showToast("添加下载任务失败!")
                        }, {
                            //在这里判断下载完成的操作
                            //showToast("下载完成!")

                        })


            } else {
                //表示有该地址，表示已经下载过了
                showToast("已经下载过该视频，请到我的页面查看")
            }

        }





    }


    //视频播放
    private fun prepareVideo() {
        var uri = intent.getStringExtra("loaclFile")
        //判断本地是否有缓存了，如果本地有缓存就播放本地的，如果没有就播放网络地址
        if (uri != null) {
            Log.e("kotlinApp", "播放的是本地缓存的地址。缓存的视频地址为:" + uri)

            //设置播放地址（播放地址，是否边播边缓存，缓存地址，视频的标题信息）
            gsy_player.setUp(uri, false, null, null)
        } else {

            //如果本地没有缓存，就播放网络地址
            gsy_player.setUp(bean.playUrl, false, null, bean.title)
        }

        //添加视频的封面
        imagView = ImageView(this)
        imagView.scaleType = ImageView.ScaleType.CENTER_CROP
        //执行异步任务获取图片的Bitmap对象，然后给ImageView设置Bitmap
        ImageViewAsyncTask(mHandler, this, imagView).execute(bean.videoPhoto)


        /*******************实例化视频播放屏幕旋转的工具**********************/
        orientationUtils = OrientationUtils(this, gsy_player)


        //设置视频播放的返回按钮是否可见
        gsy_player.backButton.visibility = View.VISIBLE
        //设置返回按钮的图片
        //gsy_player.backButton.setImageResource(R.mipmap.ic_launcher)
        //设置还回按钮点击监听
        gsy_player.backButton.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        //设置视频的小标题是否可见
        gsy_player.titleTextView.visibility = View.VISIBLE
        //设置标题文本的大小
        gsy_player.titleTextView.setTextSize(15F)



        //设置全屏播放点击监听
        gsy_player.fullscreenButton.setOnClickListener(View.OnClickListener {

            //直接横屏
            orientationUtils.resolveByClick()
            //开始全屏播放（第一个true表示：是否要隐藏actionBar，第二个true表示是否要隐藏statusBar）
            gsy_player.startWindowFullscreen(this, true, true)
        })

        /*****************相关的一些设置*******************/
        gsy_player.setIsTouchWiget(true)
        //关闭自动旋转
        gsy_player.isRotateViewAuto = false
        //这个的效果是，全屏的时候有个缩放的动画。注意!!!记得关闭，不然全屏以后，返回时布局不正确
        gsy_player.isShowFullAnimation = false
        //全屏的时候的锁
        gsy_player.isNeedLockFull = true
        /************************************************/

        //设置GsyPlayer视频播放的所有回调
        gsy_player.setStandardVideoAllCallBack(object : VideoListener() {

            //准备完毕
            override fun onPrepared(url: String?, vararg objects: Any?) {
                super.onPrepared(url, *objects)

                //开始播放才能旋转和全屏显示
                orientationUtils.isEnable = false

                isPlayer = true
            }

            override fun onAutoComplete(url: String?, vararg objects: Any?) {
                super.onAutoComplete(url, *objects)
            }

            override fun onClickStartError(url: String?, vararg objects: Any?) {
                super.onClickStartError(url, *objects)
            }

            //退出全屏的时候
            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                super.onQuitFullscreen(url, *objects)
                orientationUtils?.let {

                    //注意要加上这个（去竖屏播放，如果没有加这个时，按下视频顶部的back图标是不会去竖屏播放的）
                    orientationUtils.backToProtVideo()


                }
            }
        })


        //全屏播放时会显示锁，锁的点击监听
        gsy_player.setLockClickListener { view, lock ->
            //配合下方的onConfigurationChanged
            orientationUtils.isEnable = !lock
        }

    }


    override fun onBackPressed() {
        orientationUtils?.let {
            orientationUtils.backToProtVideo()
        }
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return
        }
        //不然的话响应系统还回
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        //释放视频播放资源
        GSYVideoPlayer.releaseAllVideos()
        orientationUtils?.let {
            //屏幕旋转工具释放相关的监听
            orientationUtils.releaseListener()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        //设置全屏显示(记得在这里也设置一下全屏显示，不然当全屏播放以后，返回竖屏播放会显示状态栏)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        //如果视频还处于播放状态
        if (isPlayer && !isPause) {

            //用户屏幕的首选方向
            if (newConfig?.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                //如果当前的视频播放不是全屏，就设置成全屏播放
                if (!gsy_player.isIfCurrentIsFullscreen) {
                    //开始全屏播放（第一个true表示：是否要隐藏actionBar，第二个true表示是否要隐藏statusBar）
                    gsy_player.startWindowFullscreen(this, true, true)

                }

            }

        } else {
            if (gsy_player.isIfCurrentIsFullscreen) {
                StandardGSYVideoPlayer.backFromWindowFull(this);
            }
            orientationUtils?.let { orientationUtils.isEnable = true }

        }
    }


    //异步任务加载视频的封面
    private class ImageViewAsyncTask(handler: Handler, activity: VideoDetailActivity, mImageView: ImageView) : AsyncTask<String, Void, String>() {
        private var mHandler: Handler = handler
        private var mActivity: VideoDetailActivity = activity
        private var mImageView: ImageView = mImageView

        //图片路径
        private var mPath: String? = null

        //文件写入流
        private var fis: FileInputStream? = null


        //执行耗时操作
        override fun doInBackground(vararg params: String?): String {
            val futureTarget = Glide.with(mActivity)
                    .load(params[0])
                    //加载的图片的宽高
                    .downloadOnly(100, 100)

            val cacheFile = futureTarget.get()
            mPath = cacheFile.absolutePath
            Logger.e("Glide加载后的图片文件路径:" + mPath)

            return mPath as String
        }

        //接收异步任务结果，更新UI
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            //实例化文件输入流
            fis = FileInputStream(result)

            //转化成Bitmap对象
            var bitmap = BitmapFactory.decodeStream(fis)

            //给ImageView设置Bitmap对象
            mImageView.setImageBitmap(bitmap)

            //然后使用Handler更新UI(即设置视频的封面)
            var msg = mHandler.obtainMessage()
            msg.what = MSG_UPDATE_PHOTO
            mHandler.sendMessage(msg)

            //关闭文件输入流
            fis?.close()


        }

    }



}




