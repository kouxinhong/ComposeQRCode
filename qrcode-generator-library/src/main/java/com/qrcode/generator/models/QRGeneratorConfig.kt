package com.qrcode.generator.models

import androidx.compose.ui.graphics.Color

enum class ErrorCorrectionLevel {
    LOW,
    MEDIUM,
    QUARTILE,
    HIGH
}

data class QRGeneratorConfig(
    val size: Int = 512,
    val padding: Float = 0.1f,
    val errorCorrectionLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.MEDIUM,
    val foregroundColor: Color = Color.Black,
    val backgroundColor: Color = Color.White,
    val style: ShapePreset = ShapePreset.Business
) {
    companion object {
        fun default(size: Int = 512): QRGeneratorConfig {
            return QRGeneratorConfig(size = size)
        }

        fun businessStyle(size: Int = 512): QRGeneratorConfig {
            return QRGeneratorConfig(
                size = size,
                foregroundColor = Color(0xFF1976D2),
                backgroundColor = Color.White,
                style = ShapePreset.Business
            )
        }

        fun artisticStyle(size: Int = 512): QRGeneratorConfig {
            return QRGeneratorConfig(
                size = size,
                foregroundColor = Color(0xFFE91E63),
                backgroundColor = Color(0xFFFCE4EC),
                style = ShapePreset.Artistic
            )
        }

        fun elegantStyle(size: Int = 512): QRGeneratorConfig {
            return QRGeneratorConfig(
                size = size,
                foregroundColor = Color(0xFF424242),
                backgroundColor = Color(0xFFF5F5F5),
                style = ShapePreset.Elegant
            )
        }

        fun vibrantStyle(size: Int = 512): QRGeneratorConfig {
            return QRGeneratorConfig(
                size = size,
                foregroundColor = Color(0xFFFF5722),
                backgroundColor = Color(0xFFFFF3E0),
                style = ShapePreset.Vibrant
            )
        }

        fun minimalStyle(size: Int = 512): QRGeneratorConfig {
            return QRGeneratorConfig(
                size = size,
                foregroundColor = Color.Black,
                backgroundColor = Color.White,
                padding = 0.15f,
                style = ShapePreset.Minimal
            )
        }
    }
}
