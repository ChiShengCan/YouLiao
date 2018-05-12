package com.youyue.csc.youliao.ui.fragment.book


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.adapter.BookTitleAdapter


/**
 * A simple [Fragment] subclass.
 *
 */
class BookFragment : Fragment(){


    //左边的类别列表控件
    var rvSort:RecyclerView?=null
    var mLinearLayoutManager:LinearLayoutManager?=null
    var mSortAdapter:BookTitleAdapter?=null
    var mBookTitle:MutableList<String>?=null



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        var view=inflater?.inflate(R.layout.fragment_book, container, false)


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
            }

        })






        return view

    }







}
