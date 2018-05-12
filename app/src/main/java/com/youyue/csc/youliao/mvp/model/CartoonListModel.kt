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

    //加载漫画列表(map需要传的是类别和页数)
    fun loadBookTypeData(context: Context, map: HashMap<String, Any>?):Observable<CartoonListBean>?{
        val retrofitClient= RetrofitClientForCartoon.getInstance(context, ApiServer.CARTOON_BASE_URL)
        val apiServer=retrofitClient?.create(ApiServer::class.java)

        return apiServer?.requestCartoonListData(map)

    }



}
