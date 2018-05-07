package com.youyue.csc.youliao.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.animation.LinearInterpolator
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.utils.KeyboardUtils
import com.youyue.csc.youliao.utils.showToast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var screenHeight:Int = 0
    var keyHeight:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /***
         * 登录页设计软键盘的弹起
         * 为了避免软键盘面板遮挡
         * 需要在清单文件的对应Activity中加入
         *android:windowSoftInputMode="stateVisible|adjustResize"
         */


        //获取屏幕的高度
        screenHeight=resources.displayMetrics.heightPixels
        //键盘弹起的高度为屏幕高度的1/3
        keyHeight=screenHeight/3



        initView()
    }

    private fun initView() {
        //设置账户文本改变监听
        edt_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone.getVisibility() == View.GONE) {
                    //设置清除账号图标显示
                    iv_clean_phone.setVisibility(View.VISIBLE)
                } else if (TextUtils.isEmpty(s)) {
                    //设置清除账号图标隐藏
                    iv_clean_phone.setVisibility(View.GONE)
                }
            }
        })

        //设置密码文本改变监听
        edt_password.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s) && clean_password.visibility == View.GONE){
                    clean_password.visibility=View.VISIBLE
                }else if (TextUtils.isEmpty(s)){
                    clean_password.visibility = View.GONE
                }

                if (s.toString().isEmpty()) return

                //匹配，只能输入字母和数字
                if (!s.toString().matches("[A-Za-z0-9]+".toRegex())) {
                   var temp:String=s.toString()
                    this@LoginActivity.showToast("请输入数字或者字母")
                    s?.delete(temp.length-1,temp.length)
                    edt_password.setSelection(s?.length!!)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        //设置显示密码点击监听
        iv_show_pwd.setOnClickListener {
            if (edt_password.inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                edt_password.inputType= InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                //改变图标
                iv_show_pwd.setImageResource(R.drawable.pwd_visiable)
            }else{
                edt_password.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                iv_show_pwd.setImageResource(R.drawable.pwd_gone)
            }

            var pwd:String = edt_password.text.toString()
            if (!TextUtils.isEmpty(pwd)){
                edt_password.setSelection(pwd?.length!!)
            }
        }

        //清理账号
        iv_clean_phone.setOnClickListener { edt_phone.setText("") }

        //清理密码
        clean_password.setOnClickListener { edt_password.setText("") }

        //设置登录监听
        btn_login.setOnClickListener {

            //先隐藏软键盘
            KeyboardUtils.hideSoftInput(this)

            if (TextUtils.isEmpty(edt_phone.text)){
                this.showToast("请输入正确的手机号!")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(edt_password.text)){
                this.showToast("请输入正确的手机号!")
                return@setOnClickListener
            }

            //结束当前页
            this.finish()

        }









        /**********************************下面主要实现的功能是动画****************************/
        //禁止键盘弹起的时候可以滚动
        scrollView.setOnTouchListener(View.OnTouchListener { v, event -> true })

        scrollView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
           //old表示改变之前的点
            //只要控件将Activity向上推的高度超过 1/3 的屏幕高度，就认为软键盘弹起
            if (oldBottom !=0 && bottom!=0 && (oldBottom - bottom > keyHeight)){

                var distance:Float =(content.bottom-bottom)*1.0F
                Logger.e("键盘弹起$distance")

                if (distance > 0){
                    //内容布局向上位移对应的距离
                    val mAnimatorTranslateContentY = ObjectAnimator.ofFloat(content, "translationY", 0.0f, -distance)
                    mAnimatorTranslateContentY.duration = 300
                    mAnimatorTranslateContentY.interpolator = LinearInterpolator()
                    mAnimatorTranslateContentY.start()


                    //Logo缩小动画0.6倍
                    logo.setPivotY(logo.getHeight().toFloat())
                    logo.setPivotX((logo.getWidth() / 2).toFloat())
                    val mAnimatorSet = AnimatorSet()
                    val mAnimatorTranslateY:ObjectAnimator = ObjectAnimator.ofFloat(logo, "translationY",0.0f, -distance)
                    val mAnimatorScaleX = ObjectAnimator.ofFloat(logo, "scaleX", 1.0f, 0.6f)
                    val mAnimatorScaleY = ObjectAnimator.ofFloat(logo, "scaleY", 1.0f, 0.6f)
                    mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX)
                    mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY)
                    mAnimatorSet.duration = 300
                    mAnimatorSet.start()

                }
                //设置联系客服和关于我们隐藏
                service.visibility= View.INVISIBLE

            }else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)){
                Logger.e("键盘收起"+(bottom-oldBottom))
                if ((content.getBottom() - oldBottom) > 0) {
                    var dis:Float=content.getTranslationY()
                    //内容布局向下移动
                    val mAnimatorTranslateContentY:ObjectAnimator = ObjectAnimator.ofFloat(content, "translationY",dis, 0.0f)
                    mAnimatorTranslateContentY.setDuration(300)
                    mAnimatorTranslateContentY.setInterpolator(LinearInterpolator())
                    mAnimatorTranslateContentY.start()

                    //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                    logo.setPivotY(logo.getHeight().toFloat())
                    logo.setPivotX((logo.getWidth() / 2).toFloat())
                    val mAnimatorSet = AnimatorSet()

                    val mAnimatorScaleX = ObjectAnimator.ofFloat(logo, "scaleX", 0.6f, 1.0f)
                    val mAnimatorScaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0.6f, 1.0f)
                    val mAnimatorTranslateY = ObjectAnimator.ofFloat(logo, "translationY", logo.getTranslationY(), 0.0f)

                    mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX)
                    mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY)
                    mAnimatorSet.duration = 300
                    mAnimatorSet.start()

                }
                service.setVisibility(View.VISIBLE)
            }
        }





    }
}

