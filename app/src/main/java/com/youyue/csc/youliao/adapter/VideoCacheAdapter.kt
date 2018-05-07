package com.youyue.csc.youliao.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.bean.VideoBean
import com.youyue.csc.youliao.ui.activity.VideoDetailActivity
import com.youyue.csc.youliao.utils.SPUtils
import com.youyue.csc.youliao.utils.showToast
import io.reactivex.disposables.Disposable
import zlc.season.rxdownload2.RxDownload
import zlc.season.rxdownload2.entity.DownloadFlag
import zlc.season.rxdownload2.entity.DownloadStatus

/**
 * Created by csc on 2018/4/18.
 * explain：视频缓存界面的适配器
 */
class VideoCacheAdapter(context: Context, list: ArrayList<VideoBean>?) : RecyclerView.Adapter<VideoCacheAdapter.VideoCacheHolder>() {

    var context: Context? = null
    var list: ArrayList<VideoBean>? = null
    var layoutInflater: LayoutInflater? = null

    //是否正在下载
    var isDownload: Boolean = false

    //是否下载完成(主要的作用就是跳转到播放页的时候，是播放本地的视频还是播放网络的)
    var hadLoad: Boolean = false


    //rxjava固然好用，但是在订阅以后没有及时的取消订阅，会导致activity或者fragment销毁后仍然占用内存，无法释放。所以可以用disposable来取消订阅
     var disposable: Disposable?=null


