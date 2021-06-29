package com.neoqee.facemachine.baidu

import com.neoqee.facemachine.face.OnFaceDetectCallback

interface OnBdFaceDetectCallback: OnFaceDetectCallback {

    fun onFaceDetectDrawCallback(livenessModel: LivenessModel?)
    fun onFaceDetectCallback(livenessModel: LivenessModel?)

}