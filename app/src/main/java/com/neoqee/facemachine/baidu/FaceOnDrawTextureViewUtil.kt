package com.neoqee.facemachine.baidu

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.TextureView
import com.baidu.idl.main.facesdk.FaceInfo
import com.baidu.idl.main.facesdk.model.BDFaceImageInstance
import com.neoqee.facemachine.db.entity.UserEntity
import com.neoqee.facemachine.utils.DensityUtils

class FaceOnDrawTextureViewUtil {
    companion object {
        private var faceID = -1

        /**
         * 通过中心点坐标（x，y） 和 width ，绘制Rect
         *
         * @param faceInfo
         * @return
         */
        fun getFaceRectTwo(faceInfo: FaceInfo): Rect {
            val rect = Rect()
            rect.top = (faceInfo.centerY - faceInfo.width / 1.3).toInt()
            rect.left = (faceInfo.centerX - faceInfo.width / 2).toInt()
            rect.right = (faceInfo.centerX + faceInfo.width / 2).toInt()
            rect.bottom = (faceInfo.centerY + faceInfo.width / 1.8).toInt()
            return rect
        }

        fun getFaceRectTwo(
            context: Context?,
            faceInfo: FaceInfo
        ): Rect {
            val rect = Rect()
            var offset = 0f
            if (SingleBaseConfig.getBaseConfig().mirrorRGB == 1) {
                val width = DensityUtils.getDisplayWidth(context!!) / 2
                offset = (width - faceInfo.centerX) * 2
                Log.e(
                    "Neoqee",
                    "width = " + width + "centerX = " + faceInfo.centerX + "offset = " + offset
                )
            }
            rect.top = (faceInfo.centerY - faceInfo.width / 1.3).toInt()
            rect.left = (faceInfo.centerX - faceInfo.width / 2 + offset).toInt()
            rect.right = (faceInfo.centerX + faceInfo.width / 2 + offset).toInt()
            rect.bottom = (faceInfo.centerY + faceInfo.width / 1.8).toInt()
            return rect
        }

        fun mapFromOriginalRect(
            rectF: RectF?,
            previewWidth: Int,
            previewHeight: Int,
            imageFrame: BDFaceImageInstance
        ) {
            // 获取屏幕的宽
            val selfWidth: Int = previewWidth
            // 获取屏幕的高
            val selfHeight: Int = previewHeight
            // 新建矩阵对象
            val matrix = Matrix()
            // 当屏幕宽度/图像宽度>屏幕高度/图像高度时
            if (selfWidth * imageFrame.height > selfHeight * imageFrame.width) {
                // 将高度按照宽度比进行缩放
                val targetHeight = imageFrame.height * selfWidth / imageFrame.width
                // 计算平移距离
                val delta = (targetHeight - selfHeight) / 2
                // 计算宽度比
                val ratio = 1.0f * selfWidth / imageFrame.width
                // 设置矩阵变换缩放比
                matrix.postScale(ratio, ratio)
                // 设置变换矩阵的平移距离
                matrix.postTranslate(0f, -delta.toFloat())
            } else {
                // 将宽度按照高度比进行缩放
                val targetWith = imageFrame.width * selfHeight / imageFrame.height
                // 计算平移距离
                val delta = (targetWith - selfWidth) / 2
                // 计算宽度比
                val ratio = 1.0f * selfHeight / imageFrame.height
                // 设置矩阵变换缩放比
                matrix.postScale(ratio, ratio)
                // 设置变换矩阵的平移距离
                matrix.postTranslate(-delta.toFloat(), 0f)
            }
            // 对人脸框数据进行矩阵变换
            matrix.mapRect(rectF)
        }

        fun mapFromOriginalRect(
            rectF: RectF?,
            textureView: TextureView,
            imageFrame: BDFaceImageInstance
        ) {
            val selfWidth = textureView.width
            val selfHeight = textureView.height
            val matrix = Matrix()
            if (selfWidth * imageFrame.height > selfHeight * imageFrame.width) {
                val targetHeight = imageFrame.height * selfWidth / imageFrame.width
                val delta = (targetHeight - selfHeight) / 2
                val ratio = 1.0f * selfWidth / imageFrame.width
                matrix.postScale(ratio, ratio)
                matrix.postTranslate(0f, -delta.toFloat())
            } else {
                val targetWith = imageFrame.width * selfHeight / imageFrame.height
                val delta = (targetWith - selfWidth) / 2
                val ratio = 1.0f * selfHeight / imageFrame.height
                matrix.postScale(ratio, ratio)
                matrix.postTranslate(-delta.toFloat(), 0f)
            }
            matrix.mapRect(rectF)
        }

        fun converttPointXY(
            pointXY: FloatArray, previewWidth: Int, previewHeight: Int,
            imageFrame: BDFaceImageInstance, width: Float
        ) {
            val selfWidth: Int = previewWidth
            val selfHeight: Int = previewHeight
            if (selfWidth * imageFrame.height > selfHeight * imageFrame.width) {
                val targetHeight = imageFrame.height * selfWidth / imageFrame.width
                val delta = (targetHeight - selfHeight) / 2
                val ratio = 1.0f * selfWidth / imageFrame.width
                pointXY[0] = pointXY[0] * ratio
                pointXY[1] = pointXY[1] * ratio
                pointXY[1] = pointXY[1] - delta
                pointXY[2] = width * ratio
                pointXY[3] = width * ratio
            } else {
                val targetWith = imageFrame.width * selfHeight / imageFrame.height
                val delta = (targetWith - selfWidth) / 2
                val ratio = 1.0f * selfHeight / imageFrame.height
                pointXY[0] = pointXY[0] * ratio
                pointXY[1] = pointXY[1] * ratio
                pointXY[0] = pointXY[0] - delta
                pointXY[2] = width * ratio
                pointXY[3] = width * ratio
            }
        }

        /**
         * 绘制人脸框
         *
         * @param canvas      画布
         * @param rectF       矩形
         * @param paint       画笔
         * @param strokeWidth 笔画的宽度
         * @param mScreenRate 线的长度
         */
        fun drawRect(
            canvas: Canvas, rectF: RectF, paint: Paint?,
            strokeWidth: Float, mScreenRate: Float
        ) {
            // 左上横线
            canvas.drawRect(
                rectF.left - strokeWidth / 3,
                rectF.top - strokeWidth / 2,
                rectF.left + mScreenRate,
                rectF.top - strokeWidth / 2,
                paint!!
            )
            // 左上竖线
            canvas.drawRect(
                rectF.left - strokeWidth / 2,
                rectF.top - strokeWidth / 3,
                rectF.left - strokeWidth / 2,
                rectF.top + mScreenRate,
                paint
            )
            // 右上横线
            canvas.drawRect(
                rectF.right - mScreenRate,
                rectF.top - strokeWidth / 2,
                rectF.right + strokeWidth / 3,
                rectF.top - strokeWidth / 2,
                paint
            )
            // 右上竖线
            canvas.drawRect(
                rectF.right + strokeWidth / 2,
                rectF.top - strokeWidth / 3,
                rectF.right + strokeWidth / 2,
                rectF.top + mScreenRate,
                paint
            )
            // 左下横线
            canvas.drawRect(
                rectF.left - strokeWidth / 3,
                rectF.bottom + strokeWidth / 2,
                rectF.left + mScreenRate,
                rectF.bottom + strokeWidth / 2,
                paint
            )
            // 左下竖线
            canvas.drawRect(
                rectF.left - strokeWidth / 2,
                rectF.bottom - mScreenRate,
                rectF.left - strokeWidth / 2,
                rectF.bottom + strokeWidth / 3,
                paint
            )
            // 右下横线
            canvas.drawRect(
                rectF.right - mScreenRate,
                rectF.bottom + strokeWidth / 2,
                rectF.right + strokeWidth / 3,
                rectF.bottom + strokeWidth / 2,
                paint
            )
            // 右下竖线
            canvas.drawRect(
                rectF.right + strokeWidth / 2,
                rectF.bottom - mScreenRate,
                rectF.right + strokeWidth / 2,
                rectF.bottom + strokeWidth / 3,
                paint
            )
        }

        /**
         * 人脸框颜色
         *
         * @param mUser         当前的user
         * @param paint         人脸边框部分
         * @param paintBg       人脸边框阴影部分
         * @param livenessModel 人脸框数据
         */
        fun drawFaceColor(
            mUser: UserEntity?,
            paint: Paint,
            paintBg: Paint,
            livenessModel: LivenessModel
        ) {
            val trackStatus: Int = livenessModel.trackStatus
            // 当前为track
            if (trackStatus == 1) {
                // 当当前帧的id == 上一帧的ID
                if (faceID == livenessModel.trackFaceInfo!![0].faceID && mUser == null
                ) {
                    paint.color = Color.parseColor("#FEC133")
                    paintBg.color = Color.parseColor("#FEC133")
                }
                if (faceID == livenessModel.trackFaceInfo!![0].faceID && mUser != null
                    || faceID != livenessModel.trackFaceInfo!![0].faceID
                ) {
                    paint.color = Color.parseColor("#00baf2")
                    paintBg.color = Color.parseColor("#00baf2")
                }
            } else {
                if (mUser == null) {
                    faceID = livenessModel.trackFaceInfo!![0].faceID
                    paint.color = Color.parseColor("#FEC133")
                    paintBg.color = Color.parseColor("#FEC133")
                } else {
                    faceID = livenessModel.trackFaceInfo!![0].faceID
                    paint.color = Color.parseColor("#00baf2")
                    paintBg.color = Color.parseColor("#00baf2")
                }
            }
        }

        fun drawCircle(
            canvas: Canvas,
//            mAutoCameraPreviewView: AutoTexturePreviewView,
            previewWidth: Int,
            previewHeight: Int,
            rectF: RectF,
            paint: Paint,
            paintBg: Paint,
            faceInfo: FaceInfo
        ) {
            paint.style = Paint.Style.STROKE
            paintBg.style = Paint.Style.STROKE
            // 画笔粗细
            paint.strokeWidth = 8f
            // 设置线条等图形的抗锯齿
            paint.isAntiAlias = true
            paintBg.strokeWidth = 13f
            paintBg.alpha = 90
            // 设置线条等图形的抗锯齿
            paintBg.isAntiAlias = true
            if (faceInfo.width > faceInfo.height) {
                if (!SingleBaseConfig.getBaseConfig().rgbRevert) {
                    canvas.drawCircle(
                        previewWidth - rectF.centerX(),
                        rectF.centerY(), rectF.width() / 2 - 8, paintBg
                    )
                    canvas.drawCircle(
                        previewWidth - rectF.centerX(),
                        rectF.centerY(), rectF.width() / 2, paint
                    )
                } else {
                    canvas.drawCircle(
                        rectF.centerX(), rectF.centerY(),
                        rectF.width() / 2 - 8, paintBg
                    )
                    canvas.drawCircle(
                        rectF.centerX(), rectF.centerY(),
                        rectF.width() / 2, paint
                    )
                }
            } else {
                if (!SingleBaseConfig.getBaseConfig().rgbRevert) {
                    canvas.drawCircle(
                        previewWidth - rectF.centerX(),
                        rectF.centerY(), rectF.height() / 2 - 8, paintBg
                    )
                    canvas.drawCircle(
                        previewWidth - rectF.centerX(),
                        rectF.centerY(), rectF.height() / 2, paint
                    )
                } else {
                    canvas.drawCircle(
                        rectF.centerX(), rectF.centerY(),
                        rectF.height() / 2 - 8, paintBg
                    )
                    canvas.drawCircle(
                        rectF.centerX(), rectF.centerY(),
                        rectF.height() / 2, paint
                    )
                }
            }
        }
    }
}