package com.youyue.csc.youliao.ui.activity.book

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.base.CartoonListBean
import com.youyue.csc.youliao.mvp.contract.CartoonListContract
import com.youyue.csc.youliao.mvp.presenter.CartoonListPresenter

class CartoonListActivity : AppCompatActivity(),CartoonListContract.View{

    //漫画列表数据控制器
    var mPresenter:CartoonListPresenter?=null
    var map:HashMap<String,Any>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cartoon_list)

        //获取页面传过来的数据
        var theme_id=intent.getIntExtra("theme_id",-1)
        var pageidx=intent.getIntExtra("pageidx",0)
        map= HashMap()
        map?.put("theme_id",theme_id)
        map?.put("pageidx",pageidx)


        //加载漫画列表数据
        mPresenter=CartoonListPresenter(this,this)
        mPresenter?.requestCartoonListData(map)

    }

    //设置漫画列表数据
    override fun setCartoonListData(bean: CartoonListBean) {
        Logger.e("获取到的数据:"+bean.toString())
    }
}
