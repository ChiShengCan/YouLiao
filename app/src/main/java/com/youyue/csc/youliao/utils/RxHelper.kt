package com.youyue.csc.youliao.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit



/**
 * Created by csc on 2018/4/17.
 * explain：倒计时工具
 */
class RxHelper private constructor() {



    init {
        throw AssertionError()
    }

    companion object {



        /**
         * 倒计时
         */
        fun countDown(time: Int): Observable<Int> {
            var time = time
            if (time < 0) {
                time = 0
            }
            val countTime = time

            return Observable.interval(0, 1, TimeUnit.SECONDS)
                    .map(object : Function<Long, Int> {
                        override fun apply(t: Long): Int {
                            return countTime - t!!.toInt()
                        }

                    })
                    .take((countTime + 1).toLong())
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

}
