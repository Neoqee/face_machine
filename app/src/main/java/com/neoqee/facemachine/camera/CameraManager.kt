package com.neoqee.facemachine.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.TextureView

interface OnPreviewCallback {
    fun onPreview(byteArray: ByteArray?, width: Int, height: Int)
}

class CameraManager(private val threadName: String = "camera_thread") : ICamera, TextureView.SurfaceTextureListener {

    companion object{
        const val BACK_FACING = "camera_back_thread"
        const val FRONT_FACING = "camera_front_thread"
    }

    private val camera = Camera1()

    private var mContext: Context? = null
    private var textureView: TextureView? = null
    private var previewCallback: OnPreviewCallback? = null

    private var handler: Handler? = null
    private var cameraThread: HandlerThread? = null

    fun startPreview(context: Context, textureView: TextureView, callback: OnPreviewCallback) {
        mContext = context
        this.textureView = textureView
        previewCallback = callback

        if (textureView.isAvailable) {
            openCamera(context, textureView.surfaceTexture, callback)
        } else {
            textureView.surfaceTextureListener = this
        }
    }

    fun stopPreview() {

    }

    override fun openCamera(
        context: Context,
        surfaceTexture: SurfaceTexture,
        previewCallback: OnPreviewCallback
    ) {
        postTask {
            camera.openCamera(context, surfaceTexture, previewCallback)
        }
    }

    override fun openCamera(
        facing: Int,
        context: Context,
        surfaceTexture: SurfaceTexture,
        previewCallback: OnPreviewCallback
    ) {
        postTask {
            camera.openCamera(facing, context, surfaceTexture, previewCallback)
        }
    }

    override fun closeCamera() {
        postTask { camera.closeCamera() }
    }

    override fun switchCamera() {
        postTask {
            camera.switchCamera()
            textureView?.run {
                if (isAvailable) {
                    camera.openCamera(mContext!!, surfaceTexture, previewCallback!!)
                }
            }
        }
    }

    override fun switchCamera(facing: Int) {
        postTask {
            camera.switchCamera(facing)
            textureView?.run {
                if (isAvailable) {
                    camera.openCamera(mContext!!, surfaceTexture, previewCallback!!)
                }
            }
        }
    }

    override fun setCameraFacing(facing: Int) {
        postTask {
            camera.setCameraFacing(facing)
        }
    }

    override fun getCameraFacing(): Int {
        return camera.getCameraFacing()
    }

    override fun setDisplayOrientation(degree: Int) {
        postTask { camera.setDisplayOrientation(degree) }
    }

    override fun releaseCamera() {
        if (handler == null) return
        postTask {
            camera.releaseCamera()
            stopBackground()
            mContext = null
        }
    }

// =================================================================================================

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        postTask { camera.releaseCamera() }
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        Log.e("Neoqee","onSurfaceTextureAvailable")
        surface?.let {
            openCamera(mContext!!, it, previewCallback!!)
        }
    }

// =================================================================================================

    private fun getHandler(): Handler {
        if (handler == null || cameraThread == null) {
            startBackground()
        }
        return handler!!
    }

    private fun startBackground() {
        stopBackground()
        cameraThread = HandlerThread(threadName)
        cameraThread!!.start()
        handler = Handler(cameraThread!!.looper)
    }

    private fun stopBackground() {
        if (cameraThread != null) {
            cameraThread!!.quitSafely()
            cameraThread = null
        }
        if (handler != null) {
            handler = null
        }
    }

    private fun postTask(runnable: Runnable) {
        getHandler().post(runnable)
    }

    private fun postTask(block: () -> Unit) {
        postTask(Runnable(block))
    }

}