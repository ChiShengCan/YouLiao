package com.youyue.csc.youliao.ui.fragment




import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.adapter.HomeAdapter
import com.youyue.csc.youliao.adapter.ScrollIndicatorAdapter
import com.youyue.csc.youliao.base.BaseApp
import com.youyue.csc.youliao.base.BaseFragment
import com.youyue.csc.youliao.bean.HomeBean
import com.youyue.csc.youliao.mvp.contract.HomeContract
import com.youyue.csc.youliao.mvp.presenter.HomePresenter
import com.youyue.csc.youliao.ui.activity.BottomTabLayoutActivity
import com.youyue.csc.youliao.widget.indicator.slidebar.ColorBar
import com.youyue.csc.youliao.widget.indicator.transition.OnTransitionTextListener
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : BaseFragment(), HomeContract.View{

    var mIsRefresh : Boolean = false
    var mPresenter : HomePresenter?=null
    //数据集合
    var mList=ArrayList<HomeBean.IssueListBean.ItemListBean>()

    //Rv适配器
    var mAdapter: HomeAdapter?=null

    //指示器的数据
    var titles:List<String> = listOf("动画","广告","音乐","生活","剧情","创意","记录","萌宠","锦集","运动","综艺","搞笑","旅行","预告")


    //获得加载更多的标识
    var data:String?=null


    //获取首页的红点小圆圈
    lateinit var redCount: TextView


    companion object {
        fun newInstance():HomeFragment{
            var homeFragment:HomeFragment= HomeFragment()

            return homeFragment
        }
    }





    //设置布局
    override fun getLayoutResources(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {

        //获取首页的红点小圆圈(如果没有小红点，请不要设置)
        var view: View = (activity as BottomTabLayoutActivity).mTabLayout.getTabAt(0)?.customView!!
        redCount=view.findViewById(R.id.tab_red_count) as TextView
        //设置小圆圈的数字
        redCount.setText("15")


        //设置指示器
        //设置选中和未选中文字的颜色
        indicator.setOnTransitionListener(OnTransitionTextListener().setColor(resources.getColor(R.color.buttonSelectColor), Color.BLACK).setSize(16.0F, 12.0F))
        //设置指示器的颜色、宽度、高度
        indicator.setScrollBar(ColorBar(BaseApp.getInstance(),resources.getColor(R.color.buttonSelectColor),100,1))
        //设置默认的选中条目
        indicator.setCurrentItem(0,true)
        //给指示器设置数据 ScrollIndicatorAdapter
        indicator.setAdapter(ScrollIndicatorAdapter(titles))

        /***************设置条目选中监听********************/
        indicator.setOnItemSelectListener { selectItemView, select, preSelect ->
            Logger.e("当前的data数据为:"+data)
            mPresenter?.requestHomeMoreData(context,data!!)
        }



        //实例化Presenter
        mPresenter = HomePresenter(context,this)
        //加载数据
        mPresenter?.requestHomeData()

        //设置RecyclerView
        recyclerView.layoutManager= LinearLayoutManager(context)
        mAdapter= HomeAdapter(context,mList)
        recyclerView.adapter=mAdapter

        //设置下拉刷新监听
        refreshLayout.setOnRefreshListener {

            //设置首页的小红点
            redCount.visibility=View.GONE


            if (!mIsRefresh){
                mIsRefresh = true
                mPresenter?.requestHomeData()
            }
        }


    }

    //给适配器设置数据
    override fun setHomeData(bean: HomeBean) {
        //获得加载更多的标识
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(bean?.nextPageUrl)
        data = m.replaceAll("").subSequence(1, m.replaceAll("").length - 1).toString()


        if (mIsRefresh){
            mIsRefresh = false
            refreshLayout.isRefreshing=false
            if (mList.size > 0){
                mList.clear()
            }
        }

        //给集合添加数据
        bean.issueList!!
                .flatMap { it.itemList!! }
                .filter { it.type.equals("video") }
                .forEach { mList.add(it) }

        //刷新适配器
        mAdapter?.notifyDataSetChanged()



    }


    override fun setIndicatorHomeData(bean: HomeBean) {
        //获得加载更多的标识
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(bean?.nextPageUrl)
        data = m.replaceAll("").subSequence(1, m.replaceAll("").length - 1).toString()


        mList.clear()
        bean.issueList!!
                .flatMap { it.itemList!! }
                .filter { it.type.equals("video") }
                .forEach { mList.add(it) }

        //刷新适配器
        mAdapter?.notifyDataSetChanged()




    }




}
