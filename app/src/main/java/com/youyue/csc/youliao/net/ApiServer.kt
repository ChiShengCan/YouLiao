package com.youyue.csc.youliao.net

import com.youyue.csc.youliao.base.BookTypeBean
import com.youyue.csc.youliao.base.CartoonListBean
import com.youyue.csc.youliao.bean.HomeBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by csc on 2018/4/12.
 * explain：接口
 */
interface ApiServer{

    //基地址
    companion object {

        //视频的基地址
        val BASE_URL : String = "http://baobab.kaiyanapp.com/api/"

        //图书的基地址
        val BOOK_BASE_URL:String="http://www.luliangdev.cn/"

        //关于漫画的基地址
        val CARTOON_BASE_URL:String="http://comic.oacg.cn/"

    }

    //获取首页的第一页数据
    @GET("v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83")
    fun getHomeFirstPageData() : Observable<HomeBean>

    //获取首页的其他数据 ?date=1499043600000&num=2
    @GET("v2/feed")
    fun getHomeMoreData(@Query("date")date:String,@Query("num")num:String):Observable<HomeBean>


    //获取图书分类数据
    @GET("api/classify")
    fun getBookTypeData():Observable<BookTypeBean>

    //获取漫画列表数据(分类，页数)
    @FormUrlEncoded
    @POST("index.php?m=Index&a=type_theme")
    fun requestCartoonListData(@FieldMap paramters: HashMap<String, Any>?):Observable<CartoonListBean>


}
