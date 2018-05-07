package com.youyue.csc.youliao.utils

import android.content.Context
import com.orhanobut.logger.Logger
import java.io.*

/**
 * Created by csc on 2018/4/18.
 * explain：把对象写入到文件中保存的工具
 */

object SaveObjectInFileUtils{

    fun saveObject(context: Context, name: String, value: Any) {
        var fos: FileOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            fos = context.openFileOutput(name, Context.MODE_PRIVATE)
            oos = ObjectOutputStream(fos)
            oos.writeObject(value)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                fos.close()
            }
            if (oos != null) {
                oos.close()
            }
        }
        Logger.e("写入文件成功!")
    }
    fun getValue(context: Context, name: String): Any? {
        var fis: FileInputStream? = null
        var ois: ObjectInputStream? = null
        try {
            fis = context.openFileInput(name)
            if(fis==null){
                return null
            }
            ois = ObjectInputStream(fis)
            return ois.readObject()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    // fis流关闭异常
                    e.printStackTrace()
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (e: IOException) {
                    // ois流关闭异常
                    e.printStackTrace()
                }
            }
        }
        Logger.e("读取文件成功!")
        return null
    }


    fun deleteFile(name: String,context: Context){
        context.deleteFile(name)
    }

}
