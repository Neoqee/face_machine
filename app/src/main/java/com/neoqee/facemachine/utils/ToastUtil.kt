package com.neoqee.facemachine.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class ToastUtil {

    companion object {
        private val handler = Handler(Looper.getMainLooper())
        private var mContext: Context? = null

        fun toast(text: String) {
            mContext.let {
                handler.post {
                    Toast.makeText(it, text, Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun toast(resId: Int) {
            mContext.let {
                handler.post {
                    Toast.makeText(it, resId, Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun init(context: Context) {
            mContext = context
        }
    }

}