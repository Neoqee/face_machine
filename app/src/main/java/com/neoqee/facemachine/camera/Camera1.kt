package com.neoqee.facemachine.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.util.Log
import kotlin.math.abs

@Suppress("DEPRECATION")
class Camera1 : ICamera {

    companion object {
        private const val TAG = "Camera1"
        private const val PREVIEW_WIDTH = 640
        private const val PREVIEW_HEIGHT = 480
    }

    private var camera: Camera? = null      // 相机实例
    private var facing = CameraConstants.Facing.BACK    // 摄像头 默认后置
    private var displayOrientation = 0      // 预览角度
    private var mirror = 1      // 镜像处理
    private var cameraSize = Size(-1, -1)

    override fun openCamera(context: Context, surfaceTexture: SurfaceTexture,previewCallback: OnPreviewCallback) {
        openCamera(facing, context, surfaceTexture,previewCallback)
    }

    override fun openCamera(facing: Int, context: Context, surfaceTexture: SurfaceTexture,previewCallback: OnPreviewCallback) {
        closeCamera()

        val numberOfCameras = Camera.getNumberOfCameras()
        if (numberOfCameras <= 0) {

            return
        }

        var cameraIndex = -1
        if (facing == CameraConstants.Facing.BACK) {
            cameraIndex = Camera.CameraInfo.CAMERA_FACING_BACK
        } else if (facing == CameraConstants.Facing.FRONT) {
            cameraIndex = Camera.CameraInfo.CAMERA_FACING_FRONT
        }
        if (cameraIndex == -1) {

            return
        }

        camera = if (numberOfCameras > 1) {
            Camera.open(cameraIndex)
        } else {
            Camera.open()
        }

        if (camera == null) {
            return
        }

        this.facing = facing

        try {
            camera?.let {
                val parameters = it.parameters
                val sizeList = parameters.supportedPreviewSizes
                val optionSize = getOptimalPreviewSize(sizeList, PREVIEW_WIDTH, PREVIEW_HEIGHT)
                optionSize?.let { size ->
                    cameraSize.width = size.width
                    cameraSize.height = size.height
                } ?: kotlin.run {
                    cameraSize.width = PREVIEW_WIDTH
                    cameraSize.height = PREVIEW_HEIGHT
                }
                parameters.setPreviewSize(cameraSize.width, cameraSize.height)
                it.parameters = parameters
                it.setPreviewTexture(surfaceTexture)
                it.setDisplayOrientation(displayOrientation)
                it.setPreviewCallback { data, _ ->
                    previewCallback.onPreview(data,cameraSize.width,cameraSize.height)
                }
                it.startPreview()
                Log.e("Neoqee","openCamera")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun closeCamera() {
        camera?.let {
            it.setPreviewCallback(null)
            it.stopPreview()
            it.release()
            camera = null
        }
    }

    override fun switchCamera() {
        facing = if (facing == CameraConstants.Facing.BACK){
            CameraConstants.Facing.FRONT
        }else{
            CameraConstants.Facing.BACK
        }
    }

    override fun switchCamera(facing: Int) {
        this.facing = facing
    }

    override fun setCameraFacing(facing: Int) {
        this.facing = facing
    }

    override fun getCameraFacing(): Int {
        return facing
    }

    override fun setDisplayOrientation(degree: Int) {
        this.displayOrientation = degree
    }

    override fun releaseCamera() {
        closeCamera()
    }

    /**
     * 获取最优预览Size，解决预览变形问题
     * */
    private fun getOptimalPreviewSize(sizes: List<Camera.Size>?, w: Int, h: Int): Camera.Size? {
        val aspectTolerance: Double = 0.1
        val targetRatio: Double = (w / h).toDouble()
        return sizes?.let { list ->
            var optimalSize: Camera.Size? = null
            var minDiff: Double = Double.MAX_VALUE
            val targetHeight: Int = h
            // Try to find an size match aspect ratio and size
            for (size in list) {
                val ratio: Double = (size.width / size.height).toDouble()
                if (abs(ratio - targetRatio) > aspectTolerance) {
                    continue
                }
                if (abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size
                    minDiff = abs(size.height - targetHeight).toDouble()
                }
            }
            // Cannot fin the one match the aspect ratio, ignore the requirement
            if (null == optimalSize) {
                minDiff = Double.MAX_VALUE
                for (size in list) {
                    if (abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size
                        minDiff = abs(size.height - targetHeight).toDouble()
                    }
                }
            }
            optimalSize
        }
    }

}

private class Size(var width: Int, var height: Int) {}