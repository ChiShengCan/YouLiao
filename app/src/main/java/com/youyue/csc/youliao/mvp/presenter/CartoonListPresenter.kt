package com.youyue.csc.youliao.mvp.presenter

import android.content.Context
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.base.CartoonListBean
import com.youyue.csc.youliao.mvp.contract.CartoonListContract
import com.youyue.csc.youliao.mvp.model.CartoonListModel
import com.youyue.csc.youliao.utils.applySchedulers
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Created by csc on 2018/4/12.
 * explain：漫画列表Presenter
 */
class CartoonListPresenter(context: Context, view:CartoonListContract.View): CartoonListContract.Presenter{


    var mContext:Context?=null
    var mView:CartoonListContract.View?=null
    val mModel: CartoonListModel by lazy{
        CartoonListModel()
    }

    init {
        mView=view
        mContext=context
    }

    //根据选择的漫画类型和分页加载(漫画的类型:-1表示全部，1~15代表对应的类型)
    override fun requestCartoonListData(map: HashMap<String, Any>?) {
        val observable:Observable<CartoonListBean>?=mContext?.let {
            mModel.loadBookTypeData(it,map)
        }

        observable?.applySchedulers()
                ?.subscribe(Consumer {
                  mView?.setCartoonListData(it)
                }, Consumer {
                    Logger.e("加载数据异常:"+it)
                })
    }









}
