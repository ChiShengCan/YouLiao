package com.youyue.csc.youliao.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.orhanobut.logger.Logger
import com.youyue.csc.youliao.R
import com.youyue.csc.youliao.utils.DownloadApkUtils
import com.youyue.csc.youliao.utils.GetVersion
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    companion object {
        //获取下载APK对象
        lateinit var mDownloadApkUtils: DownloadApkUtils
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initView()

        //实例化下载APK对象
        mDownloadApkUtils= DownloadApkUtils(this,"http://app-global.pgyer.com/50037ec0d4ef437f90eac46743092857.apk?attname=%E6%B8%B8%E6%96%99.apk&sign=413dbc466547882956c492206886655a&t=5ad9bd1f")
    }

    override fun onResume() {
        super.onResume()
        if ( mDownloadApkUtils !=null){
            //获取下载进度的观察者和apk下载完成的监听
            mDownloadApkUtils.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mDownloadApkUtils !=null){
            mDownloadApkUtils.onPause()
        }
    }

    private fun initView() {
        //返回监听
        back_setting.setOnClickListener { finish() }

        //设置当前的版本号
        tv_version.text= GetVersion.getLocalVersionName(this)



        check_version.setOnClickListener{
            //开始下载APK
            mDownloadApkUtils.downloadApk("http://app-global.pgyer.com/50037ec0d4ef437f90eac46743092857.apk?attname=%E6%B8%B8%E6%96%99.apk&sign=413dbc466547882956c492206886655a&t=5ad9bd1f","游一圈","解决一些Bug")

            //设置APK的下载进度的监听
            mDownloadApkUtils.setUpdateListener(object:DownloadApkUtils.OnUpdateListener{
                override fun update(currentByte: Int, totalByte: Int) {
                    //这里还可以去做更新进度条


                    if ((currentByte  == totalByte)  && totalByte!=0){
                        Logger.e("APK下载完成!")

                    }
                }

            })



        }
    }


}
