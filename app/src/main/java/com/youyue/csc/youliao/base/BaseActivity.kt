package com.youyue.csc.youliao.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.utils.SPUtils

/**
 * Created by csc on 2018/4/17.
 * explain：Activity的基类
 */
open class BaseActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        prepareTheme()
        super.onCreate(savedInstanceState)
    }

    private fun prepareTheme() {
        var themeFlag:Int= SPUtils.getInstance(this,"app_theme").getInt("theme")
        if (themeFlag== -1){
            //设置成夜间模式
            setTheme(R.style.DayTheme)
        }else if (themeFlag==1){
            //设置成白天模式
            setTheme(R.style.NightTheme)
        }

    }


}
