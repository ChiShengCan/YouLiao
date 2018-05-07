package com.youyue.csc.youliao.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.bean.CommentBean

/**
 * Created by csc on 2018/4/21.
 * explain：评论适配器
 */
class CommentAdapter(context: Context?) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    var comments:MutableList<CommentBean>?=null
    var context:Context?=null
    var layoutInflater:LayoutInflater?=null
    init {

        this.context=context
        layoutInflater= LayoutInflater.from(context)

    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentHolder {

        return CommentHolder(layoutInflater?.inflate(R.layout.item_adapter_comment,parent,false),context)

    }

    override fun getItemCount(): Int {
        return 8

    }

    override fun onBindViewHolder(holder: CommentHolder?, position: Int) {
        if (position == 0) {
            Glide.with(context).load(R.drawable.header_10).into(holder?.comment_header)
            holder?.comment_name?.text = "不爱美女爱代码"
            holder?.comment_content?.text = "哈哈哈，这个作者这牛逼"
            holder?.comment_time?.text = "1小时前"
        }else if (position ==1){
            Glide.with(context).load(R.drawable.header_2).into(holder?.comment_header)
            holder?.comment_name?.text = "近在咫尺"
            holder?.comment_content?.text = "6666"
            holder?.comment_time?.text = "1小时前"
        }else if (position ==2){
            Glide.with(context).load(R.drawable.header_3).into(holder?.comment_header)
            holder?.comment_name?.text = "中小企业信息咨询"
            holder?.comment_content?.text = "有什么问题可以拨打18867810923"
            holder?.comment_time?.text = "2小时前"
        }else if (position ==3){
            Glide.with(context).load(R.drawable.header_4).into(holder?.comment_header)
            holder?.comment_name?.text = "有笔"
            holder?.comment_content?.text = "可爱的作者"
            holder?.comment_time?.text = "4小时前"
        }else if (position ==4){
            Glide.with(context).load(R.drawable.header_5).into(holder?.comment_header)
            holder?.comment_name?.text = "豆豆"
            holder?.comment_content?.text = "中兴要玩到了"
            holder?.comment_time?.text = "5小时前"
        }else if (position ==5){
            Glide.with(context).load(R.drawable.header_6).into(holder?.comment_header)
            holder?.comment_name?.text = "城管大队"
            holder?.comment_content?.text = "我们很文明执法"
            holder?.comment_time?.text = "6小时前"
        }else if (position ==6){
            Glide.with(context).load(R.drawable.header_7).into(holder?.comment_header)
            holder?.comment_name?.text = "贝贝"
            holder?.comment_content?.text = "楼主变态"
            holder?.comment_time?.text = "6小时前"
        }else if (position ==7){
            Glide.with(context).load(R.drawable.header_8).into(holder?.comment_header)
            holder?.comment_name?.text = "hehe---"
            holder?.comment_content?.text = "今天还要上班"
            holder?.comment_time?.text = "8小时前"
        }

        //设置回复点击监听
        holder?.comment_reply?.setOnClickListener {
            if (mReplyListener !=null){
               mReplyListener.setReplyListener(position, holder?.comment_reply!!)

            }
        }


    }


    class CommentHolder(itemView: View?,context: Context?) : RecyclerView.ViewHolder(itemView) {
        var comment_header:ImageView?=null
        var comment_name:TextView?=null
        var comment_content:TextView?=null
        var comment_time:TextView?=null
        var comment_reply: LinearLayout?=null

        init {
            comment_header=itemView?.findViewById(R.id.comment_header) as  ImageView
            comment_name=itemView?.findViewById(R.id.comment_name) as TextView
            comment_content=itemView?.findViewById(R.id.comment_content) as TextView
            comment_time=itemView?.findViewById(R.id.comment_time) as TextView
            comment_reply=itemView?.findViewById(R.id.reply_comment) as LinearLayout

        }

    }

    lateinit var mReplyListener:OnReplyListener

    fun setOnReplyListener(onReplyListener:OnReplyListener){
        this.mReplyListener=onReplyListener

    }
    interface OnReplyListener{
        fun setReplyListener(position:Int,view:View)
    }


}
