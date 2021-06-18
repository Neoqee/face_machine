package com.neoqee.facemachine.baidu

interface OnBdFaceDetectCallback {

    fun onFaceDetectDrawCallback(livenessModel: LivenessModel?)
    fun onFaceDetectCallback(livenessModel: LivenessModel?)

}