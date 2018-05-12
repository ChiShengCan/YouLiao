package com.youyue.csc.youliao.ui.activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.utils.RxHelper
import com.youyue.csc.youliao.utils.newIntent
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.ref.WeakReference

/**
 * Created by csc on 2018/4/12.
 * explain：启动页
 */

class SplashActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //设置全屏显示
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)


        setContentView(R.layout.activity_splash)

       /* //设置延迟跳转
        MyHandler(this).postDelayed(Runnable {

            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            *//**
            如果使用封装的跳转
            newIntent<MainActivity>()
            *//*

            //结束当前页
            this@SplashActivity.finish()

        },2000)*/

        //开始倒计时
        RxHelper.countDown(5)
                .subscribe({
                    //表示onNext()
                    result ->
                    tv_count.text="跳过广告 $result"+"s"

                },{
                    //表示onError()
                    error ->
                  // newIntent<MainActivity>()
                   newIntent<BottomTabLayoutActivity>()
                    //结束当前页
                    this@SplashActivity.finish()

                },{
                   //表示onCompleted()
                   //  newIntent<MainActivity>()
                    newIntent<BottomTabLayoutActivity>()
                    //结束当前页
                    this@SplashActivity.finish()

                })





    }

    class MyHandler(activity: SplashActivity) : Handler(){
        val mActivity : WeakReference<SplashActivity> = WeakReference(activity)

    }

    override fun onDestroy() {
        MyHandler(this).removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onBackPressed() {
        return
    }

}
