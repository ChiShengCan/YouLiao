package com.youyue.csc.youliao.ui.fragment.book


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.adapter.BookTitleAdapter
import com.youyue.csc.youliao.base.BookTypeBean
import com.youyue.csc.youliao.mvp.contract.BookContract
import com.youyue.csc.youliao.mvp.presenter.BookPresenter
import com.youyue.csc.youliao.ui.activity.BottomTabLayoutActivity


/**
 * A simple [Fragment] subclass.
 *
 */
class BookFragment : Fragment(),BookContract.View{


    //左边的类别列表控件
    var rvSort:RecyclerView?=null
    var mLinearLayoutManager:LinearLayoutManager?=null
    var mSortAdapter:BookTitleAdapter?=null
    var mBookTitle:MutableList<String>?=null

    //右边布局
    var mSortDetailFragment:BookSortDetailFragment?=null


    //控制器
    var mPresenter:BookPresenter?=null



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        var view=inflater?.inflate(R.layout.fragment_book, container, false)
        intLeftData(view)

        //获取图书分类的数据
        mPresenter= BookPresenter(context,this)
        mPresenter?.requestBookTypeData()


        return view

    }


    //实例化左边的数据
    private fun intLeftData(view: View?) {
        rvSort=view?.findViewById(R.id.rv_sort) as RecyclerView
        mLinearLayoutManager= LinearLayoutManager(context)
        rvSort?.layoutManager=mLinearLayoutManager

        //添加分类标题的数据
        mBookTitle= ArrayList<String>() as MutableList<String>?
        mBookTitle?.add("男生")
        mBookTitle?.add("女生")
        mBookTitle?.add("出版")
        mBookTitle?.add("漫画")
        mSortAdapter= BookTitleAdapter(context,mBookTitle)
        rvSort?.adapter=mSortAdapter
        //设置条目点击监听
        mSortAdapter?.setOnClickListener(object : BookTitleAdapter.ItemOnCkickListener{
            override fun onClickListener(position: Int) {
                //改变条目选中的状态
                mSortAdapter?.setCheckedPosition(position)

                //同时根据选中的图书类型，刷新右边的数据
                mSortDetailFragment?.setBookTypeFlag(position)

            }

        })

    }



    //获取到图书分类的数据
    override fun setBookTypeData(bean: BookTypeBean) {

        //因为没有漫画分类的详情接口，所以手动添加一下漫画的分类数据
        var cartoonList:MutableList<BookTypeBean.DataBean.CartoonBean> = ArrayList<BookTypeBean.DataBean.CartoonBean>()
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("全部",199984))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("恋爱",18854))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("搞笑",92645))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("魔幻",12734))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("动作",34337))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("科幻",10032))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("生活",10004))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("后宫",13554))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("治愈",9840))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("推理",32198))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("恐怖",28744))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("古风",6475))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("耽美百合",5812))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("少年",578853))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("少女",65433))
        cartoonList.add(BookTypeBean.DataBean.CartoonBean("校园",977553))


       bean.data.mCartoonList=cartoonList
        Logger.e("获取到的数据:"+bean.data.cartoon.get(2).getName())

        //获得数据成功以后实例化右边的布局
        var fragmentTransaction: FragmentTransaction =(activity as BottomTabLayoutActivity).supportFragmentManager.beginTransaction()
        mSortDetailFragment= BookSortDetailFragment()
        var bundle:Bundle= Bundle()
        bundle.putParcelable("bookBean",bean)
        mSortDetailFragment?.arguments=bundle
        fragmentTransaction.add(R.id.content_sort,mSortDetailFragment)
        fragmentTransaction.commit()


    }





}
