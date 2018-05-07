package com.youyue.csc.youliao.ui.activity

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.adapter.VideoCacheAdapter
import com.youyue.csc.youliao.bean.VideoBean
import com.youyue.csc.youliao.utils.SPUtils
import com.youyue.csc.youliao.utils.SaveObjectInFileUtils
import kotlinx.android.synthetic.main.activity_video_cache.*
import zlc.season.rxdownload2.RxDownload

class VideoCacheActivity : AppCompatActivity() {

    //适配器
    var mAdapter: VideoCacheAdapter? = null

    //视频的数据集合
    var mList: ArrayList<VideoBean>? = null

    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            var list: ArrayList<VideoBean> = msg?.data?.getParcelableArrayList<VideoBean>("beans")!!

            //赋值
            mList = list

            //设置适配器
            recyclerView.layoutManager = LinearLayoutManager(this@VideoCacheActivity)
            mAdapter = VideoCacheAdapter(this@VideoCacheActivity, mList)
            recyclerView.adapter = mAdapter



            //设置条目的长按点击监听
            mAdapter!!.setOnLongClickListener(object : VideoCacheAdapter.ItemOnLongClickListener {
                override fun itemOnLongClick(position: Int) {

                    Logger.e("当前的视频剩余的数量:"+ SPUtils.getInstance(this@VideoCacheActivity,"downloads").getInt("count"))



                    //弹出一个对话框
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this@VideoCacheActivity)

                            .setMessage("是否要删除该视频")
                            .setNegativeButton("否", { dialog, which ->
                                dialog.dismiss()
                            })
                            .setPositiveButton("是", { dialog, which ->

                                /*****************************删除条目操作*************************************/
                                deleteItem(position)


                            })
                    var dialog:AlertDialog=builder.create()
                    dialog.show()


                }

            })


        }
    }

    fun deleteItem(position:Int){
        //先把下载该视频的线程服务给删除(其中的 true表示删除该视频的保存文件,flase表示不删除该视频文件)
        RxDownload.getInstance(this@VideoCacheActivity).deleteServiceDownload(mList?.get(position)?.playUrl, true).subscribe {
            Logger.e("RxDownload中删除该文件成功!")
        }

        //然后在共享参数中把该视频置空
        SPUtils.getInstance(this@VideoCacheActivity, "downloads").put(mList?.get(position)?.playUrl!!, "")

        //修改一下视频的缓存数量
        var videoCount=SPUtils.getInstance(this@VideoCacheActivity,"downloads").getInt("count")
        var nowVidoCount=videoCount-1
        SPUtils.getInstance(this@VideoCacheActivity,"downloads").put("count",nowVidoCount)

        //接着移除集合中的数据
        mList?.removeAt(position)

        //然后再我们写入的该视频信息文件给删除
        SaveObjectInFileUtils.saveObject(this@VideoCacheActivity,"download_list",mList!!)


        //最后适配器(只要更新移除的条目信息)
        mAdapter?.notifyDataSetChanged()

        //最后的最后需要判断一下集合是否全部删除,如果全部都删除了，这时我们保存的文件（SaveObjectInFileUtils.saveObject的"download_list"就不存在数组了，下一次重新添加数据下载会报错）
        if (mList?.size ==0){
            SPUtils.getInstance(this, "flag").put("flag", false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_cache)

        //异步加载我们点击缓存的视频数据
        VideoDataAsyncTask(mHandler, this).execute()


        //返回点击监听
        back_cache.setOnClickListener { finish() }


    }


    //异步获取我们缓存的视频(即对应的视频实体)
    private class VideoDataAsyncTask(mHandler: Handler, mActivity: VideoCacheActivity) : AsyncTask<Void, Void, ArrayList<VideoBean>?>() {

        var handler: Handler = mHandler
        var activity: Activity = mActivity

        var list:ArrayList<VideoBean>?=null

        override fun doInBackground(vararg params: Void?): ArrayList<VideoBean>? {

            //注意到存集合的文件没有数据的时候会报错的，需要我们判断一下
            if (SPUtils.getInstance(activity, "flag").getBoolean("flag")){
                list = SaveObjectInFileUtils.getValue(activity, "download_list") as ArrayList<VideoBean>
                return list!!
            }else{
                list=ArrayList()
            }

            /*Logger.e("获取集合的第1条数据:"+listBean.get(0).playUrl)
            Logger.e("获取集合的第2条数据:"+listBean.get(1).playUrl)




            //视频的数据集合
            var list = ArrayList<VideoBean>()

            //获取缓存视频的个数
            var count = SPUtils.getInstance(activity, "downloads").getInt("count")


            //获取到每一个视频的实体，添加到集合中
            var i = 1
            while (i.compareTo(count) <= 0) {
                var bean: VideoBean
                if (SaveObjectInFileUtils.getValue(activity, "download$i") == null) {
                    //根据count读取文件中的一个实体,如果实体为空，就接着读取下一个

                } else {
                    //如果读取到的实体数据不为空，则获取到这个实体数据
                    bean = SaveObjectInFileUtils.getValue(activity, "download$i") as VideoBean

                    //把读取到的实体添加到集合中
                    list.add(bean)

                }


                //自加1，获取下一条数据
                i++
            }
*/
            //还回获取到的视频集合
            return list

        }

        override fun onPostExecute(result: ArrayList<VideoBean>?) {
            super.onPostExecute(result)
            var msg: Message = handler.obtainMessage()
            var bundle = Bundle()
            //保存序列化对象
            bundle.putParcelableArrayList("beans", result)

            msg.data = bundle

            //发送消息
            handler.sendMessage(msg)

        }


    }


    override fun onDestroy() {
        //注意调用一下解除订阅（RxDownload,不然会造成内存的泄漏）
        mAdapter?.unSubcribeRx()
        super.onDestroy()
    }


}




