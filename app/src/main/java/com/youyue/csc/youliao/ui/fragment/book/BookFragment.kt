package com.youyue.csc.youliao.ui.fragment.book


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.adapter.BookTabLayoutAdapter


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


        return view

    }







}
