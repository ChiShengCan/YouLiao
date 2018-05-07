package com.youyue.csc.youliao.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by csc on 2018/4/12.
 * explain：扩展工具
 */


/********************吐司工具**************************/
fun Context.showToast(message: String): Toast {
    var toast: Toast = Toast.makeText(this,message,Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
    return toast
}

/*********************Intent跳转封装*******************/
//Kotlin引入关键字 inline，是为了解决 某个方法被频繁的调用，带来了性能问题
//reified 的意思是具体化。而作为 Kotlin 的一个方法 泛型 关键字，它代表你可以在方法体内访问泛型指定的JVM类对象。必须以 inline 内联方式声明这个方法才有效。
inline fun <reified T : Activity> Activity.newIntent() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

/****************封装一下RxJava的运行线程***************/
fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

