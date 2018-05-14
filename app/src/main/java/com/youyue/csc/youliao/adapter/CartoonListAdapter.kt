package com.youyue.csc.youliao.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.base.CartoonListBean

/**
 * Created by csc on 2018/5/14.
 * explain：漫画列表适配器
 */
class CartoonListAdapter (context:Context,beans:List<CartoonListBean.ComicArr>?) : RecyclerView.Adapter<CartoonListAdapter.ListViewHolder>() {
   var mLists:List<CartoonListBean.ComicArr>?=null
    var mContext:Context?=null
    var layoutInflater:LayoutInflater?=null
    init {
        mLists=beans
        mContext=context
        layoutInflater= LayoutInflater.from(mContext)
    }

    //设置新数据，更新适配器
    public fun setData(newBean:List<CartoonListBean.ComicArr>){
        mLists=newBean
        notifyDataSetChanged()

    }



    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewHolder {
        return ListViewHolder(layoutInflater?.inflate(R.layout.item_cartoon_list,parent,false))
    }

    override fun getItemCount(): Int {
        return mLists?.size?:0
    }

    override fun onBindViewHolder(holder: ListViewHolder?, position: Int) {

        var path:String =mLists?.get(position)?.comic_pic_240_320.toString()
        Glide.with(mContext).load("http://gx.cdn.comic.oacg.cn$path").into(holder?.bg_item)

        holder?.title_item?.text=mLists?.get(position)?.comic_name
        holder?.workRoom?.text=mLists?.get(position)?.painter_user_nickname
        holder?.type?.text=mLists?.get(position)?.comic_tag_name
        holder?.desc?.text=mLists?.get(position)?.comic_desc

    }

    class ListViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var bg_item:ImageView
        lateinit var title_item:TextView
        lateinit var workRoom:TextView
        lateinit var type:TextView
        lateinit var desc:TextView

        init {
            bg_item=itemView?.findViewById(R.id.bg_item) as ImageView
            title_item=itemView?.findViewById(R.id.tv_item_cartoon_title) as TextView
            workRoom=itemView?.findViewById(R.id.tv_item_cartoon_work_room) as TextView
            type=itemView?.findViewById(R.id.tv_item_cartoon_type) as TextView
            desc=itemView?.findViewById(R.id.tv_item_cartoon_desc) as TextView

        }


    }
}
