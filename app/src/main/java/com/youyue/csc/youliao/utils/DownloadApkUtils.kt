package com.youyue.csc.youliao.utils

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.os.Handler
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import java.io.File
import java.lang.ref.WeakReference


/**
 * Created by csc on 2018/4/20.
 * explain：下载APK工具
 */


class DownloadApkUtils() {
    var TAG: String = "AppDownloadManager"

    //Apk的下载地址
    lateinit var url: String

    //软引用Activity
    lateinit var weakReference: WeakReference<Activity>

    //下载进度观察者
    lateinit var mDownloadChangeObserver: DownloadChangeObserver

    //Apk下载完成广播
    lateinit var mDownloadReceiver: DownloadReceiver

    //App名称
    var appName: String?=null


    companion object {
        //根据这个id查找下载的apk文件
        var mReqId: Long? = null

        //Android SDK下载工具类
        lateinit var mDownloadManager: DownloadManager
    }

    /****************下载进度接口回调*****************/
    lateinit var mUpdateListener: OnUpdateListener

    fun setUpdateListener(mUpdateListener: OnUpdateListener) {
        this.mUpdateListener = mUpdateListener
    }

    interface OnUpdateListener {
        fun update(currentByte: Int, totalByte: Int)
    }
    /*************************************************/



    constructor(activity: Activity, appUrl: String) : this() {
        url = appUrl
        weakReference = WeakReference<Activity>(activity)
        mDownloadManager = weakReference.get()?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        mDownloadChangeObserver = DownloadChangeObserver(Handler())
        mDownloadReceiver = DownloadReceiver()
        appName=weakReference.get()?.resources?.getString(R.string.app_name)!!
    }


    /***
     *第一步使用 DownloadManager 下载APK
     * 总结:DownloadManager 是Android自带的SDK，大概的使用流程如下
     * 1、创建一个Request，进行简单的配置(下载地址，文件保存地址等)
     * 2、下载完成以后，系统会发送一个下载完成的广播，我们需要监听关播
     * 3、监听到下载完成的广播后,根据id查找下载的apk文件
     * 4、在代码中执行apk安装
     */
    fun downloadApk(appUrl: String, title: String, desc: String) {
        //注意装不了新的版本，在下载之前应该删除已有的文件

        var apkFile: File = File(weakReference.get()?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), appName + ".apk")
        if (apkFile.exists() && apkFile != null) {
            Logger.e("已存在该文件，现在已经删除!")
            apkFile.delete()
        }

        var request: DownloadManager.Request = DownloadManager.Request(Uri.parse(appUrl))
        //设置标题
        request.setTitle(title)
        //设置描述
        request.setDescription(desc)
        //下载完成以后显示状态栏的通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        //设置Apk保存路径
        request.setDestinationInExternalFilesDir(weakReference.get(), Environment.DIRECTORY_DOWNLOADS, appName)

        //在手机的SD卡上创建一个download文件夹
        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir()
        //指定下载到SD卡的/download/my/目录下
        //request.setDestinationInExternalPublicDir("/codoon/","csc.apk")

        request.setMimeType("application/vnd.android.package-archive")

        //调用DownloadManager.enqueue(request)开始下载
        //我们需要记住这个ReqId，因为下载完成之后，我们需要根据这个ID去查找apk文件，然后安装APK
        mReqId = mDownloadManager.enqueue(request)

    }

    /***
     * 第二步更新下载进度
     * 1、首先我们需要定义一个观察者DownloadChangeObserver观察下载进度
     * 2、在DownloadChangeObserver中更新UI进度，给用户提示
     * 3、下载之前，需要在Activity中注册Observer
     */
    inner class DownloadChangeObserver(handler: Handler) : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            //去更新UI操作（在这个方法中可以查看已下载的字节数等信息）
            updateView()

        }
    }

    private fun updateView() {
        val bytesAndStatus = intArrayOf(0, 0, 0)
        var query: DownloadManager.Query = DownloadManager.Query().setFilterById(mReqId!!)
        var c: Cursor? = null
        try {
            c = mDownloadManager.query(query)
            if (c != null && c.moveToFirst()) {
                //已经下载的字节数
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                //总需下载的字节数
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                //状态所在的列索引
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            }

        } finally {
            if (c != null) {
                c.close()
            }
        }

        //设置我们写的下载进度的回调接口
        if (mUpdateListener != null) {
            mUpdateListener.update(bytesAndStatus[0], bytesAndStatus[1])
        }
        Logger.e("下载进度:" + bytesAndStatus[0] + "/" + bytesAndStatus[1])

    }

    /***
     * 第三部定义个广播，获取下载的结果
     * DownloadManager在下载完成之后，会发送一个下载完成的关播。我们只要监听这个广播，接收广播后，获取apk文件安装
     */
   class DownloadReceiver : BroadcastReceiver() {
        lateinit var uri: Uri

        override fun onReceive(context: Context?, intent: Intent?) {

            /***
             * 如果你有耐心看到这里哈哈，恭喜你基本完成了app的在线更新功能。
             */

            //下面的写法只能在Android6.0以下可以正常运行的，但是其他的版本就会有问题的

             var completeDownloadId:Long= intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)!!
            Logger.e("-------------->接收到了Apk下载完成广播<------------")
             var intentInstall:Intent= Intent()
             intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
             intentInstall.setAction(Intent.ACTION_VIEW)

             if (completeDownloadId == mReqId){
                 uri = mDownloadManager.getUriForDownloadedFile(completeDownloadId)
             }
             intentInstall.setDataAndType(uri, "application/vnd.android.package-archive")
             context?.startActivity(intentInstall)


        }

    }



    //下载取消
    fun cancel() {

        mDownloadManager.remove(mReqId!!)
    }

    //先要获取下载进度需要在下载之前先注册
    /***
     * 记得在Activity的对应的 onResume()生命周期中调用此方法
     */
    fun onResume() {
        //注册apk的下载进度监听
        weakReference.get()?.contentResolver?.registerContentObserver(Uri.parse(url), true, mDownloadChangeObserver)

        //注册Apk下载完成广播(监听APK是否下载完成)
        weakReference.get()?.registerReceiver(mDownloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }

    //对应的Activity生命周期中调用
    fun onPause() {
        //解注册apk下载进度监听
        weakReference.get()?.contentResolver?.unregisterContentObserver(mDownloadChangeObserver)

        //解注册Apk下载完成广播
        weakReference.get()?.unregisterReceiver(mDownloadReceiver)
    }


}
