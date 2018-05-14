package com.youyue.csc.youliao.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Parcelable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.bean.HomeBean
import com.youyue.csc.youliao.bean.VideoBean
import com.youyue.csc.youliao.ui.activity.VideoDetailActivity
import com.youyue.csc.youliao.utils.showToast
import com.youyue.csc.youliao.widget.likeview.RxShineButton


/**
 * Created by csc on 2018/4/12.
 * explain：首页的适配器
 */
class HomeAdapter(context: Context,list: MutableList<HomeBean.IssueListBean.ItemListBean>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){
    //把List定义成MutableList，是可读可写的，我们可以对list进行add，remove等一系列操作
    var mLists:MutableList<HomeBean.IssueListBean.ItemListBean>?=null

    var context:Context?=null

    var layoutInflater:LayoutInflater?=null



    //实例化
    init {
        this.context=context
        this.mLists=list
        this.layoutInflater= LayoutInflater.from(context)
    }



    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeViewHolder {
       val  homeViewHolder=HomeViewHolder(layoutInflater?.inflate(R.layout.item_adapter_home,parent,false),context!!)

        return homeViewHolder

    }


    //设置对应条目的数据
    override fun onBindViewHolder(holder: HomeViewHolder?, position: Int) {
        //关注状态
        var flag:Boolean=true

        //获取对应的条目实体
        var bean=mLists?.get(position)

        var title = bean?.data?.title
        var category = bean?.data?.category
        var minute = bean?.data?.duration?.div(60)
        var second = bean?.data?.duration?.minus((minute?.times(60)) as Long )
        var realMinute : String
        var realSecond : String
        if(minute!!<10){
            realMinute = "0"+minute
        }else{
            realMinute = minute.toString()
        }
        if(second!!<10){
            realSecond = "0"+second
        }else{
            realSecond = second.toString()
        }
        //播放地址
        var playUrl = bean?.data?.playUrl
        //背景图片
        var photo = bean?.data?.cover?.feed
        //用户头像
        var authorHeader = bean?.data?.author

        holder?.tv_title?.text = title
        holder?.tv_detail?.text = "类别:$category"
        holder?.tv_time?.text=" $realMinute:$realSecond\""
        holder?.tv_msg_count?.text=""+bean?.data?.consumption?.replyCount.toString()

        //加载圆形头像
        Glide.with(context).load(authorHeader?.icon).asBitmap().centerCrop().into(object : BitmapImageViewTarget(holder?.iv_user) {  //记得必须是静态图片asBitmap()

            override fun setResource(resource: Bitmap?) {
                super.setResource(resource)
                //val circular = RoundedBitmapDrawableFactory.create(BaseApp.getInstance(), resource)
                val circular = RoundedBitmapDrawableFactory.create(context?.resources,resource)
                circular.setCircular(true)//画圆
                holder?.iv_user?.setImageDrawable(circular)
            }

        })

        //设置视频图片
        Glide.with(context).load(photo).into(holder?.iv_photo)

        //设置关注点击监听
        holder?.ll_attention?.setOnClickListener{

            when(flag){
                true -> {
                    flag=false
                    context?.showToast("关注成功!")
                    Glide.with(context).load(R.drawable.attention_selected).into(holder?.iv_attention)
                }

                false -> {
                    flag=true
                    context?.showToast("取消关注!")
                    Glide.with(context).load(R.drawable.attention_normal).into(holder?.iv_attention)

                }
            }


        }

        //点赞点击监听
       /* holder?.like?.OnButtonClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var like=v as RxShineButton
                if (like.isChecked){
                    Toast.makeText(context,"谢谢你的赞赏!", Toast.LENGTH_SHORT)
                }else{
                    Toast.makeText(context,"请不要吝啬你的赞美!", Toast.LENGTH_SHORT)
                }
            }

        })*/
        holder?.like?.setOnClickListener {

            Toast.makeText(context,"谢谢你的赞赏!", Toast.LENGTH_SHORT)

           /* var like=it as RxShineButton
            if (like.isChecked){
                Toast.makeText(context,"谢谢你的赞赏!", Toast.LENGTH_SHORT)
            }else{
                Toast.makeText(context,"请不要吝啬你的赞美!", Toast.LENGTH_SHORT)
            }*/
        }


        holder?.itemView?.setOnClickListener{
            //跳转到视频播放详情页
            var intent=Intent(context, VideoDetailActivity::class.java)
            //视频的描述
            var desc = bean?.data?.description
            //视频的时长
            var duration = bean?.data?.duration
            //视频的播放地址
            var playUrl = bean?.data?.playUrl
            //高斯模糊的背景图片
            var blurred = bean?.data?.cover?.blurred

            //收藏数量
            var collectCount = bean?.data?.consumption?.collectionCount
            //分享数量
            var shareCount = bean?.data?.consumption?.shareCount
            //评论
            var replyCount = bean?.data?.consumption?.replyCount

            //当前的系统时间
            var time = System.currentTimeMillis()

            //序列化(Parcelable)后的VidoBean实体来传递数据
            var videoBean= VideoBean(photo,title,desc,duration,playUrl,
                    category,blurred,collectCount,shareCount,replyCount,time)

            intent.putExtra("data",videoBean as Parcelable)

            context?.startActivity(intent)

        }


    }

    //设置总条目
    override fun getItemCount(): Int {
        return mLists?.size?:0

    }



    class HomeViewHolder(itemView : View ?,context: Context) : RecyclerView.ViewHolder(itemView) {
        var tv_detail : TextView?=null
        var tv_title : TextView?=null
        var tv_time : TextView?=null
        var iv_photo : ImageView?=null
        var iv_user : ImageView?=null
        var tv_msg_count:TextView?=null
        var ll_attention:LinearLayout?=null
        //关注图片
        var iv_attention:ImageView?=null

        //点赞
        var like: RxShineButton?=null

        init {
            tv_detail=itemView?.findViewById(R.id.tv_detail)as TextView
            tv_title=itemView?.findViewById(R.id.tv_title)as TextView
            tv_time=itemView?.findViewById(R.id.tv_time) as TextView
            iv_photo=itemView?.findViewById(R.id.iv_photo)as ImageView
            iv_user=itemView?.findViewById(R.id.iv_user)as ImageView
            tv_msg_count=itemView?.findViewById(R.id.tv_msg_count) as TextView
            ll_attention=itemView?.findViewById(R.id.ll_attention) as LinearLayout
            iv_attention=itemView?.findViewById(R.id.iv_attention) as ImageView


        }

    }
}
