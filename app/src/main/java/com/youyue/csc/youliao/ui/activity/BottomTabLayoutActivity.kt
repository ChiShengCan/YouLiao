package com.youyue.csc.youliao.ui.activity

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.base.BaseActivity
import com.youyue.csc.youliao.ui.fragment.HomeFragment
import com.youyue.csc.youliao.utils.DataGenetator
import com.youyue.csc.youliao.utils.SPUtils
import com.youyue.csc.youliao.utils.showToast
import kotlinx.android.synthetic.main.activity_bottom_tab_layout.*


//主要实现的是底部4个按钮来切换页面
class BottomTabLayoutActivity : BaseActivity() {

    lateinit var mFragments: Array<Fragment?>
    lateinit var mTabLayout:TabLayout
    lateinit var mFrameLayout:FrameLayout

    //用来记录点击首页是切换到首页的Fragment还是去刷新
    var homeRefresh:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.e("这个方法执行了一遍--------------------------")
        setContentView(R.layout.activity_bottom_tab_layout)

        mFragments=DataGenetator.getFragments()

        initView()
    }

    private fun initView() {
        mTabLayout=bottom_tab_layout


        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Logger.e("这个监听表示再次的点击到了供一个位置:"+tab?.position)

                //在这里我们即可以仿头条，当再一次点击的时候是刷新操作
                if (0 == tab?.position) {
                    this@BottomTabLayoutActivity.showToast("刷新数据成功!")
                    //调用首页的刷新
                  var  homeFragment:Fragment=mFragments[0] as HomeFragment
                    //然后调用首页的刷新

                }



            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                //根据选中的Tab切换Fragment
                onTabItemSelected(tab?.position!!)


                //改变Tab的状态
                for (i in 0 until mTabLayout.tabCount){
                    var view: View = mTabLayout?.getTabAt(i)?.customView!!
                    var icon: ImageView =view.findViewById(R.id.tab_content_iv) as ImageView
                    var text:TextView=view.findViewById(R.id.tab_content_tv) as TextView
                    if (i== tab?.position){
                        //选中的状态
                       icon.setImageResource(DataGenetator.mTabsSelected[i])
                        text.setTextColor(resources.getColor(android.R.color.black))

                    }else{
                        //未选中的状态
                        icon.setImageResource(DataGenetator.mTabs[i])
                        text.setTextColor(resources.getColor(android.R.color.darker_gray))

                    }
                }
            }

        })

        //这里需要注意一点就是，addTab之前需要先设置addOnTabSelectedListener()监听而触发 onTabItemSelected()方法，不然首次进去不会触发首页的选中

        //提供自定义的布局添加Tab
        for (i in 0 until 4){
            //没有小红点调用这个方法
           // mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenetator.getTabView(this,i)))

            //有小红点
            //实例化小红点的数据(这里需要我们从网络获取)
            var mRedCount: Array<String> = arrayOf("15", "1", "1", "4")
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenetator.getTabView(this,i,mRedCount)))
        }



    }

    //根据选中的Tab切换Fragment
    private fun onTabItemSelected(position:Int){
        var fragment:Fragment?=null
        Logger.e("执行到这里了----------------------------")
        when(position){
            0 -> {
              fragment = mFragments[0]
            }

            1 ->{
                fragment=mFragments[1]
            }

            2->{
                fragment=mFragments[2]
            }
            3->{
                fragment=mFragments[3]
            }
        }
        if (fragment!=null){
            supportFragmentManager.beginTransaction().replace(R.id.content,fragment).commit()
        }
    }


    //日夜间模式的切换
    public fun changeTheme(){
        var themeFlag:Int= SPUtils.getInstance(this,"app_theme").getInt("theme")
        if (themeFlag== -1){
          //设置成夜间模式
            setTheme(R.style.NightTheme)
        }else if (themeFlag==1){
            //设置成白天模式
            setTheme(R.style.DayTheme)
        }


        //更新状态栏的颜色
        if (Build.VERSION.SDK_INT >= 21){
            var typedValue:TypedValue = TypedValue()
            var theme: Resources.Theme=getTheme()
            theme.resolveAttribute(R.attr.colorPrimary,typedValue,true)
            window?.setStatusBarColor(resources.getColor(typedValue.resourceId))

        }


        //更新主界面的配色
        var background:TypedValue = TypedValue()//背景色
        var theme:Resources.Theme = theme
        theme.resolveAttribute(R.attr.colorBackground,background,true)
        main_background.setBackgroundResource(background.resourceId)


    }
}
