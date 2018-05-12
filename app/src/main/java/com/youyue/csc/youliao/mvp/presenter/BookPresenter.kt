package com.youyue.csc.youliao.mvp.presenter

import android.content.Context
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.base.BookTypeBean
import com.youyue.csc.youliao.mvp.contract.BookContract
import com.youyue.csc.youliao.mvp.model.BookModel
import com.youyue.csc.youliao.utils.applySchedulers
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Created by csc on 2018/4/12.
 * explain:图书分类的Presenter
 */
class BookPresenter(context: Context, view:BookContract.View): BookContract.Presenter{


    var mContext:Context?=null
    var mView:BookContract.View?=null
    val mModel: BookModel by lazy{
        BookModel()
    }

    init {
        mView=view
        mContext=context
    }

    override fun requestBookTypeData() {
        val observable:Observable<BookTypeBean>?=mContext.let {
            mModel.loadBookTypeData(mContext!!)


        }

        observable
                ?.applySchedulers()
                ?.subscribe(Consumer {
                    Logger.e("获取到的数据:"+it.toString())
                    mView?.setBookTypeData(it)
                }, Consumer {
                    Logger.e("获取数据异常:"+it)
                })

    }







}


