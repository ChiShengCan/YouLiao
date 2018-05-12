package com.youyue.csc.youliao.mvp.model

import android.content.Context
import com.youyue.csc.youliao.base.BookTypeBean
import com.youyue.csc.youliao.net.ApiServer
import com.youyue.csc.youliao.net.RetrofitClientForBook
import io.reactivex.Observable

/**
 * Created by csc on 2018/4/12.
 * explain：图书的Model
 */
class BookModel{

    //加载图书分类信息
    fun loadBookTypeData(context: Context):Observable<BookTypeBean>?{
        val retrofitClient= RetrofitClientForBook.getInstance(context, ApiServer.BOOK_BASE_URL)
        val apiServer=retrofitClient?.create(ApiServer::class.java)

        return apiServer?.getBookTypeData()

    }



}
