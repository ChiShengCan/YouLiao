package com.youyue.csc.youliao.mvp.presenter

import android.content.Context
import com.youyue.csc.youliao.mvp.contract.HomeContract
import com.youyue.csc.youliao.mvp.contract.MineContract
import com.youyue.csc.youliao.mvp.model.MineModel

/**
 * Created by csc on 2018/4/16.
 * explain：我的页面的Presenter
 */

class MinePresenter(context: Context, view: HomeContract.View): MineContract.Presenter {

    var mContext: Context? = null
    var mView: HomeContract.View? = null
    val mMineModel: MineModel by lazy {
       MineModel()
    }
}
