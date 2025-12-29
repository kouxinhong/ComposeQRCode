package com.qrcode.app.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import android.graphics.Color as AndroidColor

/**
 * QRose二维码样式配置
 * 包含所有可自定义的样式选项
 */
data class QRoseStyleConfig(
    /**
     * 基础设置
     */
    val size: Int = 256,
    val padding: Float = 0.1f,
    val errorCorrectionLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.MEDIUM,

    /**
     * 暗像素颜色（前景色）
     */
    val darkPixelColor: Color = Color.Black,
    val darkPixelGradientColors: List<Color> = listOf(Color.Black),
    val darkPixelGradientAngle: Float = 0f,
    val isDarkPixelGradient: Boolean = false,

    /**
     * 亮像素颜色（背景色）
     */
    val lightPixelColor: Color = Color.White,
    val lightPixelGradientColors: List<Color> = listOf(Color.White),
    val lightPixelGradientAngle: Float = 0f,
    val isLightPixelGradient: Boolean = false,

    /**
     * 背景设置（用于外部背景，非二维码数据区域）
     */
    val backgroundColor: Color = Color.White,
    val backgroundType: BackgroundType = BackgroundType.SOLID,
    val backgroundGradientColors: List<Color> = listOf(Color.White),
    val backgroundGradientAngle: Float = 0f,
    val isBackgroundGradient: Boolean = false,

    /**
     * 眼部样式（Ball）
     */
    val ballShape: BallShape = BallShape.DEFAULT,
    val ballColor: Color = Color.Black,
    val ballCornerRadius: Float = 0f,
    val ballGradientColors: List<Color> = listOf(Color.Black),
    val ballGradientAngle: Float = 0f,
    val isBallGradient: Boolean = false,

    /**
     * 眼部边框样式（Eye Frame）
     */
    val frameShape: FrameShape = FrameShape.DEFAULT,
    val frameColor: Color = Color.Black,
    val frameWidth: Float = 0f,
    val frameCornerRadius: Float = 0f,

    /**
     * 像素样式（Dark Pixel）
     */
    val pixelShape: PixelShape = PixelShape.DEFAULT,
    val pixelCornerRadius: Float = 0f,
    val pixelGradientColors: List<Color> = listOf(Color.Black),
    val pixelGradientAngle: Float = 0f,
    val isPixelGradient: Boolean = false,

    /**
     * Logo设置
     */
    val logoBitmap: Bitmap? = null,
    val logoSize: Float = 0.2f,
    val logoPadding: Float = 0.1f,
    val logoShape: LogoShape = LogoShape.SQUARE,
    val logoCornerRadius: Float = 0f,
    val logoBackgroundColor: Color? = null,

    /**
     * 边框设置（外部边框）
     */
    val outerBorderColor: Color = Color.Black,
    val outerBorderWidth: Float = 0f,
    val outerBorderCornerRadius: Float = 0f,

    /**
     * 高级设置
     */
    val isInverted: Boolean = false,
    val transparency: Float = 1f
) {
    companion object {
        val DEFAULT = QRoseStyleConfig()

        val BUSINESS = QRoseStyleConfig(
            darkPixelColor = Color(0xFF0D47A1),
            lightPixelColor = Color.White,
            backgroundColor = Color.White,
            ballColor = Color(0xFF0D47A1),
            ballShape = BallShape.DEFAULT,
            frameShape = FrameShape.ROUNDED_CORNERS,
            pixelShape = PixelShape.DEFAULT,
            errorCorrectionLevel = ErrorCorrectionLevel.MEDIUM
        )

        val ARTISTIC = QRoseStyleConfig(
            darkPixelColor = Color(0xFFC2185B),
            lightPixelColor = Color.White,
            backgroundColor = Color.White,
            ballColor = Color(0xFFC2185B),
            ballShape = BallShape.CIRCLE,
            frameShape = FrameShape.ROUNDED_CORNERS,
            pixelShape = PixelShape.DEFAULT,
            errorCorrectionLevel = ErrorCorrectionLevel.QUARTILE
        )

        val ELEGANT = QRoseStyleConfig(
            darkPixelColor = Color(0xFF212121),
            lightPixelColor = Color.White,
            backgroundColor = Color.White,
            ballColor = Color(0xFF212121),
            ballShape = BallShape.ROUNDED_CORNERS,
            frameShape = FrameShape.ROUNDED_CORNERS,
            pixelShape = PixelShape.ROUNDED_CORNERS,
            pixelCornerRadius = 0.3f,
            ballCornerRadius = 0.3f,
            errorCorrectionLevel = ErrorCorrectionLevel.MEDIUM
        )

        val VIBRANT = QRoseStyleConfig(
            darkPixelColor = Color(0xFFE64A19),
            lightPixelColor = Color.White,
            backgroundColor = Color.White,
            ballColor = Color(0xFFE64A19),
            ballShape = BallShape.CIRCLE,
            frameShape = FrameShape.ROUNDED_CORNERS,
            pixelShape = PixelShape.DEFAULT,
            errorCorrectionLevel = ErrorCorrectionLevel.QUARTILE
        )

        val MINIMAL = QRoseStyleConfig(
            darkPixelColor = Color.Black,
            lightPixelColor = Color.White,
            backgroundColor = Color.White,
            padding = 0.15f,
            ballShape = BallShape.DEFAULT,
            frameShape = FrameShape.DEFAULT,
            pixelShape = PixelShape.DEFAULT,
            errorCorrectionLevel = ErrorCorrectionLevel.MEDIUM
        )

        val RAINBOW = QRoseStyleConfig(
            darkPixelGradientColors = listOf(
                Color(0xFF1565C0),
                Color(0xFF2E7D32),
                Color(0xFF6A1B9A)
            ),
            darkPixelGradientAngle = 45f,
            isDarkPixelGradient = true,
            lightPixelColor = Color.White,
            backgroundColor = Color.White,
            ballShape = BallShape.DEFAULT,
            frameShape = FrameShape.ROUNDED_CORNERS,
            pixelShape = PixelShape.DEFAULT,
            errorCorrectionLevel = ErrorCorrectionLevel.HIGH
        )

        val NEON = QRoseStyleConfig(
            darkPixelColor = Color(0xFF00E676),
            lightPixelColor = Color.White,
            backgroundColor = Color.White,
            ballColor = Color(0xFF00E676),
            ballShape = BallShape.DEFAULT,
            frameShape = FrameShape.ROUNDED_CORNERS,
            pixelShape = PixelShape.DEFAULT,
            errorCorrectionLevel = ErrorCorrectionLevel.HIGH
        )
    }
}

