package com.neoqee.facemachine.face

import android.content.Context
import com.neoqee.facemachine.baidu.BdFaceManager

/**
 * 人脸sdk大概需要的流程
 * 初始化鉴权
 * 初始化模型
 * */

interface IFaceSDKInitListener {
    fun onSuccess()
    fun onFailure(errorCode: Int, msg: String?)
}

interface IFaceModelInitListener {
    fun initModelSuccess()
    fun initModelFailure(errorCode: Int, msg: String?)
}

interface OnFaceDetectCallback {}

interface IFaceManager {
    fun initSdkLicense(context: Context, listener: IFaceSDKInitListener?)
    fun initModel(context: Context, listener: IFaceModelInitListener?)
    fun detectFace(
        rgbData: ByteArray?, nirData: ByteArray?, depthData: ByteArray?,
        srcWidth: Int, srcHeight: Int, detectCallback: OnFaceDetectCallback?
    )
    fun asyncFace(asyncList: FaceAsyncList)
}

object FaceManager : IFaceManager {

    private val mFaceManager: IFaceManager = BdFaceManager

    override fun initSdkLicense(context: Context, listener: IFaceSDKInitListener?) {
        mFaceManager.initSdkLicense(context, listener)
    }

    override fun initModel(context: Context, listener: IFaceModelInitListener?) {
        mFaceManager.initModel(context, listener)
    }

    override fun detectFace(
        rgbData: ByteArray?,
        nirData: ByteArray?,
        depthData: ByteArray?,
        srcWidth: Int,
        srcHeight: Int,
        detectCallback: OnFaceDetectCallback?
    ) {
        mFaceManager.detectFace(rgbData, nirData, depthData, srcWidth, srcHeight, detectCallback)
    }

    override fun asyncFace(asyncList: FaceAsyncList) {

    }

}