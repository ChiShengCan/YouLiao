package com.youyu.rollingnumber

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var  number:NumberAnimationTextView= NumberAnimationTextView(this)
        number.setTextSize(50F)
        number.setText("266")
        number.setTextColor(Color.parseColor("#3399fe"))

        content.addView(number)

        go.setOnClickListener {

            //动画执行的时间
            number.setDuration(10000)
            //开始滚动的数字，结束滚动的数字
            number.setNumber("266","67777")

        }




    }
}