/**
 * 眼部（Ball）形状枚举
 */
enum class BallShape {
    DEFAULT,
    CIRCLE,
    ROUNDED_CORNERS,
    VERTICAL,
    HORIZONTAL,
    ARC
}

/**
 * 框架（Frame）形状枚举
 */
enum class FrameShape {
    DEFAULT,
    ROUNDED_CORNERS,
    ROUNDED_VERTICAL,
    ROUNDED_HORIZONTAL
}

/**
 * 像素形状枚举
 */
enum class PixelShape {
    DEFAULT,
    CIRCLE,
    ROUNDED_CORNERS,
    ARC,
    VERTICAL,
    HORIZONTAL,
    DIAMOND
}

/**
 * 背景类型枚举
 */
enum class BackgroundType {
    SOLID,
    LINEAR_GRADIENT,
    RADIAL_GRADIENT,
    IMAGE,
    TRANSPARENT
}

/**
 * Logo形状枚举
 */
enum class LogoShape {
    SQUARE,
    CIRCLE,
    ROUNDED_CORNERS,
    DIAMOND
}

/**
 * 纠错级别枚举
 */
enum class ErrorCorrectionLevel {
    LOW,
    MEDIUM,
    QUARTILE,
    HIGH
}

/**
 * 自定义形状绘制器
 */
fun interface ShapeDrawer {
    fun DrawScope.drawShape(
        x: Float,
        y: Float,
        size: Float,
        color: Color,
        cornerRadius: Float
    )
}

/**
 * 样式预设
 */
sealed class StylePreset {
    data object Business : StylePreset()
    data object Artistic : StylePreset()
    data object Elegant : StylePreset()
    data object Vibrant : StylePreset()
    data object Minimal : StylePreset()
    data object Rainbow : StylePreset()
    data object Neon : StylePreset()
    data class Custom(val config: QRoseStyleConfig) : StylePreset()
}