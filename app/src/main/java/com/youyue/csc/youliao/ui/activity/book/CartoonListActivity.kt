package com.youyue.csc.youliao.ui.activity.book

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.adapter.CartoonListAdapter
import com.youyue.csc.youliao.base.CartoonListBean
import com.youyue.csc.youliao.mvp.contract.CartoonListContract
import com.youyue.csc.youliao.mvp.presenter.CartoonListPresenter
import kotlinx.android.synthetic.main.activity_cartoon_list.*

class CartoonListActivity : AppCompatActivity(), CartoonListContract.View {

    //漫画列表数据控制器
    var mPresenter: CartoonListPresenter? = null
    var map: HashMap<String, Any>? = null

    var mAdapter: CartoonListAdapter? = null
    var mLinearLayoutManager:LinearLayoutManager?=null

    //实体数据
    var mBean: CartoonListBean? = null
    //数据实体的集合
    lateinit var mLists:MutableList<CartoonListBean.ComicArr>

    var theme_id:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cartoon_list)

        mLists=ArrayList<CartoonListBean.ComicArr>()

        initRv()

        //获取页面传过来的数据
        theme_id= intent.getIntExtra("theme_id", -1)
        var pageidx = intent.getIntExtra("pageidx", 0)
        var theme_name=intent.getStringExtra("title")
        map = HashMap()
        map?.put("theme_id", theme_id)
        map?.put("pageidx", pageidx)



        //加载漫画列表数据
        mPresenter = CartoonListPresenter(this, this)
        mPresenter?.requestCartoonListData(theme_id)


        //设置还回点击监听
        back_cartoon_list.setOnClickListener {
            finish()
        }
        //设置标题
        title_cartoon_list.text=theme_name




    }

    private fun initRv() {
        mLinearLayoutManager= LinearLayoutManager(this)
        rv_cartoon_list.layoutManager = mLinearLayoutManager
        mAdapter = CartoonListAdapter(this, mBean?.comic_arr)
        rv_cartoon_list.adapter = mAdapter

        //给RecyclerView添加滚动监听,即到底部自动加载更多
        rv_cartoon_list.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                var layoutManager=recyclerView?.layoutManager as LinearLayoutManager
                var lastPosition=layoutManager.findLastVisibleItemPosition()
                if (newState  == RecyclerView.SCROLL_STATE_IDLE && lastPosition == (mLists.size)!! -1){

                    //加载更多的数据
                    mPresenter?.requestCartoonListData(theme_id)
                }

            }
        })


    }

    //设置漫画列表数据
    override fun setCartoonListData(bean: CartoonListBean) {
        bean.comic_arr?.forEach {
           mLists.add(it)
        }

        //更新数据
        mAdapter?.setData(mLists)


    }
}
