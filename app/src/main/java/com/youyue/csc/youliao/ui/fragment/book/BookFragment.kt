package com.youyue.csc.youliao.ui.fragment.book


import android.content.res.Resources
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.adapter.BookTabLayoutAdapter
import java.lang.reflect.Field


/**
 * A simple [Fragment] subclass.
 *
 */
class BookFragment : Fragment(){



    private lateinit var fragments: MutableList<Fragment>
    private lateinit var titles: MutableList<String>
    private lateinit var boyBookFragment: BoyBookFragment
    private lateinit var girlBookFragment: GirlBookFragment
    private lateinit var famousBookFragment: FamousBookFragment
    private  var tabLayoutAdapter: BookTabLayoutAdapter?=null



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {




        var view=inflater?.inflate(R.layout.fragment_book, container, false)

        var tabLayout:TabLayout=view?.findViewById(R.id.tabLayout_book) as TabLayout
        var viewPager:ViewPager=view?.findViewById(R.id.viewPager_book) as ViewPager

        //设置指示器的长度
        setIndicator(tabLayout,5f,5f)

        fragments = ArrayList<Fragment>()
        titles = ArrayList<String>()


        boyBookFragment = BoyBookFragment()
        girlBookFragment = GirlBookFragment()
        famousBookFragment = FamousBookFragment()

        titles.add("男生")
        titles.add("女生")
        titles.add("出版")
        fragments.add(boyBookFragment)
        fragments.add(girlBookFragment)
        fragments.add(famousBookFragment)

        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)))
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)))
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)))

        if (tabLayoutAdapter ==null){
            tabLayoutAdapter=BookTabLayoutAdapter(activity.supportFragmentManager,titles,fragments)
        }

        viewPager.adapter=tabLayoutAdapter
        tabLayout.setupWithViewPager(viewPager)

        return view

    }


    //设置指示器的长度(通过反射)
    public fun setIndicator(tabs: TabLayout,leftDip:Float,rightDip:Float){
        var tabLayout =tabs.javaClass
        var tabStrip:Field
        tabStrip= tabLayout.getDeclaredField("mTabStrip")
        tabStrip.isAccessible=true
        var llTab:LinearLayout
        llTab=tabStrip.get(tabs) as LinearLayout

        var left:Float= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics()) as Float
        var right:Float=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics()) as Float

        for (i in 0 until llTab.childCount){
            var child:View=llTab.getChildAt(i)
            child.setPadding(0,0,0,0)
            var params:LinearLayout.LayoutParams= LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.leftMargin=left as Int
            params.rightMargin=right as Int
            child.layoutParams=params
            child.invalidate()
        }
    }




}
