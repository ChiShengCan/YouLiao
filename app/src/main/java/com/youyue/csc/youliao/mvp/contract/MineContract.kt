package com.youyue.csc.youliao.mvp.contract

import com.youyue.csc.youliao.base.BasePresenter
import com.youyue.csc.youliao.base.BaseView


/**
 * Created by csc on 2018/4/16.
 * explain：我的页面的Contract
 */

interface MineContract{
    interface View : BaseView<Presenter> {

    }


    interface Presenter : BasePresenter {

    }
}