    init {
        this.context = context
        this.list = list
        layoutInflater = LayoutInflater.from(context)

    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VideoCacheHolder {
        return VideoCacheHolder(layoutInflater?.inflate(R.layout.item_adapter_cache, parent, false), context!!)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0

    }
    override fun onBindViewHolder(holder: VideoCacheHolder?, position: Int) {

        //从共享参数中获得该视频的下载状态
        isDownload = SPUtils.getInstance(context!!, "download_state").getBoolean(list?.get(position)?.playUrl!!)

        //设置视频的封面图片
        Glide.with(context).load(list?.get(position)?.videoPhoto).into(holder?.video_imgv)
        //设置标题
        holder?.video_title?.text = list?.get(position)?.title

        //获得视频的下载状态，设置对应的数据
        getDownloadState(list?.get(position)?.playUrl!!, holder)

        //根据视频的下载状态设置不同的图标
        if (isDownload) {
            holder?.start_pause_flag?.setImageResource(R.drawable.pause_cache)
        } else {
            holder?.start_pause_flag?.setImageResource(R.drawable.start_cache)
        }
        //设置开始暂停下载的监听
        holder?.start_pause_flag?.setOnClickListener {

            //获取一下该视频的下载状态
            isDownload= SPUtils.getInstance(context!!, "download_state").getBoolean(list?.get(position)?.playUrl!!)

            if (isDownload) {
                //如果是下载，则把下载状态改成不是
                isDownload = false
                holder?.start_pause_flag?.setImageResource(R.drawable.start_cache)

                //修改一下该视频的下载状态
                SPUtils.getInstance(context!!, "download_state").put(list?.get(position)?.playUrl!!, false)

                //暂停下载
                RxDownload.getInstance(context!!).pauseServiceDownload(list?.get(position)?.playUrl).subscribe()


            } else {
                //表示重新下载
                isDownload = true
                holder?.start_pause_flag?.setImageResource(R.drawable.pause_cache)

                //修改一下该视频的下载状态
                SPUtils.getInstance(context!!, "download_state").put(list?.get(position)?.playUrl!!, true)

                //开始重新下载(如果该URL支持断点续传的话，就会继续下载。如果不支持的话就会重新下载)
                RxDownload.getInstance(context).serviceDownload(list?.get(position)?.playUrl, "download$position+1").subscribe({
                   // context?.showToast("开始下载!")
                    Logger.e("开始下载视频:"+list?.get(position)?.playUrl)
                }, {
                    context?.showToast("下载视频失败!")
                    Logger.e("下载视频失败:"+list?.get(position)?.playUrl)
                })
            }

        }

        //条目点击监听（跳转到播放详情页）
        holder?.itemView?.setOnClickListener{
            var intent: Intent = Intent(context,VideoDetailActivity::class.java)
            //描述
            var decription:String?=list?.get(position)?.description
            //播放地址
            var playUrl:String?=list?.get(position)?.playUrl
            //模糊图
            var blurred:String?=list?.get(position)?.blurred
            //收藏数
            var collectCount:Int?=list?.get(position)?.cllectCount
            //分享次数
            var shareCount:Int?=list?.get(position)?.shareCount
            //回复条数
            var replyCount:Int?=list?.get(position)?.replyCount

        }



        //设置条目的长按监听
        holder?.itemView?.setOnLongClickListener {
            mOnLongListener.itemOnLongClick(position)
            true
        }
    }



    class VideoCacheHolder(itemView: View?, context: Context) : RecyclerView.ViewHolder(itemView) {
        //视频的图片
        var video_imgv: ImageView = itemView?.findViewById(R.id.imgv_video) as ImageView
        //视频的标题
        var video_title: TextView = itemView?.findViewById(R.id.video_title) as TextView
        //视频的缓存状态标识
        var video_cache_flag: TextView = itemView?.findViewById(R.id.flag_cache) as TextView
        //开始缓存和暂停缓存的图标
        var start_pause_flag: ImageView = itemView?.findViewById(R.id.video_cache_operation) as ImageView

    }

    //获得视频的下载状态
    private fun getDownloadState(playUrl: String?, holder: VideoCacheHolder?) {
        disposable = RxDownload.getInstance(context).receiveDownloadStatus(playUrl)
                .subscribe { event ->
                    if (event.flag == DownloadFlag.FAILED) {
                        var throwable: Throwable = event.error
                        Logger.e("视频下载异常:" + throwable)
                    }

                    //下载状态
                    var downloadStatus: DownloadStatus = event.downloadStatus

                    //返回文件中大小，单位为byte
                    var totalSize: Long = downloadStatus.totalSize
                    //返回已下载总大小，单位byte
                    var downloadSize: Long = downloadStatus.downloadSize
                    //获得格式化后的哦文件总大小，如:10MB
                    var fomatTotalSize: String = downloadStatus.formatTotalSize
                    //获得格式化的下载的大小,如:5KB
                    var fomatDownloadSize: String = downloadStatus.formatDownloadSize
                    //获得格式化的状态字符串，如:2MB/36MB
                    var formatStatus: String = downloadStatus.formatStatusString
                    //获得下载百分比,保留两位小数，如:5.28%
                    var getPercent: String = downloadStatus.percent


                    //下载的进度(数字)
                    var percent: Long = downloadStatus.percentNumber

                    if (percent == 100L) {
                        //表示下载完成，需要我们取消订阅，不然会造成内存的泄漏
                        if (!disposable!!.isDisposed && disposable != null) {
                            //disposable.dispose()
                        }

                        //设置已经下载状态为完成
                        hadLoad = true

                        //设置开始暂停的图标不可见
                        holder?.start_pause_flag?.visibility = View.GONE
                        //设置显示已缓存和文件大小
                        holder?.video_cache_flag?.text = "已缓存|$fomatTotalSize"

                        //设置是否在下载，不是
                        isDownload = false

                        //共享参数中保存一下下载状态(true表示正在下载，false表示不在下载)
                        SPUtils.getInstance(context!!, "download_state").put(playUrl!!, false)

                    } else {
                        //表示还在下载中
                        Logger.e("当前的下载进度:" + percent)

                        if (holder?.start_pause_flag?.visibility != View.VISIBLE) {
                            //设置下载暂停图标可见
                            holder?.start_pause_flag?.visibility = View.VISIBLE
                        }

                        if (isDownload) {
                            //表示该视频正在下载
                            holder?.video_cache_flag?.text = "下载中 / $percent%"
                        } else {
                            holder?.video_cache_flag?.text = "暂停 / $percent%"
                        }

                    }
                }
    }


    public fun unSubcribeRx() {
        //需要我们取消订阅，不然会造成内存的泄漏
        if (disposable !=null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }

    }

    //条目长按点击监听
    interface ItemOnLongClickListener{
        fun itemOnLongClick(position: Int)
    }

    lateinit var mOnLongListener:ItemOnLongClickListener

    fun setOnLongClickListener(onLongClickListener: ItemOnLongClickListener){
        mOnLongListener=onLongClickListener
    }




}








