package com.neoqee.facemachine.camera

import android.content.Context
import android.graphics.SurfaceTexture

interface ICamera {

    fun openCamera(context: Context, surfaceTexture: SurfaceTexture, previewCallback: OnPreviewCallback)

    fun openCamera(facing: Int, context: Context, surfaceTexture: SurfaceTexture,previewCallback: OnPreviewCallback)

    fun closeCamera()

    fun switchCamera()

    fun switchCamera(facing: Int)

    fun setCameraFacing(facing: Int)

    fun getCameraFacing(): Int

    fun setDisplayOrientation(degree: Int)

    fun releaseCamera()

}