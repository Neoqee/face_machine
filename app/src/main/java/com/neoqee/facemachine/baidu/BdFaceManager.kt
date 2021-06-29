package com.neoqee.facemachine.baidu

import android.content.Context
import android.util.Log
import com.baidu.idl.main.facesdk.*
import com.baidu.idl.main.facesdk.model.*
import com.neoqee.facemachine.face.*
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

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
        // initLicenseOffLine  离线授权
        // initLicenseBatchLine 在线按应用批量授权
        faceAuth.initLicenseOnLine(context, licenseOnlineKey) { code, response ->
            // code 0 : 成功；code 1 加载失败 response 结果信息
            if (code == 0) {
                listener?.onSuccess()
            } else {
                listener?.onFailure(code, response)
            }
        }
    }

    /**
     * code == 0 成功； code == 1 context 为null； code == -1 非法的参数；
     * code == -2 内存分配失败； code == -3 实例对象为空；
     * code == -4 模型内容为空；code == -5 不支持的能力类型；
     * code == -6 不支持预测类型；code == -7 预测库对象创建失败；
     * code == -8 预测库初始化失败；code == -9 图像数据为空；
     * code == -10 人脸能力初始化失败；code == -11 能力未加载 ；
     * code == -12 人脸能力已加载；code == -13 未授权；
     * code == -14 人脸能力运行异常 ；code == -15 不支持的图像类型；
     * code == -16 图像转换失败；
     * */
    override fun initModel(context: Context, listener: IFaceModelInitListener?) {
        // 检测接口
        val bdFaceInstance = BDFaceInstance()
        bdFaceInstance.creatInstance()
        faceDetect = FaceDetect(bdFaceInstance)

        val bdFaceInstanceNir = BDFaceInstance()
        bdFaceInstanceNir.creatInstance()
        faceDetectNir = FaceDetect(bdFaceInstanceNir)

        // 活体接口
        faceLive = FaceLive()

        // 特征抽取接口
        faceFeature = FaceFeature()

        // 抠图能力接口
        faceCrop = FaceCrop()

        // 口罩检测接口
        faceMouthMask = FaceMouthMask()

        // 图片光照检测接口
        imageIllum = ImageIllum()

        // 1.2.7 配置信息加载
        // 说明：检测最小人脸，是否开启内部质量检测，检测或者追踪时间间隔等配置（只适用于3.2及以下版本的detect、track接口）
        val bdFaceSDKConfig = BDFaceSDKConfig().apply {
            maxDetectNum = 1
            minFaceSize = 80
            isAttribute = false
            isHeadPose = true
            isIllumination = true
            isOcclusion = true
            isCheckBlur = true
        }
        faceDetect.loadConfig(bdFaceSDKConfig)

        // 1.2.1 可见光检测对齐模型加载
        // 说明：检测对齐模型加载，支持可见光模型
//        faceDetect.initModel(
//            context,
//            GlobalSet.DETECT_VIS_MODEL,
//            GlobalSet.DETECT_NIR_MODE,
//            GlobalSet.ALIGN_TRACK_MODEL
//        ){ code, msg ->
//
//        }

        // 1.2.2 检测对齐模型加载
        // 说明：检测对齐模型加载，支持可见光、近红外检测模型
        faceDetect.initModel(
            context,
            GlobalSet.DETECT_VIS_MODEL,
            GlobalSet.ALIGN_TRACK_MODEL,
            BDFaceSDKCommon.DetectType.DETECT_VIS,
            BDFaceSDKCommon.AlignType.BDFACE_ALIGN_TYPE_RGB_FAST
        ) { code, msg ->
            callbackHandle(code, msg, listener)
        }
        faceDetect.initModel(
            context,
            GlobalSet.DETECT_VIS_MODEL,
            GlobalSet.ALIGN_RGB_MODEL,
            BDFaceSDKCommon.DetectType.DETECT_VIS,
            BDFaceSDKCommon.AlignType.BDFACE_ALIGN_TYPE_RGB_ACCURATE
        ) { code, msg ->
            callbackHandle(code, msg, listener)
        }
        // 1.2.3 质量检测模型加载
        // 说明：质量检测模型加载，判断人脸遮挡信息，光照信息，模糊信息，模型包含模糊模型，遮挡信息，作用于质量检测接口
        faceDetect.initQuality(
            context,
            GlobalSet.BLUR_MODEL,
            GlobalSet.OCCLUSION_MODEL
        ) { code, msg ->
            callbackHandle(code, msg, listener)
        }
        // 1.2.4 属性情绪模型加载
        // 说明：人脸属性（年龄，性别，戴眼镜等），情绪（喜怒哀乐）模型初始化
        faceDetect.initAttrEmo(
            context,
            GlobalSet.ATTRIBUTE_MODEL,
            GlobalSet.EMOTION_MODEL
        ) { code, msg ->
            callbackHandle(code, msg, listener)
        }
        // 1.2.5 眼睛闭合，嘴巴闭合模型加载
        // 说明：人脸眼睛闭合，嘴巴闭合模型初始化
        // faceDetect.initFaceClose

        // 1.2.6 最优人脸模型加载
        // 说明：最优人脸模型初始化
        faceDetect.initBestImage(
            context,
            GlobalSet.BEST_IMAGE
        ) { code, msg ->
            callbackHandle(code, msg, listener)
        }


        faceDetectNir.initModel(
            context,
            GlobalSet.DETECT_NIR_MODE,
            GlobalSet.ALIGN_NIR_MODEL,
            BDFaceSDKCommon.DetectType.DETECT_NIR,
            BDFaceSDKCommon.AlignType.BDFACE_ALIGN_TYPE_NIR_ACCURATE
        ) { code, msg ->
            callbackHandle(code, msg, listener)
        }


        // 1.3.1 活体模型加载
        // 说明：静默活体检测模型初始化，可见光活体模型，深度活体，近红外活体模型初始化
        faceLive.initModel(
            context,
            GlobalSet.LIVE_VIS_MODEL,
            GlobalSet.LIVE_DEPTH_MODEL,
            GlobalSet.LIVE_NIR_MODEL
        ) { code, msg ->
            callbackHandle(code, msg, listener)
        }


        // 1.4.1 特征模型加载
        // 说明：离线特征获取模型加载，目前支持可见光模型，近红外检测模型（非必要参数，可以为空），证件照模型；
        // 用户根据自己场景，选择相应场景模型
        // 4.0及以上版本
        faceFeature.initModel(
            context,
            GlobalSet.RECOGNIZE_IDPHOTO_MODEL,
            GlobalSet.RECOGNIZE_VIS_MODEL,
            GlobalSet.RECOGNIZE_NIR_MODEL,
            GlobalSet.RECOGNIZE_RGBD_MODEL
        ) { code, msg ->
            callbackHandle(code, msg, listener)
        }


        // 1.8.1 initFaceCrop抠图能力加载
        faceCrop.initFaceCrop { code, msg ->
            callbackHandle(code, msg, listener)
        }


        // 1.9.1 口罩检测模型加载
        // 说明：加载口罩检测模型
        faceMouthMask.initModel(
            context,
            GlobalSet.MOUTH_MASK
        ) { code, msg ->
            callbackHandle(code, msg, listener)
        }



        listener?.initModelSuccess()
    }

    private fun callbackHandle(code: Int, msg: String, listener: IFaceModelInitListener?) {
        Log.e("Neoqee","code = $code msg = $msg")
        if (code != 0) {
            listener?.initModelFailure(code, msg)
        }
    }

    override fun detectFace(
        rgbData: ByteArray?,
        nirData: ByteArray?,
        depthData: ByteArray?,
        srcWidth: Int,
        srcHeight: Int,
        detectCallback: OnFaceDetectCallback?
    ) {
        onFastDetect(
            rgbData,
            nirData,
            depthData,
            srcWidth,
            srcHeight,
            detectCallback as? OnBdFaceDetectCallback
        )
    }

    private val asCoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private var job1: Deferred<*>? = null
    private fun onFastDetect(
        rgbData: ByteArray?,
        nirData: ByteArray?,
        depthData: ByteArray?,
        srcWidth: Int,
        srcHeight: Int,
        callback: OnBdFaceDetectCallback?
    ) {
        if (job1 != null && !job1!!.isCompleted) {
            return
        }
        GlobalScope.launch {
            job1 = GlobalScope.async(asCoroutineDispatcher) {
                val startTime = System.currentTimeMillis()
                // 创建检测结果存储数据
                val livenessModel = LivenessModel()
                val rgbInstance = BDFaceImageInstance(
                    rgbData, srcHeight, srcWidth,
                    BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_YUV_NV21,
                    SingleBaseConfig.getBaseConfig().detectDirection.toFloat(),
                    SingleBaseConfig.getBaseConfig().mirrorRGB
                )

                // getImage() 获取送检图片,如果检测数据有问题，可以通过image view 展示送检图片
                livenessModel.bdFaceImageInstance = rgbInstance.image

                // 检查函数调用，返回检测结果
                val startDetectTime = System.currentTimeMillis()

                // 快速检测获取人脸信息，仅用于绘制人脸框，详细人脸数据后续获取
                val faceInfos: Array<FaceInfo>? = faceDetect.track(
                    BDFaceSDKCommon.DetectType.DETECT_VIS,
                    BDFaceSDKCommon.AlignType.BDFACE_ALIGN_TYPE_RGB_FAST,
                    rgbInstance
                )
                livenessModel.rgbFastDuration = System.currentTimeMillis() - startDetectTime

                // 检测结果判断
                if (faceInfos != null && faceInfos.isNotEmpty()) {
                    livenessModel.trackFaceInfo = faceInfos
                    livenessModel.faceInfo = faceInfos[0]
                    livenessModel.trackStatus = 1
                    livenessModel.landmarks = faceInfos[0].landmarks
                    Log.e("Neoqee","fast detect success")
                    // 返回追踪到的人脸  绘制人脸框
                    callback?.run {
                        onFaceDetectDrawCallback(livenessModel)
                    }

                    onAccurateDetect(
                        rgbInstance,
                        rgbData,
                        nirData,
                        depthData,
                        srcWidth,
                        srcHeight,
                        livenessModel,
                        faceInfos,
                        startTime,
                        callback
                    )
                } else {
                    // 流程结束 销毁图片，开始下一帧图片检测，防止内存泄漏
                    rgbInstance.destory()
                    callback?.run {
                        onFaceDetectCallback(null)
                        onFaceDetectDrawCallback(null)
                    }
                }
            }.apply { start() }
        }
    }

    private val asCoroutineDispatcher2 = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private var job2: Deferred<*>? = null
    private fun onAccurateDetect(
        rgbInstance: BDFaceImageInstance,
        rgbData: ByteArray?,
        nirData: ByteArray?,
        depthData: ByteArray?,
        srcWidth: Int,
        srcHeight: Int,
        livenessModel: LivenessModel,
        fastFaceInfos: Array<FaceInfo>,
        startTime: Long,
        callback: OnBdFaceDetectCallback?
    ) {
        if (job2 != null && !job2!!.isCompleted) {
            rgbInstance.destory()
            return
        }
        GlobalScope.launch {
            job2 = GlobalScope.async(asCoroutineDispatcher2) {
                val faceInfos = rgbAccurateDetect(rgbInstance, fastFaceInfos, livenessModel)
                // 重新赋予详细人脸信息
                if (faceInfos != null && faceInfos.isNotEmpty()) {
                    livenessModel.faceInfo = faceInfos[0]
                    livenessModel.trackStatus = 2
                    livenessModel.landmarks = faceInfos[0].landmarks
                } else {
                    rgbInstance.destory()
                    callback?.onFaceDetectCallback(livenessModel)
                    return@async
                }

                mouthMaskCheck(rgbInstance, faceInfos, livenessModel)

                val qualityCheck: Boolean
                livenessModel.qualityCheckDuration = measureTimeMillis {
                    qualityCheck = onQualityCheck(livenessModel)
                }
                if (!qualityCheck) {
                    rgbInstance.destory()
                    callback?.onFaceDetectCallback(livenessModel)
                    return@async
                }

                rgbLiveCheck(rgbInstance, livenessModel)
                val nirInstance = nirLiveCheck(nirData, srcWidth, srcHeight, livenessModel)
                featureCheck(rgbInstance, livenessModel)

                // 流程结束，记录最终时间
                livenessModel.allDetectDuration = System.currentTimeMillis() - startTime
                // 流程结束销毁图片，开始下一帧图片检测，防止内存泄漏
                rgbInstance.destory()
                nirInstance.destory()
                // 显示最终结果提示
                callback?.onFaceDetectCallback(livenessModel)
                return@async
            }.apply { start() }
        }
    }

    private fun rgbAccurateDetect(
        rgbInstance: BDFaceImageInstance,
        fastFaceInfos: Array<FaceInfo>,
        livenessModel: LivenessModel
    ): Array<FaceInfo>? {
        val bdFaceDetectListConfig = BDFaceDetectListConf().apply {
            with(SingleBaseConfig.getBaseConfig().isQualityControl) {
                usingQuality = this
                usingHeadPose = this
            }
        }
        var faceInfos: Array<FaceInfo>?
        livenessModel.rgbAccurateDuration = measureTimeMillis {
            faceInfos = faceDetect.detect(
                BDFaceSDKCommon.DetectType.DETECT_VIS,
                BDFaceSDKCommon.AlignType.BDFACE_ALIGN_TYPE_RGB_ACCURATE,
                rgbInstance,
                fastFaceInfos,
                bdFaceDetectListConfig
            )
        }
        return faceInfos
    }

    private fun mouthMaskCheck(
        rgbInstance: BDFaceImageInstance,
        faceInfos: Array<FaceInfo>,
        livenessModel: LivenessModel
    ) {
        livenessModel.maskCheckDuration = measureTimeMillis {
            faceMouthMask.checkMask(rgbInstance, faceInfos)?.let {
                livenessModel.maskScore = it[0]
            }
        }
    }

    private fun onQualityCheck(livenessModel: LivenessModel): Boolean {
        if (!SingleBaseConfig.getBaseConfig().isQualityControl) {
            return true
        }
        if (livenessModel.maskScore > 0.9) {
            return true
        }
        if (livenessModel.faceInfo != null) {
            // 角度过滤
            if (Math.abs(livenessModel.faceInfo!!.yaw) > SingleBaseConfig.getBaseConfig().yaw) {
                return false
            } else if (Math.abs(livenessModel.faceInfo!!.roll) > SingleBaseConfig.getBaseConfig().roll
            ) {
                return false
            } else if (Math.abs(livenessModel.faceInfo!!.pitch) > SingleBaseConfig.getBaseConfig().pitch
            ) {
                return false
            }

            // 模糊结果过滤
            val blur: Float = livenessModel.faceInfo!!.bluriness
            if (blur > SingleBaseConfig.getBaseConfig().blur) {
                return false
            }

            // 光照结果过滤
            val illum: Float = livenessModel.faceInfo!!.illum.toFloat()
            if (illum < SingleBaseConfig.getBaseConfig().illumination) {
                return false
            }

            // 遮挡结果过滤
            if (livenessModel.faceInfo!!.occlusion != null) {
                val occlusion: BDFaceOcclusion = livenessModel.faceInfo!!.occlusion
                if (occlusion.leftEye > SingleBaseConfig.getBaseConfig().leftEye) {
                    // 左眼遮挡置信度
                } else if (occlusion.rightEye > SingleBaseConfig.getBaseConfig().rightEye) {
                    // 右眼遮挡置信度
                } else if (occlusion.nose > SingleBaseConfig.getBaseConfig().nose) {
                    // 鼻子遮挡置信度
                } else if (occlusion.mouth > SingleBaseConfig.getBaseConfig().mouth) {
                    // 嘴巴遮挡置信度
                } else if (occlusion.leftCheek > SingleBaseConfig.getBaseConfig().leftCheek) {
                    // 左脸遮挡置信度
                } else if (occlusion.rightCheek > SingleBaseConfig.getBaseConfig().rightCheek
                ) {
                    // 右脸遮挡置信度
                } else if (occlusion.chin > SingleBaseConfig.getBaseConfig().chinContour) {
                    // 下巴遮挡置信度
                } else {
                    return true
                }
            }
        }
        return false
    }

    private fun rgbLiveCheck(rgbInstance: BDFaceImageInstance, livenessModel: LivenessModel) {
        measureTimeMillis {
            if (SingleBaseConfig.getBaseConfig().type != 0) {
                livenessModel.rgbLivenessScore = faceLive.silentLive(
                    BDFaceSDKCommon.LiveType.BDFACE_SILENT_LIVE_TYPE_RGB,
                    rgbInstance,
                    livenessModel.faceInfo!!.landmarks
                )
            }
        }.let {
            livenessModel.rgbLivenessDuration = it
        }
    }

    private fun nirLiveCheck(
        nirData: ByteArray?,
        srcWidth: Int,
        srcHeight: Int,
        livenessModel: LivenessModel
    ): BDFaceImageInstance {
        var faceInfosIr: Array<FaceInfo>?
        return BDFaceImageInstance(
            nirData, srcHeight, srcWidth,
            BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_YUV_NV21,
            90F,
            SingleBaseConfig.getBaseConfig().mirrorNIR
        ).also { nirInstance ->
            livenessModel.irDetectDuration = measureTimeMillis {
                val bdFaceDetectListConf = BDFaceDetectListConf()
                bdFaceDetectListConf.usingDetect = true
                faceInfosIr = faceDetectNir.detect(
                    BDFaceSDKCommon.DetectType.DETECT_NIR,
                    BDFaceSDKCommon.AlignType.BDFACE_ALIGN_TYPE_NIR_ACCURATE,
                    nirInstance, null, bdFaceDetectListConf
                )
                bdFaceDetectListConf.usingDetect = false
            }
            livenessModel.irLivenessDuration = measureTimeMillis {
                if (faceInfosIr != null && faceInfosIr!!.isNotEmpty()) {
                    val faceInfoIr = faceInfosIr!![0]
                    livenessModel.irLivenessScore = faceLive.silentLive(
                        BDFaceSDKCommon.LiveType.BDFACE_SILENT_LIVE_TYPE_NIR,
                        nirInstance, faceInfoIr.landmarks
                    )
                }
            }
        }

    }

    private fun featureCheck(
        rgbInstance: BDFaceImageInstance,
        livenessModel: LivenessModel
    ) {
        val featureType = SingleBaseConfig.getBaseConfig().activeModel
        val feature = ByteArray(512)
        when (featureType) {
            3 -> {
            }
            2 -> {
            }
            else -> {
                livenessModel.featureDuration = measureTimeMillis {
                    livenessModel.featureCode = faceFeature.feature(
                        BDFaceSDKCommon.FeatureType.BDFACE_FEATURE_TYPE_LIVE_PHOTO,
                        rgbInstance,
                        livenessModel.faceInfo!!.landmarks,
                        feature
                    )
                }
                livenessModel.feature = feature
                featureSearch(livenessModel)
            }
        }
    }

    private fun featureSearch(livenessModel: LivenessModel) {
        val featureCheckMode = 3
        val type = BDFaceSDKCommon.FeatureType.BDFACE_FEATURE_TYPE_LIVE_PHOTO
        if (featureCheckMode == 2) {
            return
        }
        livenessModel.checkDuration = measureTimeMillis {
            if (livenessModel.featureCode == (GlobalSet.FEATURE_SIZE / 4).toFloat()) {
                val featureResult: ArrayList<Feature>?
                livenessModel.featureSearchDuration = measureTimeMillis {
                    featureResult = faceFeature.featureSearch(livenessModel.feature, type, 1, true)
                }

                if (!featureResult.isNullOrEmpty()) {
                    val topFeature = featureResult[0]
                    var threholdScore: Int? = SingleBaseConfig.getBaseConfig().liveThreshold
                    // 判断第一个阈值是否大于设定阈值，如果大于，检索成功
                    if (livenessModel.maskScore > 0.9) {
                        threholdScore = (threholdScore ?: 80) - 15
                    }
                    if (topFeature.score > threholdScore!!) {
                        livenessModel.featureScore = topFeature.score
                    }
                }
            }
        }
    }

    override fun asyncFace(asyncList: FaceAsyncList) {

    }
}