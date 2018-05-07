package com.youyue.csc.youliao.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.ui.fragment.HomeFragment
import com.youyue.csc.youliao.ui.fragment.HotFragment
import com.youyue.csc.youliao.ui.fragment.MineFragment
import com.youyue.csc.youliao.ui.fragment.book.BookFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var homeFragment: HomeFragment?=null
    var findFragment: BookFragment?=null
    var hotFragment: HotFragment?=null
    var mineFragment: MineFragment?=null

    //退出实现
    var mExitTime : Long =0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)



        //设置RadioButton来切换底部导航栏的显示切换
        setRadioButton()

        //实例化Fragment
        initFragment(savedInstanceState)
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState !=null){
            //如果不是第一次
            val mFragments : List<Fragment> = supportFragmentManager.fragments
            for (fragment in mFragments) {
                if (fragment is HomeFragment){
                    homeFragment=fragment
                }
                if (fragment is BookFragment){
                    findFragment=fragment
                }
                if (fragment is HotFragment){
                    hotFragment=fragment
                }
                if (fragment is MineFragment){
                    mineFragment=fragment
                }

            }
        }else{
            //实例化Fragment
            homeFragment= HomeFragment()
            findFragment= BookFragment()
            hotFragment= HotFragment()
            mineFragment= MineFragment()
            val fragmentTransaction=supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fl_content,homeFragment)
            fragmentTransaction.add(R.id.fl_content,findFragment)
            fragmentTransaction.add(R.id.fl_content,hotFragment)
            fragmentTransaction.add(R.id.fl_content,mineFragment)
            fragmentTransaction.commit()
        }

        //第一次显示首页
        supportFragmentManager.beginTransaction()
                .show(homeFragment)
                .hide(findFragment)
                .hide(hotFragment)
                .hide(mineFragment)
                .commit()

        tv_bar_title.text="游料"

    }


    private fun setRadioButton() {
        //设置首页默认选中
        rb_home.isChecked =true
        rb_home.setTextColor(resources.getColor(R.color.black))

        //设置各个RadioButton的点击监听
        rb_home.setOnClickListener(this)
        rb_find.setOnClickListener(this)
        rb_hot.setOnClickListener(this)
        rb_mine.setOnClickListener(this)
    }


    //点击监听
    override fun onClick(v: View?) {
        //点击RadioButton时先清除原来的状态
        clearState()

        //根据点击到的RadioButton来切换对应的Fragment
        when(v?.id){
            //首页
            R.id.rb_home ->{
                //显示toolbar
                toolbar.visibility=View.VISIBLE

                rb_home.isChecked=true
                rb_home.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction()
                        .show(homeFragment)
                        .hide(findFragment)
                        .hide(hotFragment)
                        .hide(mineFragment)
                        .commit()
                tv_bar_title.text="游料"
                tv_bar_title.visibility=View.VISIBLE
                iv_search.setImageResource(R.drawable.icon_search)
            }

            //发现页
            R.id.rb_find ->{
                //显示toolbar
                toolbar.visibility=View.VISIBLE

                rb_find.isChecked=true
                rb_find.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction()
                        .show(findFragment)
                        .hide(homeFragment)
                        .hide(hotFragment)
                        .hide(mineFragment)
                        .commit()
                tv_bar_title.text="发现"
                tv_bar_title.visibility=View.VISIBLE
                iv_search.setImageResource(R.drawable.icon_search)
            }

            //热门视频
            R.id.rb_hot ->{
                //显示toolbar
                toolbar.visibility=View.VISIBLE

                rb_hot.isChecked=true
                rb_hot.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction()
                        .show(hotFragment)
                        .hide(homeFragment)
                        .hide(findFragment)
                        .hide(mineFragment)
                        .commit()
                tv_bar_title.text="热门"
                tv_bar_title.visibility=View.VISIBLE
                iv_search.setImageResource(R.drawable.icon_search)

            }

            //我的
            R.id.rb_mine ->{
                //隐藏toolbar
                toolbar.visibility=View.GONE

                rb_mine.isChecked=true
                rb_mine.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction()
                        .show(mineFragment)
                        .hide(homeFragment)
                        .hide(findFragment)
                        .hide(hotFragment)
                        .commit()

                tv_bar_title.visibility=View.VISIBLE
                iv_search.setImageResource(R.drawable.icon_search)


            }
        }



    }

    private fun clearState(){
        //RadioGroup先清除选中的状态
        rg_root.clearCheck()
        //设置各个颜色不是选中的颜色
        rb_home.setTextColor(resources.getColor(R.color.gray))
        rb_find.setTextColor(resources.getColor(R.color.gray))
        rb_hot.setTextColor(resources.getColor(R.color.gray))
        rb_mine.setTextColor(resources.getColor(R.color.gray))


    }


    //为了切换日夜间模式的时候，会切换到首页而专门提供的
    public fun setHomeChecked(){
        clearState()

        toolbar.visibility=View.VISIBLE
        rb_home.isChecked=true
        rb_home.setTextColor(resources.getColor(R.color.black))

    }






}
