package com.youyue.csc.youliao.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by csc on 2018/5/5.
 * explain：书迷页中的TabLayout的ViewPager的适配器
 */

class BookTabLayoutAdapter (val fm:FragmentManager,var titles:List<String>,var fragments:List<Fragment>)  : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return fragments.get(position)

    }

    override fun getCount(): Int {
        return fragments.size?:0
    }

    //显示标题的名称
    override fun getPageTitle(position: Int): CharSequence {
        return titles.get(position % titles.size)
    }

}
