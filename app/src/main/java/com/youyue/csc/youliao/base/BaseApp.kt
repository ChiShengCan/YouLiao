package com.youyue.csc.youliao.base

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.xiaomi.channel.commonutils.logger.LoggerInterface
import com.xiaomi.mipush.sdk.MiPushClient

/**
 * Created by csc on 2018/4/13.
 * explain：
 */
class BaseApp : Application() {


    companion object {
        //小米推送的AppId和AppKey,以及Log的Tag
        var APP_ID:String="2882303761517784624"
        var APP_KEY:String="5161778467624"
        var TAG:String="com.youyue.csc.youliao"

        //单例
        private var instance:Application?=null
        fun getInstance() : Context{
            return instance!!
        }
    }

    override fun onCreate() {

        super.onCreate()
        instance=this

        //实例化小米推送
        if (shouldInit()){
            MiPushClient.registerPush(this, APP_ID, APP_KEY)
        }
        //开启小米推送的Log
        val newLogger:LoggerInterface=object :LoggerInterface{
            override fun setTag(p0: String?) {
                //ignore
            }

            override fun log(content: String?) {
                Log.d(TAG,content)
            }

            override fun log(content: String?, t: Throwable?) {
                Log.d(TAG,content,t)
            }

        }
        com.xiaomi.mipush.sdk.Logger.setLogger(this,newLogger)



        //实例化Logger日志
       var formatStratrgy: FormatStrategy= PrettyFormatStrategy.newBuilder()
               .showThreadInfo(true)
               .methodCount(0)
               .methodOffset(7)
               .tag("KotlinPlayer")
               .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStratrgy))
    }


    //判断内否实例化小米推送
    private fun shouldInit():Boolean{
        var am:ActivityManager=getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var processInfos:List<ActivityManager.RunningAppProcessInfo> = am.runningAppProcesses
        var mainProcessName:String=packageName
        var myPid:Int = Process.myPid()
        processInfos.forEach {
            if (it.pid == myPid && mainProcessName.equals(it.processName)){
                return true
            }
        }
        return false

    }




}