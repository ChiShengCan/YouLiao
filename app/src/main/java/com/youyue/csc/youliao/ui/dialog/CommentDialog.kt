package com.youyue.csc.youliao.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.utils.KeyboardUtils
import com.youyue.csc.youliao.utils.showToast

@SuppressLint("ValidFragment")
/**
 * Created by csc on 2018/4/21.
 * explain：评论弹出对话框
 */
class CommentDialog() : DialogFragment(), TextWatcher, View.OnClickListener {

    /*************发送内容回调监听*********************/
    interface SendListener{
        fun sendComment(text:String)
    }
    var mSendListener:SendListener?=null
    /*************发送内容回调监听*********************/

    //文本的提示文字
    var hintText:String?=null

    lateinit var edt_content:EditText
    lateinit var tv_send:TextView
    lateinit var iv_emoji:ImageView
    //评论的父布局
    lateinit var ll_comment:LinearLayout




    //构造器
    constructor(parcel: Parcel) : this() {
        hintText = parcel.readString()
    }


    @SuppressLint("ValidFragment")
    constructor(hintText:String, sendListener: SendListener) : this() {
        this.hintText=hintText
        this.mSendListener=sendListener

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       val dialog:Dialog= Dialog(activity,R.style.Comment_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var contentView:View= View.inflate(activity,R.layout.dialog_comment, null)

        edt_content=contentView.findViewById(R.id.edt_content) as EditText
        edt_content.setHint(hintText)//设置提示文本
        tv_send=contentView.findViewById(R.id.tv_send) as TextView
        iv_emoji=contentView.findViewById(R.id.iv_emoji) as ImageView
        ll_comment=contentView.findViewById(R.id.dialog_comment) as LinearLayout

        //添加布局
        dialog.setContentView(contentView)
        //外部点击取消
        dialog.setCanceledOnTouchOutside(true)

        //设置宽占满屏幕，布局在底部
        var window:Window=dialog.window
        var lp: WindowManager.LayoutParams=window.attributes
        lp.gravity= Gravity.BOTTOM  //底部
        lp.alpha=1.0f
        lp.dimAmount=0.0f
        lp.width=WindowManager.LayoutParams.MATCH_PARENT
        window.attributes=lp
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        //设置文本框文本改变监听
        edt_content.addTextChangedListener(this)
        //设置表情点击监听
        iv_emoji.setOnClickListener(this)
        //设置发送点击监听
        tv_send.setOnClickListener(this)

        //设置文本框获取焦点
        edt_content.isFocusable=true
        edt_content.isFocusableInTouchMode=true
        edt_content.requestFocus()

        //设置dialog关闭时，关闭软键盘
        dialog.setOnDismissListener {
            KeyboardUtils.hideKeyboard(activity,edt_content)
        }


        return dialog



    }

    //点击监听
    override fun onClick(v: View?) {
        when(v?.id){
            //发送
            R.id.tv_send ->{
              var context:String=edt_content.text.toString().trim()
                if (!TextUtils.isEmpty(context)){
                    if (mSendListener !=null) {
                        mSendListener?.sendComment(context)
                    }else{
                        Logger.e("SendListener还没有被实例化!")
                    }
                    dismiss()
                }else{
                    activity.showToast("评论内容不能为空!")
                    return
                }
            }

            //表情
            R.id.iv_emoji ->{
                activity.showToast("点击到了表情了啊!")
            }
        }

    }


    //文本框文本改变监听
    override fun afterTextChanged(s: Editable?) {
        if (s?.length!! > 0){
            tv_send.isEnabled=true
            tv_send.setTextColor(Color.BLACK)
        }else{
            tv_send.isEnabled=false
            tv_send.setTextColor(Color.GRAY)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
    //文本框文本改变监听




}
