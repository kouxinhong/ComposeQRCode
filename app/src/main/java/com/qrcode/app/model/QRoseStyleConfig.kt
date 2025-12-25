package com.qrcode.app.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import android.graphics.Color as AndroidColor

/**
 * QRose二维码样式配置
 * 包含所有可自定义的样式选项
 */
data class QRoseStyleConfig(
    // 基础设置
    val size: Int = 512,
    val padding: Float = 0.1f,
    val errorCorrectionLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.MEDIUM,
    
    // 像素形状
    val pixelShape: PixelShape = PixelShape.SQUARE,
    val pixelColor: Color = Color(AndroidColor.BLACK),
    val pixelCornerRadius: Float = 0.2f,
    
    // 眼部设置
    val eyeShape: EyeShape = EyeShape.SQUARE,
    val eyeColor: Color = Color(AndroidColor.BLACK),
    val eyeCornerRadius: Float = 0.2f,
    val eyeFrameColor: Color = Color(AndroidColor.BLACK),
    
    // 背景设置
    val backgroundColor: Color = Color(AndroidColor.WHITE),
    val backgroundType: BackgroundType = BackgroundType.SOLID,
    val gradientStartColor: Color = Color(AndroidColor.WHITE),
    val gradientEndColor: Color = Color(AndroidColor.WHITE),
    val gradientAngle: Float = 0f,
    
    // Logo设置
    val logoData: String? = null,
    val logoSize: Float = 0.2f,
    val logoPadding: Float = 0.05f,
    val logoCornerRadius: Float = 0.1f,
    val logoShape: LogoShape = LogoShape.SQUARE,
    
    // 高级设置
    val frameColor: Color = Color(AndroidColor.BLACK),
    val frameWidth: Float = 0.02f,
    val isInverted: Boolean = false,
    val transparency: Float = 1f
) {
    companion object {
        val DEFAULT = QRoseStyleConfig()
        
        val BUSINESS = QRoseStyleConfig(
            pixelShape = PixelShape.SQUARE,
            pixelColor = Color(0xFF1976D2),
            eyeColor = Color(0xFF1976D2),
            backgroundColor = Color(AndroidColor.WHITE)
        )
        
        val ARTISTIC = QRoseStyleConfig(
            pixelShape = PixelShape.CIRCLE,
            pixelColor = Color(0xFFE91E63),
            eyeShape = EyeShape.CIRCLE,
            eyeColor = Color(0xFFE91E63),
            backgroundType = BackgroundType.LINEAR_GRADIENT,
            gradientStartColor = Color(0xFFFCE4EC),
            gradientEndColor = Color(0xFFF8BBD9)
        )
        
        val ELEGANT = QRoseStyleConfig(
            pixelShape = PixelShape.ROUNDED,
            pixelColor = Color(0xFF424242),
            eyeShape = EyeShape.ROUNDED,
            eyeColor = Color(0xFF424242),
            backgroundColor = Color(0xFFF5F5F5),
            pixelCornerRadius = 0.4f,
            eyeCornerRadius = 0.4f
        )
        
        val VIBRANT = QRoseStyleConfig(
            pixelShape = PixelShape.CIRCLE,
            pixelColor = Color(0xFFFF5722),
            eyeShape = EyeShape.CIRCLE,
            eyeColor = Color(0xFFFF5722),
            backgroundType = BackgroundType.RADIAL_GRADIENT,
            gradientStartColor = Color(0xFFFFF3E0),
            gradientEndColor = Color(0xFFFFE0B2)
        )
        
        val MINIMAL = QRoseStyleConfig(
            pixelShape = PixelShape.SQUARE,
            pixelColor = Color(AndroidColor.BLACK),
            eyeColor = Color(AndroidColor.BLACK),
            backgroundColor = Color(AndroidColor.WHITE),
            padding = 0.15f
        )
    }
}

enum class PixelShape {
    SQUARE,     // 方形
    CIRCLE,     // 圆形
    ROUNDED,    // 圆角
    DIAMOND,    // 菱形
    STAR,       // 星形
    HEART,      // 心形
    CUSTOM      // 自定义
}

enum class EyeShape {
    SQUARE,     // 方形
    CIRCLE,     // 圆形
    ROUNDED,    // 圆角
    DIAMOND,    // 菱形
    CUSTOM      // 自定义
}

enum class BackgroundType {
    SOLID,              // 纯色
    LINEAR_GRADIENT,    // 线性渐变
    RADIAL_GRADIENT,    // 径向渐变
    SWEEP_GRADIENT,     // 扫描渐变
    IMAGE,              // 图片背景
    TRANSPARENT         // 透明
}

enum class LogoShape {
    SQUARE,     // 方形
    CIRCLE,     // 圆形
    ROUNDED,    // 圆角
    DIAMOND,    // 菱形
    CUSTOM      // 自定义
}

enum class ErrorCorrectionLevel {
    LOW,        // 低 (7%)
    MEDIUM,     // 中 (15%)
    QUARTILE,   // 高 (25%)
    HIGH        // 极高 (30%)
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
    data class Custom(val config: QRoseStyleConfig) : StylePreset()
}