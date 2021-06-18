package com.yangfei.faceacm.model

import com.squareup.moshi.JsonClass

/**
 * author : shangrong
 * date : 2019/5/22 9:10 PM
 * description :配置文件
 */
@JsonClass(generateAdapter = true)
class BaseConfig {
    // 设备通信密码
    var dPass: String = ""

    // RGB检测帧回显
    var display: Boolean = true

    // RGB预览Y轴转向falese为0，true为180
    var rgbRevert: Boolean = true // todo

    // 默认为0。可传入0、90、180、270四个选项。
    //    private int videoDirection = 0;
    var videoDirection: Int = 270

    // 0：RGB无镜像，1：有镜像
    //    private int mirrorRGB = 0;     // 设备
    var mirrorRGB: Int = 0 // 手机oppo todo

    // 默认为0。可传入0、90、180、270四个选项
    //    private int detectDirection = 0;
    var detectDirection: Int = 270 // oppo手机

    // 0：NIR无镜像，1：有镜像
    var mirrorNIR: Int = 0

    // NIR或depth实时视频预览
    var nirOrDepth: Boolean = false

    // 默认为false。可选项为"true"、"false"，是否开启调试显示，将会作用到所有视频流识别页面，包含1：N、1：1采集人脸图片环节。
    var isDebug: Boolean = false

    // 默认为wireframe。可选项为"wireframe"、"fixedarea"，如选择fixed_area，需要传入半径，px像素为单位
    var detectFrame: String = "wireframe"

    //    public int getRadius() {
    //        return radius;
    //    }
    //
    //    public void setRadius(int radius) {
    //        this.radius = radius;
    //    }
    // 当选择fixed_area，需要传入半径信息，以px为单位，如50px
    //    private int radius = 50;

    //    private int detectDirection = 90;// 设备
    // 默认为max。分为"max" 、"none"三个方式，分别是最大人脸 ，和不检测人脸
    var trackType: String = "max"

    // 默认为80px。可传入大于50px的数值，小于此大小的人脸不予检测
    var minimumFace: Int = 60

    // 模糊度设置，默认0.5。取值范围[0~1]，0是最清晰，1是最模糊
    var blur: Float = 1.0F

    // 光照设置，默认40.取值范围[0~255], 数值越大，光线越强
    var illumination: Int = 40

    // 姿态阈值
    var gesture: Float = 15F

    // 三维旋转之俯仰角度[-90(上), 90(下)]，默认20
    var pitch: Float = 20F

    // 平面内旋转角[-180(逆时针), 180(顺时针)]，默认20
    var roll: Float = 20F

    // 三维旋转之左右旋转角[-90(左), 90(右)]，默认20
    var yaw: Float = 20F

    // 遮挡阈值
    var occlusion: Float = 0.6F

    // 左眼被遮挡的阈值，默认0.6
    var leftEye: Float = 0.6F

    // 右眼被遮挡的阈值，默认0.6
    var rightEye: Float = 0.6F

    // 鼻子被遮挡的阈值，默认0.7
    var nose: Float = 0.7F

    // 嘴巴被遮挡的阈值，默认0.7
    var mouth: Float = 0.7F

    // 左脸颊被遮挡的阈值，默认0.8
    var leftCheek: Float = 0.8F

    // 右脸颊被遮挡的阈值，默认0.8
    var rightCheek: Float = 0.8F

    // 下巴被遮挡阈值，默认为0.6
    var chinContour: Float = 0.6F

    // 人脸完整度，默认为1。0为人脸溢出图像边界，1为人脸都在图像边界内
    var completeness: Float = 1.0F

    // 识别阈值，0-100，默认为80分,需要选择具体模型的阈值。live：80、idcard：80
    var liveThreshold: Int = 80

    // 识别阈值，0-100，默认为80分,需要选择具体模型的阈值。live：80、idcard：80
    var idThreshold: Int = 80

    // 识别阈值，0-100，默认为80分,需要选择具体模型的阈值。live：80、idcard：80
    var rgbAndNirThreshold: Int = 80

    // 模态切换光线阈值
    var camera_lightThreshold: Int = 50

    // 使用的特征抽取模型默认为生活照：1；证件照：2；RGB+NIR混合模态模型：3；
    var activeModel: Int = 1

    // 识别结果出来后的演示展示，默认为0ms
    var timeLapse: Int = 60

    // 不使用活体: 0
    // RGB活体：1
    // RGB+NIR活体：2
    // RGB+Depth活体：3
    // RGB+NIR+Depth活体：4
    var type: Int = 2

    // 是否开启质量检测开关
    var isQualityControl: Boolean = true

    // 是否开启活体检测开关
    var isLivingControl: Boolean = true

    // RGB活体阀值
    var rgbLiveScore: Float = 0.80F

    // NIR活体阀值
    var nirLiveScore: Float = 0.80F

    // Depth活体阀值
    var depthLiveScore: Float = 0.80F

    // 帧数阈值
    var framesThreshold: Int = 3

    // 0:奥比中光海燕、大白（640*400）
    // 1:奥比中光海燕Pro、Atlas（400*640）
    // 2:奥比中光蝴蝶、Astra Pro\Pro S（640*480）
    // 3:舜宇Seeker06
    // 4:螳螂慧视天蝎P1
    // 5:瑞识M720N
    // 6:奥比中光Deeyea(结构光)
    // 7:华捷艾米A100S、A200(结构光)
    // 8:Pico DCAM710(ToF)
    var cameraType: Int = 0

    // 是否开启属性检测
    var isAttribute: Boolean = false

    // rgb和nir摄像头宽
    var rgbAndNirWidth: Int = 640

    // rgb和nir摄像头高
    var rgbAndNirHeight: Int = 480

    // depth摄像头宽
    var depthWidth: Int = 640

    // depth摄像头高
    var depthHeight: Int = 480

    // 是否开启最优人脸检测
    var isUsingBestImage: Boolean = false

    // 最优人脸分数
    var bestImageScore: Int = 30

    fun getdPass(): String {
        return dPass
    }

    fun setdPass(dPass: String) {
        this.dPass = dPass
    }

}