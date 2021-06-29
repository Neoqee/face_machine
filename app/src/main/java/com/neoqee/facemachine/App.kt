package com.neoqee.facemachine

import android.app.Application
import com.neoqee.facemachine.utils.ToastUtil

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initUtil()
    }

    private fun initUtil() {
        ToastUtil.init(this)
    }

}