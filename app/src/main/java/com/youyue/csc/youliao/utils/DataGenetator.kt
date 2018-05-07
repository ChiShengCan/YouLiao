package com.youyue.csc.youliao.utils

import android.content.Context
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.ui.fragment.book.BookFragment
import com.youyue.csc.youliao.ui.fragment.HomeFragment
import com.youyue.csc.youliao.ui.fragment.HotFragment
import com.youyue.csc.youliao.ui.fragment.MineFragment


/**
 * Created by csc on 2018/4/24.
 * explain：TabLayout的切换的数据控制器
 */
class DataGenetator {
    companion object {
        var mTabs = intArrayOf(R.drawable.home_normal, R.drawable.book_normal, R.drawable.find_normal, R.drawable.mine_normal)
        var mTabsSelected = intArrayOf(R.drawable.home_selected, R.drawable.book_selected, R.drawable.find_selected, R.drawable.mine_selected)
        var mTabTitle: Array<String> = arrayOf("首页", "书迷", "发现", "我的")

        fun getFragments(): Array<Fragment?> {
            var mFragments: Array<Fragment?> = arrayOfNulls<Fragment>(4)
            mFragments[0] = HomeFragment.newInstance()
            mFragments[1] = BookFragment()
            mFragments[2] = HotFragment()
            mFragments[3] = MineFragment()

            return mFragments

        }

        /***
         * 获取Tab显示的内容
         */
        fun getTabView(context: Context, position: Int): View {
            //加载底部的一个button的布局
            var view: View = LayoutInflater.from(context).inflate(R.layout.tab_content, null)
            var tabIcon: ImageView = view.findViewById(R.id.tab_content_iv) as ImageView
            tabIcon.setImageResource(DataGenetator.mTabs[position])
            var tabText: TextView = view.findViewById(R.id.tab_content_tv) as TextView


            tabText.setText(mTabTitle[position])

            return view
        }

        //如果有小红点请调用这个方法
        fun getTabView(context: Context, position: Int,redCount:Array<String>): View {
            //加载底部的一个button的布局
            var view: View = LayoutInflater.from(context).inflate(R.layout.tab_content, null)
            //图标
            var tabIcon: ImageView = view.findViewById(R.id.tab_content_iv) as ImageView
            tabIcon.setImageResource(DataGenetator.mTabs[position])
            //标题
            var tabText: TextView = view.findViewById(R.id.tab_content_tv) as TextView
            tabText.setText(mTabTitle[position])
            //小红点
            var redText:TextView=view.findViewById(R.id.tab_red_count) as TextView
            redText.setText(redCount[position])

            return view
        }
    }


}
