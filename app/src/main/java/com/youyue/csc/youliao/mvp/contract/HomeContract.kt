package com.youyue.csc.youliao.mvp.contract

import android.content.Context
import com.youyue.csc.youliao.base.BasePresenter
import com.youyue.csc.youliao.base.BaseView
import com.youyue.csc.youliao.bean.HomeBean


/**
 * Created by csc on 2018/4/12.
 * explain：首页Contract
 */
interface HomeContract{
    interface View : BaseView<Presenter> {
        //设置首页的数据
        fun setHomeData(bean : HomeBean)


    }


    interface Presenter : BasePresenter {

        //请求首页的数据
        fun requestHomeData()

        //请求首页的更多数据
        fun requestHomeMoreData(context: Context,data:String)
    }
}
