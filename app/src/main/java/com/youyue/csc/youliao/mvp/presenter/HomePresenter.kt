package com.youyue.csc.youliao.mvp.presenter

import android.content.Context
import com.youyue.csc.youliao.bean.HomeBean
import com.youyue.csc.youliao.mvp.contract.HomeContract
import com.youyue.csc.youliao.mvp.model.HomeModel
import com.youyue.csc.youliao.utils.applySchedulers

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by csc on 2018/4/12.
 * explain：首页Presenter
 */
class HomePresenter(context: Context,view:HomeContract.View): HomeContract.Presenter{

    var mContext:Context?=null
    var mView:HomeContract.View?=null
    val mModel: HomeModel by lazy{
        HomeModel()
    }

    init {
        mView=view
        mContext=context
    }


    //加载首页的数据
    override fun requestHomeData() {
        val observable : Observable<HomeBean>?=mContext?.let {
            mModel.loadHomeData(it,"0")
        }

        observable
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { homeBean:HomeBean ->
                    mView?.setHomeData(homeBean)
                }

        /**
        //如果使用我们封装的方法如下写法
        observable
                ?.applySchedulers()
                ?.subscribe { homeBean:HomeBean ->
                    mView?.setHomeData(homeBean)
                }
        */

    }

    //加载更多的数据
    override fun requestHomeMoreData(context: Context,data:String) {
        val observable:Observable<HomeBean>?=mContext?.let {
            mModel.loadHomeMoreData(it,data)
        }
        observable?.applySchedulers()
                ?.subscribe {
                   homeBean:HomeBean ->
                    mView?.setHomeData(homeBean)
                }
    }





}
