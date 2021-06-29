package com.neoqee.facemachine

import android.content.Context
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.neoqee.facemachine.baidu.FaceOnDrawTextureViewUtil
import com.neoqee.facemachine.baidu.LivenessModel
import com.neoqee.facemachine.baidu.OnBdFaceDetectCallback
import com.neoqee.facemachine.camera.CameraConstants
import com.neoqee.facemachine.camera.CameraManager
import com.neoqee.facemachine.camera.OnPreviewCallback
import com.neoqee.facemachine.databinding.ActivityMainBinding
import com.neoqee.facemachine.face.FaceManager
import com.neoqee.facemachine.face.IFaceModelInitListener
import com.neoqee.facemachine.utils.ToastUtil

class MainActivity : AppCompatActivity() {
    companion object {
        var mainContext: Context? = null

        const val PREVIEW_WIDTH = 640
        const val PREVIEW_HEIGHT = 480
    }

    private var width = 640
    private var height = 480

    private lateinit var binding: ActivityMainBinding

    private val rgbCameraManager = CameraManager(CameraManager.BACK_FACING)
    private val nirCameraManager = CameraManager(CameraManager.FRONT_FACING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mainContext = this
        initSdkModel()
        setContentView(binding.root)
        initCameraManager()

        // 动态捕捉人脸 并画框
        binding.drawDetectFaceView.run {
            isOpaque = false
            keepScreenOn = true
        }

        binding.cameraPreviewView.postDelayed({
            width = binding.cameraPreviewView.width
            height = binding.cameraPreviewView.height
            Log.e("Neoqee","width = $width height = $height")
        },500)
    }

    override fun onResume() {
        super.onResume()
        startPreview()
    }

    override fun onPause() {
        super.onPause()
        stopPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        rgbCameraManager.releaseCamera()
    }

    private fun initSdkModel() {
        FaceManager.initModel(this, object : IFaceModelInitListener {
            override fun initModelSuccess() {

            }

            override fun initModelFailure(errorCode: Int, msg: String?) {
                Log.e("Neoqee","initModelFailure errorCode = $errorCode msg = $msg")
                if (errorCode != -12) {
                    ToastUtil.toast("模型加载失败，请尝试重启应用")
                }
            }
        })
    }

    private fun initCameraManager() {
        rgbCameraManager.run {
            setCameraFacing(CameraConstants.Facing.BACK)
            setDisplayOrientation(270)
        }
        nirCameraManager.run {
            setCameraFacing(CameraConstants.Facing.FRONT)
            setDisplayOrientation(270)
        }
    }

    private var rgbData: ByteArray? = null
    private var rgbWidth: Int = PREVIEW_WIDTH
    private var rgbHeight: Int = PREVIEW_HEIGHT
    private fun startPreview() {
        rgbCameraManager.startPreview(this, binding.cameraPreviewView, object : OnPreviewCallback {
            override fun onPreview(byteArray: ByteArray?, width: Int, height: Int) {
                rgbData = byteArray
                rgbWidth = width
                rgbHeight = height
                checkData()
            }
        })
    }

    private fun stopPreview() {
        rgbCameraManager.stopPreview()
    }

    @Synchronized
    private fun checkData() {
        if (rgbData != null) {
            FaceManager.detectFace(rgbData, null, null, PREVIEW_WIDTH, PREVIEW_HEIGHT,
                object : OnBdFaceDetectCallback {
                    override fun onFaceDetectDrawCallback(livenessModel: LivenessModel?) {
                        drawFaceFrame(livenessModel)
                    }

                    override fun onFaceDetectCallback(livenessModel: LivenessModel?) {
                        runOnUiThread { handleFaceDetectResult(livenessModel) }
                    }

                })
            rgbData = null
        }
    }

    private fun handleFaceDetectResult(livenessModel: LivenessModel?){
        if (livenessModel == null){

        }else{

        }
    }

    // 绘制相关
    private var rectF = RectF()
    private var paint = Paint()
    private var paintBg = Paint()
    private fun drawFaceFrame(model: LivenessModel?){
        runOnUiThread {
            val canvas = binding.drawDetectFaceView.lockCanvas() ?: return@runOnUiThread
            if (model == null){
                canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR)
                binding.drawDetectFaceView.unlockCanvasAndPost(canvas)
                return@runOnUiThread
            }

            val faceInfos = model.trackFaceInfo
            if (faceInfos.isNullOrEmpty()){
                canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR)
                binding.drawDetectFaceView.unlockCanvasAndPost(canvas)
                return@runOnUiThread
            }

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            val faceInfo = faceInfos[0]

            rectF.set(FaceOnDrawTextureViewUtil.getFaceRectTwo(faceInfo))
            // 检测图片的坐标和显示的坐标不一样，需要转换
            FaceOnDrawTextureViewUtil.mapFromOriginalRect(
                rectF,
                width,
                height,
                model.bdFaceImageInstance!!
            )
            // 人脸框颜色
            FaceOnDrawTextureViewUtil.drawFaceColor(null, paint, paintBg, model)
            // 绘制人脸框
            FaceOnDrawTextureViewUtil.drawCircle(
                canvas, width,height,
                rectF, paint, paintBg, faceInfo
            )

            binding.drawDetectFaceView.unlockCanvasAndPost(canvas)
        }
    }

}