package com.neoqee.facemachine.baidu

import com.baidu.idl.main.facesdk.FaceInfo
import com.baidu.idl.main.facesdk.model.BDFaceImageInstance
import com.neoqee.facemachine.face.FaceModel

class LivenessModel : FaceModel() {

    var detectStatus = 0    // 0 = 开始检测 1 = 快速检测成功 2 = 详细检测并抽取特征值

    var faceInfo: FaceInfo? = null

    var rgbLivenessScore = 0f
    var irLivenessScore = 0f
    var depthLivenessScore = 0f

    var landmarks: FloatArray? = null
    var feature: ByteArray? = null
    var trackStatus = 0
    var featureScore = 0f
    var featureCode = 0f

    var bdFaceImageInstance: BDFaceImageInstance? = null
//    var user: User? = null
//    var userEntity: UserEntity? = null
    var trackFaceInfo: Array<FaceInfo>? = null

    var maskScore = 0f

    var depthtLivenessDuration: Long = 0

    var rgbFastDuration: Long = 0       // 快速人脸检测时间
    var rgbAccurateDuration: Long = 0   // 精确人脸检测时间
    var maskCheckDuration: Long = 0     // 口罩检测时间
    var qualityCheckDuration: Long = 0  // 质量检测时间
    var rgbLivenessDuration: Long = 0   // rgb活体检测时间
    var irDetectDuration: Long = 0      // 红外人脸检测时间
    var irLivenessDuration: Long = 0    // 红外活体检测时间
    var featureDuration: Long = 0       // 特征提取时间
    var featureSearchDuration: Long = 0 // 特征检索时间
    var checkDuration: Long = 0         // 特征检索全时间
    var allDetectDuration: Long = 0     // 人脸检索全时间



    override fun toString(): String {
        return "LivenessModel: { " +
                "快速人脸检测时间 = $rgbFastDuration , " +
                "精确人脸检测时间 = $rgbAccurateDuration ," +
                "口罩检测时间 = $maskCheckDuration , " +
                "质量检测时间 = $qualityCheckDuration , " +
                "rgb活体检测时间 = $rgbLivenessDuration ," +
                "红外人脸检测时间 = $irDetectDuration ," +
                "红外活体检测时间 = $irLivenessDuration ," +
                "特征提取时间 = $featureDuration ," +
                "特征检索时间 = $featureSearchDuration ," +
                "特征检索全时间 = $checkDuration ," +
                "人脸检索全时间 = $allDetectDuration }"
    }
}