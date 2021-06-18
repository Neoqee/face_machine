package com.neoqee.facemachine.baidu

import android.content.Context
import com.baidu.idl.main.facesdk.*
import com.baidu.idl.main.facesdk.model.BDFaceSDKCommon
import com.neoqee.facemachine.face.IFaceManager
import com.neoqee.facemachine.face.IFaceModelInitListener
import com.neoqee.facemachine.face.IFaceSDKInitListener
import com.neoqee.facemachine.face.OnFaceDetectCallback

object BdFaceManager : IFaceManager {

    // 人脸sdk鉴权
    private val faceAuth: FaceAuth = FaceAuth()
    // 人脸检测、识别等sdk相关功能类
    private lateinit var faceDetect: FaceDetect
    private lateinit var faceFeature: FaceFeature
    private lateinit var faceLive: FaceLive
    private lateinit var faceCrop: FaceCrop
    private lateinit var imageIllum: ImageIllum
    private lateinit var faceMouthMask: FaceMouthMask
    private lateinit var faceDetectNir: FaceDetect

    init {
        faceAuth.setActiveLog(BDFaceSDKCommon.BDFaceLogInfo.BDFACE_LOG_TYPE_ALL, 1)
        faceAuth.setCoreConfigure(BDFaceSDKCommon.BDFaceCoreRunMode.BDFACE_LITE_POWER_LOW, 2)
    }

    override fun initSdkLicense(context: Context, listener: IFaceSDKInitListener?) {
//        val licenseOnlineKey = "8FF4-9FXX-XWCV-RXRS"    // 手机
//        val licenseOnlineKey = "YR7B-6JJZ-DWDR-UUAZ"    // 平板
        val licenseOnlineKey = "JWST-JVR7-ZLVH-NFEH"    // armt ais906 平板
        faceAuth.initLicenseOnLine(context, licenseOnlineKey) { code, response ->
            if (code == 0) {
                listener?.onSuccess()
            } else {
                listener?.onFailure(code, response)
            }
        }
    }

    override fun initModel(context: Context, listener: IFaceModelInitListener?) {

    }

    override fun detectFace(
        rgbData: ByteArray?,
        nirData: ByteArray?,
        depthData: ByteArray?,
        srcWidth: Int,
        srcHeight: Int,
        detectCallback: OnFaceDetectCallback?
    ) {

    }
}