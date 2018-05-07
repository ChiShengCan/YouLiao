package com.youyue.csc.youliao.utils

import android.content.Context
import android.content.pm.PackageManager


/**
 * Created by csc on 2018/4/20.
 * explain：获取当前的版本和版本号的工具
 */
class GetVersion{
   companion object {
       fun getLocalVersion(ctx: Context): Int {
           var localVersion = 0
           try {
               val packageInfo = ctx.getApplicationContext()
                       .getPackageManager()
                       .getPackageInfo(ctx.getPackageName(), 0)
               localVersion = packageInfo.versionCode

           } catch (e: PackageManager.NameNotFoundException) {
               e.printStackTrace()
           }

           return localVersion
       }

       /**
        * 获取本地软件版本号名称
        */
       fun getLocalVersionName(ctx: Context): String {
           var localVersion = ""
           try {
               val packageInfo = ctx.getApplicationContext()
                       .getPackageManager()
                       .getPackageInfo(ctx.getPackageName(), 0)
               localVersion = packageInfo.versionName
           } catch (e: PackageManager.NameNotFoundException) {
               e.printStackTrace()
           }

           return localVersion
       }
   }

}
