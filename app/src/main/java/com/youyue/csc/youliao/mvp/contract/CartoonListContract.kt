package com.youyue.csc.youliao.mvp.contract

import com.youyue.csc.youliao.base.BasePresenter
import com.youyue.csc.youliao.base.BaseView
import com.youyue.csc.youliao.base.CartoonListBean


/**
 * Created by csc on 2018/4/12.
 * explain：漫画列表页Contract
 */
interface CartoonListContract{
    interface View : BaseView<Presenter> {
        //设置漫画列表页数据
        fun setCartoonListData(bean : CartoonListBean)


    }


    interface Presenter : BasePresenter {

        //请求漫画列表数据(传入的参数是选择漫画的类型:-1表示全部，1~15表示对应的类型，和页数，用来分页加载的)
        fun requestCartoonListData(map: HashMap<String, Any>?)


    }
}
