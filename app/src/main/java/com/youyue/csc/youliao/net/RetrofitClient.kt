package com.youyue.csc.youliao.net

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by csc on 2018/4/12.
 * explain：Retrofit的封装
 */
class RetrofitClient private constructor(context: Context,baseUrl: String){
    var httpCacheDirectory : File?=null
    var cache:Cache?=null
    var okHttpClient : OkHttpClient?=null
    var retrofit:Retrofit?=null
    var mContext:Context=context
    var url:String=baseUrl
    val TIMEOUT:Long=15


    init {
        //设置缓存地址
        if (httpCacheDirectory == null){
            httpCacheDirectory = File(mContext?.cacheDir,"app_cache")
        }
        if (cache == null){
            //设置缓存路径和缓存大小
            cache = Cache(httpCacheDirectory,10*1024*1924)
        }

        //创建OkHttp
        okHttpClient=OkHttpClient.Builder()
                //添加日志拦截器
                .addNetworkInterceptor( HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                //添加缓存
                .cache(cache)
                //添加缓存拦截器
                .addInterceptor(CacheInterceptor(mContext))
                .addNetworkInterceptor(CacheInterceptor(mContext))
                //网络连接超时时长
                .connectTimeout(TIMEOUT,TimeUnit.SECONDS)
                //读写超时
                .writeTimeout(TIMEOUT,TimeUnit.SECONDS)
                .build()

        //创建Retrofit

        retrofit=Retrofit.Builder()
                .client(okHttpClient)
                //添加基地址
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()


    }

    //Kotlin语言中使用"companion object"修饰 静态方法，可以使用类名.方法名的形式调用
    companion object {
        //单例
        @Volatile
        var instence:RetrofitClient?=null

        fun getInstance(context: Context,baseUrl:String): RetrofitClient? {
            if (instence == null){
                synchronized(RetrofitClient::class){
                    if (instence == null){
                        instence = RetrofitClient(context,baseUrl)
                    }
                }
            }

            //当前对象为空不执行
            return instence!!
        }
    }

    //Retrofit的create
    fun <T> create(service : Class<T>?):T?{
        if (service ==null){
            throw RuntimeException("Api service is null!")
        }
        return retrofit?.create(service)
    }
}
