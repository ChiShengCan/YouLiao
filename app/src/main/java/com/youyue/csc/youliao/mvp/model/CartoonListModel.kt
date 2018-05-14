package com.youyue.csc.youliao.mvp.model

import android.content.Context
import com.youyue.csc.youliao.base.CartoonListBean
import com.youyue.csc.youliao.net.ApiServer
import com.youyue.csc.youliao.net.RetrofitClientForCartoon
import io.reactivex.Observable

/**
 * Created by csc on 2018/4/12.
 * explain：漫画列表的Model
 */
class CartoonListModel{
    //加载漫画的类型(-1表示加载全部的类型)
    var theme_id:Int=-1
    //加载所在的页数
    var page:Int=0
    var map:HashMap<String,Any>?=null
    init {
        map= HashMap()

    }

    //加载漫画列表(map需要传的是类别和页数)
    fun loadBookTypeData(context: Context,theme_id:Int):Observable<CartoonListBean>?{
        map?.put("theme_id",theme_id)
        map?.put("pageidx",page)
        val retrofitClient= RetrofitClientForCartoon.getInstance(context, ApiServer.CARTOON_BASE_URL)
        val apiServer=retrofitClient?.create(ApiServer::class.java)

        this.page++

        return apiServer?.requestCartoonListData(map)

    }





}
