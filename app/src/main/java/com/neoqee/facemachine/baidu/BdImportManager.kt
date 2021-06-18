package com.neoqee.facemachine.baidu

import android.content.Context
import android.util.Log
import com.baidu.idl.main.facesdk.FaceCrop
import com.baidu.idl.main.facesdk.FaceDetect
import com.baidu.idl.main.facesdk.FaceFeature
import com.baidu.idl.main.facesdk.ImageIllum
import com.baidu.idl.main.facesdk.model.BDFaceInstance
import com.baidu.idl.main.facesdk.model.BDFaceSDKCommon
import com.baidu.idl.main.facesdk.model.BDFaceSDKConfig
import com.neoqee.facemachine.face.FaceAsyncData
import com.neoqee.facemachine.face.FaceAsyncList
import com.neoqee.facemachine.face.IFaceModelInitListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

object BdImportManager {

    // 人脸检测、识别等sdk相关功能类
    private lateinit var faceDetect: FaceDetect
    private lateinit var faceFeature: FaceFeature
    private lateinit var faceCrop: FaceCrop
    private lateinit var imageIllum: ImageIllum

    fun initModel(context: Context, listener: IFaceModelInitListener?){
// 默认检测
        val bdFaceInstance = BDFaceInstance()
        bdFaceInstance.creatInstance()
        faceDetect = FaceDetect(bdFaceInstance)
        // 默认识别
        faceFeature = FaceFeature()
        // 人脸抠图
        faceCrop = FaceCrop()
        // 曝光
        imageIllum = ImageIllum()

        initConfig()

        val startInitModelTime = System.currentTimeMillis()

        faceDetect.initModel(
            context,
            GlobalSet.DETECT_VIS_MODEL,
            GlobalSet.ALIGN_TRACK_MODEL,
            BDFaceSDKCommon.DetectType.DETECT_VIS,
            BDFaceSDKCommon.AlignType.BDFACE_ALIGN_TYPE_RGB_FAST
        ) { code, response ->
            Log.e("FaceSDKManager", "1 code = $code , msg = $response")
            if (code != 0 && listener != null) {
                listener.initModelFailure(code, response)
            }
        }

        faceDetect.initModel(
            context,
            GlobalSet.DETECT_VIS_MODEL,
            GlobalSet.ALIGN_RGB_MODEL, BDFaceSDKCommon.DetectType.DETECT_VIS,
            BDFaceSDKCommon.AlignType.BDFACE_ALIGN_TYPE_RGB_ACCURATE
        ) { code, response ->
            Log.e("FaceSDKManager", "2 code = $code , msg = $response")
            if (code != 0 && listener != null) {
                listener.initModelFailure(code, response)
            }
        }

        faceDetect.initQuality(
            context,
            GlobalSet.BLUR_MODEL,
            GlobalSet.OCCLUSION_MODEL
        ) { code, response ->
            Log.e("FaceSDKManager", "3 code = $code , msg = $response")
            if (code != 0 && listener != null) {
                listener.initModelFailure(code, response)
            }
        }

        faceCrop.initFaceCrop { code, response ->
            Log.e("FaceSDKManager", "4 code = $code , msg = $response")
            if (code != 0 && listener != null) {
                listener.initModelFailure(code, response)
            }
        }

        faceDetect.initAttrEmo(
            context,
            GlobalSet.ATTRIBUTE_MODEL,
            GlobalSet.EMOTION_MODEL
        ) { code, response ->
            Log.e("FaceSDKManager", "5 code = $code , msg = $response")
            if (code != 0 && listener != null) {
                listener.initModelFailure(code, response)
            }
        }

        faceDetect.initBestImage(
            context,
            GlobalSet.BEST_IMAGE
        ) { code, response ->
            Log.e("FaceSDKManager", "6 code = $code , msg = $response")
            if (code != 0 && listener != null) {
                listener.initModelFailure(code, response)
            }
        }

        // 初始化特征提取模型
        faceFeature.initModel(
            context,
            GlobalSet.RECOGNIZE_IDPHOTO_MODEL,
            GlobalSet.RECOGNIZE_VIS_MODEL,
            GlobalSet.RECOGNIZE_NIR_MODEL,
            GlobalSet.RECOGNIZE_RGBD_MODEL
        ) { code, response ->
            Log.e("FaceSDKManager", "7 code = $code , msg = $response")
            val endInitModelTime = System.currentTimeMillis()
            if (code != 0) {
                listener?.initModelFailure(code, response)
            } else {
                // 模型初始化成功，加载人脸数据
                listener?.initModelSuccess()
            }
        }
    }

    private fun initConfig() {
        val config = BDFaceSDKConfig()
        // 最小人脸个数检查，默认设置为1，可以根据需求调整
        config.maxDetectNum = 1
        // 默认为80px。可传入大于30px的数值，小于此大小的人脸不予检测，生效时间第一次加载模型
        config.minFaceSize = 80
        // 是否进行属性检测，默认关闭
        config.isAttribute = false
        // 模糊，遮挡，光照三个质量检测和姿态角查默认关闭，如果要开启，设置页启动
        config.isHeadPose = true
        config.isIllumination = config.isHeadPose
        config.isOcclusion = config.isIllumination
        config.isCheckBlur = config.isOcclusion

        faceDetect.loadConfig(config)
    }

    private var syncFuture: Future<*>? = null
    private val syncEs = Executors.newSingleThreadExecutor()
    fun asyncFace(asyncList: FaceAsyncList){
        syncFuture = syncEs.submit {
            val failInsert = asyncInsert(asyncList.insert)
            val failUpdate = asyncUpdate(asyncList.update)
            val failRemove = asyncDelete(asyncList.remove)
            val failAsyncList = FaceAsyncList(failInsert,failUpdate,failRemove)
        }
    }

    private fun asyncInsert(asyncDataList: List<FaceAsyncData>?): List<FaceAsyncData>{
        return emptyList()
    }
    private fun asyncUpdate(asyncDataList: List<FaceAsyncData>?): List<FaceAsyncData>{
        return emptyList()
    }
    private fun asyncDelete(asyncDataList: List<FaceAsyncData>?): List<FaceAsyncData>{
        return emptyList()
    }


}