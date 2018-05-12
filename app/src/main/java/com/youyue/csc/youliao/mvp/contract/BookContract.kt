package com.youyue.csc.youliao.mvp.contract

import com.youyue.csc.youliao.base.BasePresenter
import com.youyue.csc.youliao.base.BaseView
import com.youyue.csc.youliao.base.BookTypeBean


/**
 * Created by csc on 2018/4/12.
 * explain：图书Contract
 */
interface BookContract{
    interface View : BaseView<Presenter> {

        //设置图书分类数据
        fun setBookTypeData(bean : BookTypeBean)


    }

    interface Presenter : BasePresenter {

        //请求图书类型数据
        fun requestBookTypeData()


    }
}
