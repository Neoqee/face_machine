package com.neoqee.facemachine.baidu

import android.util.Log
import com.baidu.idl.main.facesdk.model.Feature
import java.util.ArrayList
import java.util.concurrent.Executors
import java.util.concurrent.Future

object BdFeature {
    private val es = Executors.newSingleThreadExecutor()
    private var future: Future<*>? = null

    private var mUserNum: Int? = null
    private var isInitSuccess = false

    private val newEs = Executors.newFixedThreadPool(4)

    private val cacheFeatures = mutableMapOf<Int, Feature>()

    fun initFeature() {
        if (future != null && !future!!.isDone) {
            return
        }
        isInitSuccess = false
        future = es.submit {
            val features = ArrayList<Feature>()

        }

    }
}
