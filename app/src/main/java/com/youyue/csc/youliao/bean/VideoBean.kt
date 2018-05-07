package com.youyue.csc.youliao.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by csc on 2018/4/13.
 * explain：用来跳转下一个Activity所携带数据的视频实体
 *并且实现 Parcelable 序列化
 */
data class VideoBean(var videoPhoto:String?, var title:String?, var description:String?,
                     var duration:Long?, var playUrl:String?, var category:String?,
                     var blurred: String?, var cllectCount:Int?, var shareCount:Int?,
                     var replyCount:Int?, var time:Long) : Parcelable,Serializable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(videoPhoto)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeValue(duration)
        parcel.writeString(playUrl)
        parcel.writeString(category)
        parcel.writeString(blurred)
        parcel.writeValue(cllectCount)
        parcel.writeValue(shareCount)
        parcel.writeValue(replyCount)
        parcel.writeLong(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoBean> {
        override fun createFromParcel(parcel: Parcel): VideoBean {
            return VideoBean(parcel)
        }

        override fun newArray(size: Int): Array<VideoBean?> {
            return arrayOfNulls(size)
        }
    }


}




