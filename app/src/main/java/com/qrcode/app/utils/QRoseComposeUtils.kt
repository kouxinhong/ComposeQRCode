package com.qrcode.app.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.createQrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColors
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColor
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorShapes
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorPixelShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorBallShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorFrameShape

object QRoseComposeUtils {

    @Composable
    fun rememberQRCode(
        content: String,
        style: QRCodeStyle = QRCodeStyle.BASIC,
        size: Int = 200,
        foregroundColor: Int = AndroidColor.BLACK,
        backgroundColor: Int = AndroidColor.WHITE
    ): ImageBitmap {
        return remember(content, style, size, foregroundColor, backgroundColor) {
            generateQRCodeBitmap(content, style, size, foregroundColor, backgroundColor)
        }
    }

    @Composable
    fun rememberQRCode(
        content: String,
        style: QRCodeStyle = QRCodeStyle.BASIC,
        size: Int = 200
    ): ImageBitmap {
        return remember(content, style, size) {
            generateQRCodeBitmap(content, style, size, AndroidColor.BLACK, AndroidColor.WHITE)
        }
    }

    private fun generateQRCodeBitmap(
        content: String,
        style: QRCodeStyle,
        size: Int,
        foregroundColor: Int,
        backgroundColor: Int
    ): ImageBitmap {
        val data = QrData.Text(content)
        
        val options = createQrVectorOptions {
            padding = 0.1f
            
            colors {
                dark = QrVectorColor.Solid(foregroundColor)
                light = QrVectorColor.Solid(backgroundColor)
            }
            
            shapes {
                darkPixel = getPixelShape(style)
                lightPixel = QrVectorPixelShape.Default
                ball = getBallShape(style)
                frame = getFrameShape(style)
            }
        }

        val drawable = QrCodeDrawable(data, options)
        
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        drawable.setBounds(0, 0, size, size)
        drawable.draw(canvas)

        return bitmap.asImageBitmap()
    }

    private fun getPixelShape(style: QRCodeStyle): QrVectorPixelShape {
        return when (style) {
            QRCodeStyle.BASIC -> QrVectorPixelShape.Default
            QRCodeStyle.CIRCLE -> QrVectorPixelShape.Circle()
            QRCodeStyle.ROUNDED -> QrVectorPixelShape.RoundCorners(0.5f)
            QRCodeStyle.GRADIENT -> QrVectorPixelShape.Default
            QRCodeStyle.ARTISTIC -> QrVectorPixelShape.Circle()
            QRCodeStyle.DOT -> QrVectorPixelShape.Circle()
            QRCodeStyle.RHOMBUS -> QrVectorPixelShape.Rhombus()
            QRCodeStyle.STAR -> QrVectorPixelShape.Star
            QRCodeStyle.ROUNDED_VERTICAL -> QrVectorPixelShape.RoundCornersVertical()
            QRCodeStyle.ROUNDED_HORIZONTAL -> QrVectorPixelShape.RoundCornersHorizontal()
            else -> QrVectorPixelShape.Default
        }
    }

    private fun getBallShape(style: QRCodeStyle): QrVectorBallShape {
        return when (style) {
            QRCodeStyle.CIRCLE_BALL -> QrVectorBallShape.Circle(1f)
            QRCodeStyle.ROUNDED_BALL -> QrVectorBallShape.RoundCorners(0.25f)
            QRCodeStyle.RHOMBUS_BALL -> QrVectorBallShape.Rhombus()
            else -> QrVectorBallShape.RoundCorners(0.25f)
        }
    }

    private fun getFrameShape(style: QRCodeStyle): QrVectorFrameShape {
        return when (style) {
            QRCodeStyle.CIRCLE_FRAME -> QrVectorFrameShape.Circle(1f, 1f)
            QRCodeStyle.ROUNDED_FRAME -> QrVectorFrameShape.RoundCorners(0.25f, 1f)
            else -> QrVectorFrameShape.RoundCorners(0.25f, 1f)
        }
    }
}