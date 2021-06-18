package com.neoqee.facemachine.baidu

object GlobalSet {
    // 模型在asset 下path 为空
    const val PATH = ""

    // 模型在SD 卡下写对应的绝对路径
    // public static final String PATH = "/storage/emulated/0/baidu_face/model/";
    const val FEATURE_SIZE = 512
    const val TIME_TAG = "face_time"

    // 遮罩比例
    const val SURFACE_RATIO = 0.6f
    const val DETECT_VIS_MODEL = (PATH
            + "face-sdk-models/detect/detect_rgb-mobilenet-pa-kouzhao.model.float32-0.0.13.1")
    const val DETECT_NIR_MODE = (PATH
            + "face-sdk-models/detect/detect_nir-customized-pa-normquant.model.int8-0.0.12.1")

    //    public static final String ALIGN_RGB_MODEL = PATH
    //            + "align/align_rgb-mobilenet-pa-bigmodel_191224.model.int8-0.7.6.1";
    const val ALIGN_RGB_MODEL = (PATH
            + "face-sdk-models/align/align_rgb-customized-pa-kouzhao.model.float32-6.4.12.1")
    const val ALIGN_NIR_MODEL = (PATH
            + "face-sdk-models/align/align_nir-customized-pa-model.model.float32-6.4.11.1")
    const val ALIGN_TRACK_MODEL = (PATH
            + "face-sdk-models/align/align_rgb-customized-pa-mobile.model.float32-0.7.5.3")
    const val LIVE_VIS_MODEL = (PATH
            + "face-sdk-models/silent_live/liveness_rgb-customized-pa-kouzhao.model.float32-1.1.18.1")
    const val LIVE_NIR_MODEL = (PATH
            + "face-sdk-models/silent_live/liveness_nir-customized-pa-60_android.model.float32-1.1.11.1")
    const val LIVE_DEPTH_MODEL = (PATH
            + "face-sdk-models/silent_live/liveness_depth-customized-pa-paddle_60.model.float32-1.1.13.1")
    const val RECOGNIZE_VIS_MODEL = (PATH
            + "face-sdk-models/feature/feature_live-mnasnet-pa-kouzhao.model.int8-2.0.144.1")
    const val RECOGNIZE_IDPHOTO_MODEL = (PATH
            + "face-sdk-models/feature/feature_id-mnasnet-pa-renzheng.model.int8-2.0.135.1")
    const val RECOGNIZE_NIR_MODEL = (PATH
            + "face-sdk-models/feature/feature_nir-mnasnet-pa-nir_rgb_mix.model.int8-2.0.138.1")
    const val RECOGNIZE_RGBD_MODEL = (PATH
            + "face-sdk-models/feature/feature_rgbd-mnasnet-pa-RGBD_FaceID_5.model.int8-2.0.88.2")
    const val OCCLUSION_MODEL = (PATH
            + "face-sdk-models/occlusion/occlusion-customized-pa-paddle.model.float32-2.0.7.1")
    const val BLUR_MODEL = (PATH
            + "face-sdk-models/blur/blur-customized-pa-blurnet_9768.model.float32-3.0.9.1")
    const val ATTRIBUTE_MODEL = (PATH
            + "face-sdk-models/attribute/attribute-customized-pa-mobile.model.float32-1.0.9.4")
    const val EMOTION_MODEL = (PATH
            + "")
    const val GAZE_MODEL = (PATH
            + "face-sdk-models/gaze/gaze-customized-pa-mobile.model.float32-1.0.3.3")
    const val DRIVEMONITOR_MODEL = (PATH
            + "face-sdk-models/driver_monitor/driver_monitor_nir-customized-pa-DMS_rgb_nir_detect.model.float32-1.0.1.1")
    const val MOUTH_MASK = (PATH
            + "face-sdk-models/mouth_mask/mouth_mask-customized-pa-20200306.model.float32-1.0.6.3")
    const val BEST_IMAGE = (PATH
            + "face-sdk-models/best_image/best_image-mobilenet-pa-faceid60.model.float32-1.0.1.1")

    // 图片尺寸限制大小
    const val PICTURE_SIZE = 1000000

    // 摄像头类型
    const val TYPE_CAMERA = "TYPE_CAMERA"
    const val ORBBEC = 1
    const val IMIMECT = 2
    const val ORBBECPRO = 3
    const val ORBBECPROS1 = 4
    const val ORBBECPRODABAI = 5
    const val ORBBECPRODEEYEA = 6
    const val ORBBECATLAS = 7
}