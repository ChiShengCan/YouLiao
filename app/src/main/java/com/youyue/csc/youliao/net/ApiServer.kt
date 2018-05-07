package com.youyue.csc.youliao.net

import com.youyue.csc.youliao.bean.HomeBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by csc on 2018/4/12.
 * explain：接口
 */
interface ApiServer{

    //基地址
    companion object {
        val BASE_URL : String
        get() = "http://baobab.kaiyanapp.com/api/"
    }

    //获取首页的第一页数据
    @GET("v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83")
    fun getHomeFirstPageData() : Observable<HomeBean>

    //获取首页的其他数据 ?date=1499043600000&num=2
    @GET("v2/feed")
    fun getHomeMoreData(@Query("date")date:String,@Query("num")num:String):Observable<HomeBean>

}
