package com.youyue.csc.youliao.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by csc on 2018/4/12.
 * explain：Fragment实现懒加载
 */
abstract class BaseFragment : Fragment(){
    //是否是第一次加载
    var isFrist : Boolean =false
    //布局
    var rootView : View ?=null
    //Fragment是否可见
    var isFragmentVisiable : Boolean =false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null){
            rootView = inflater?.inflate(getLayoutResources(),container,false)
        }
        return rootView

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            isFragmentVisiable=true
        }
        if (rootView == null){
            return
        }

        //由可见 到 不可见，表示这个Fragment已经加载过
        if (isFragmentVisiable){
            onFragmentVisiableChang(false)
            isFragmentVisiable=false
        }


    }

    /**
    * Kotlin中的类个方法默认都是final的，这样可以规避“脆弱基因”问题
    * 如果你想创建一个类的子类，需要使用open 关键字来修饰这个类，此外需要给每一个可以被重写的属性或方法添加一个open修饰符
    */
    open protected fun onFragmentVisiableChang(b : Boolean){

    }



    //获得布局
    abstract fun getLayoutResources() : Int

    //实例化View
    abstract fun initView()

}
