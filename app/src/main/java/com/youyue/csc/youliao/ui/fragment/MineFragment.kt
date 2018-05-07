package com.youyue.csc.youliao.ui.fragment


import android.content.Intent
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.base.BaseFragment
import com.youyue.csc.youliao.mvp.contract.MineContract
import com.youyue.csc.youliao.ui.activity.BottomTabLayoutActivity
import com.youyue.csc.youliao.ui.activity.LoginActivity
import com.youyue.csc.youliao.ui.activity.SettingActivity
import com.youyue.csc.youliao.ui.activity.VideoCacheActivity
import com.youyue.csc.youliao.utils.SPUtils
import com.youyue.csc.youliao.utils.newIntent
import kotlinx.android.synthetic.main.fragment_mine.*


class MineFragment : BaseFragment(), MineContract.View {

    //获取我页面的红点小圆圈
    lateinit var redCount: TextView

    //实例化跟布局
    override fun getLayoutResources(): Int {
        return R.layout.fragment_mine
    }


    override fun initView() {
        //获取我的页面的红点小圆圈(如果没有小红点，请不要设置)
        var view: View = (activity as BottomTabLayoutActivity).mTabLayout.getTabAt(3)?.customView!!
        redCount=view.findViewById(R.id.tab_red_count) as TextView
        //设置小圆圈的数字
        redCount.setText("4")


        //跳转到登录
        rl_second.setOnClickListener {
            var intent=Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }

        //日夜间模式的切换
        choose_day_or_night.setOnClickListener {

            //保存共享参数
            var themeFlag:Int=SPUtils.getInstance(context,"app_theme").getInt("theme")
            Logger.e("当前的模式:"+themeFlag)
            if (themeFlag==-1){
                //表示当前为白天模式，去切换夜间模式
                SPUtils.getInstance(context,"app_theme").put("theme",1)


            }else if (themeFlag==1){
                //表示当前为夜间模式，去切换白天模式
                SPUtils.getInstance(context,"app_theme").put("theme",-1)


            }



            //去切换主题(需要更改一下Fragment的父控件的背景颜色)
           (activity as BottomTabLayoutActivity).changeTheme()

            //更新本界面的配色
            var background: TypedValue = TypedValue()//背景色
            var theme: Resources.Theme = activity.theme
            theme.resolveAttribute(R.attr.colorBackground,background,true)
            mine_background.setBackgroundResource(background.resourceId)





        }

        //跳转到缓存视频页
        video_cache.setOnClickListener {
            activity.newIntent<VideoCacheActivity>()
        }

        //跳转到设置页
        rl_setting.setOnClickListener {
            activity.newIntent<SettingActivity>()
        }




    }


}
