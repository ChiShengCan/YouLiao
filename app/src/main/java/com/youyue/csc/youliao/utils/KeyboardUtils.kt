package com.youyue.csc.youliao.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Created by csc on 2018/4/17.
 * explain：关于软键盘的工具
 */
class KeyboardUtils {

    /**
     * 避免输入法面板遮挡
     * 在manifest.xml中activity中设置
     * android:windowSoftInputMode="stateVisible|adjustResize"
     */


    //静态方法
    companion object {

        /**
         * 动态隐藏软键盘
         *
         * @param activity activity
         */

        fun hideSoftInput(activity: Activity) {
            val view = activity.window.peekDecorView()
            if (view != null) {
                val inputmanger = activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputmanger.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }



        /**
         * 点击隐藏软键盘
         *
         * @param activity
         * @param view
         */
        fun hideKeyboard(activity: Activity, view: View) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * 动态隐藏软键盘
         *
         * @param context 上下文
         * @param edit    输入框
         */
        fun hideSoftInput(context: Context, edit: EditText) {
            edit.clearFocus()
            val inputmanger = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputmanger.hideSoftInputFromWindow(edit.windowToken, 0)
        }


        /**
         * 动态显示软键盘
         *
         * @param context 上下文
         * @param edit    输入框
         */
        fun showSoftInput(context: Context, edit: EditText) {
            edit.isFocusable = true
            edit.isFocusableInTouchMode = true
            edit.requestFocus()
            val inputManager = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(edit, 0)
        }

        /**
         * 切换键盘显示与否状态
         *
         * @param context 上下文
         * @param edit    输入框
         */
        fun toggleSoftInput(context: Context, edit: EditText) {
            edit.isFocusable = true
            edit.isFocusableInTouchMode = true
            edit.requestFocus()
            val inputManager = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

    }




}