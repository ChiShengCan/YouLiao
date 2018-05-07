package com.youyue.csc.youliao.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.base.BaseApp
import com.youyue.csc.youliao.utils.DensityUtil
import com.youyue.csc.youliao.widget.indicator.Indicator


/**
 * Created by csc on 2018/4/14.
 * explain：首页指示器的适配器
 */

class ScrollIndicatorAdapter : Indicator.IndicatorAdapter{

    var count:Int?=null
    var titles:List<String>?=null

    constructor(lists:List<String>){
        titles=lists

    }

    override fun getCount(): Int {
        return titles?.size?:0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View
        if (convertView == null){
            var layoutInflater=LayoutInflater.from(BaseApp.getInstance())
            view= layoutInflater?.inflate(R.layout.tab_top,parent,false)!!

        }else{
            view=convertView
        }

        var textView:TextView= view as TextView
        textView.width= DensityUtil.dip2px(BaseApp.getInstance(),40F)
        textView.text=titles?.get(position).toString()

        return view



    }

}
