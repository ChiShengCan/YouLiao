package com.youyue.csc.youliao.mvp.model

import android.content.Context
import com.youyue.csc.youliao.bean.HomeBean
import com.youyue.csc.youliao.net.ApiServer
import com.youyue.csc.youliao.net.RetrofitClient

import io.reactivex.Observable

/**
 * Created by csc on 2018/4/12.
 * explain：首页的Model
 */
class HomeModel{
    //加载首页的数据
    fun loadHomeData(context: Context,data:String):Observable<HomeBean>?{
        val retrofitClient= RetrofitClient.getInstance(context, ApiServer.BASE_URL)
        val apiServer=retrofitClient?.create(ApiServer::class.java)

        return apiServer?.getHomeFirstPageData()

    }

    //加载首页的更多数据
    fun loadHomeMoreData(context: Context,data:String):Observable<HomeBean>?{
        val retrofitClient=RetrofitClient.getInstance(context,ApiServer.BASE_URL)
        val apiServer=retrofitClient?.create(ApiServer::class.java)
        return  apiServer?.getHomeMoreData(data.toString(),"2")
    }

}
