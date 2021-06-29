package com.neoqee.facemachine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.neoqee.facemachine.face.FaceManager
import com.neoqee.facemachine.face.IFaceSDKInitListener
import com.neoqee.facemachine.utils.ToastUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var flag = false
        FaceManager.initSdkLicense(this, object : IFaceSDKInitListener {
            override fun onSuccess() {
                flag = true
            }

            override fun onFailure(errorCode: Int, msg: String?) {
                flag = false
            }
        })
        runBlocking {
            delay(2000)
            if (flag){
                Intent(this@SplashActivity,MainActivity::class.java).apply {
                    startActivity(this)
                }
            }else{
                ToastUtil.toast("Baidu AI SDK init failure!")
            }
        }
    }
}