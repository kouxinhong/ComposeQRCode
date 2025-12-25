package com.qrcode.generator.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.github.alexzhirkevich.customqrgenerator.QrCodeGenerator
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.QrOptions
import com.github.alexzhirkevich.customqrgenerator.style.QrColors
import com.github.alexzhirkevich.customqrgenerator.style.QrColor
import com.github.alexzhirkevich.customqrgenerator.style.QrElementsShapes
import com.github.alexzhirkevich.customqrgenerator.style.QrPixelShape
import com.github.alexzhirkevich.customqrgenerator.style.QrBallShape
import com.github.alexzhirkevich.customqrgenerator.style.QrFrameShape
import com.github.alexzhirkevich.customqrgenerator.QrErrorCorrectionLevel
import com.qrcode.generator.models.ErrorCorrectionLevel
import com.qrcode.generator.models.QRGeneratorConfig
import com.qrcode.generator.models.ShapePreset

object QRoseGenerator {

    fun generateBitmap(
        content: String,
        config: QRGeneratorConfig
    ): ImageBitmap {
        val data = QrData.Text(content)

        val options = QrOptions.Builder(config.size)
            .padding(config.padding.toFloat())
            .errorCorrectionLevel(config.errorCorrectionLevel.toCustomQrLevel())
            .colors(
                QrColors(
                    dark = QrColor.Solid(config.foregroundColor.value.toInt()),
                    light = QrColor.Solid(config.backgroundColor.value.toInt())
                )
            )
            .shapes(
                QrElementsShapes(
                    darkPixel = getPixelShape(config.style),
                    ball = QrBallShape.RoundCorners(.25f),
                    frame = QrFrameShape.RoundCorners(.25f)
                )
            )
            .build()

        val generator = QrCodeGenerator()
        val bitmap = generator.generateQrCode(data, options)

        return bitmap.asImageBitmap()
    }

    private fun getPixelShape(preset: ShapePreset): QrPixelShape {
        return when (preset) {
            is ShapePreset.Business -> QrPixelShape.Default
            is ShapePreset.Artistic -> QrPixelShape.Circle()
            is ShapePreset.Elegant -> QrPixelShape.RoundCorners(.4f)
            is ShapePreset.Vibrant -> QrPixelShape.Circle()
            is ShapePreset.Minimal -> QrPixelShape.Default
            is ShapePreset.Custom -> QrPixelShape.Default
        }
    }

    private fun ErrorCorrectionLevel.toCustomQrLevel(): QrErrorCorrectionLevel {
        return when (this) {
            ErrorCorrectionLevel.LOW -> QrErrorCorrectionLevel.Low
            ErrorCorrectionLevel.MEDIUM -> QrErrorCorrectionLevel.Medium
            ErrorCorrectionLevel.QUARTILE -> QrErrorCorrectionLevel.MediumHigh
            ErrorCorrectionLevel.HIGH -> QrErrorCorrectionLevel.High
        }
    }
}
