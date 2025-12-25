package com.qrcode.app.utils

import androidx.compose.ui.graphics.Color

/**
 * 二维码配置数据类
 * 包含二维码的各种样式和属性配置
 */
data class QRCodeConfig(
    /** 二维码样式 */
    val style: QRCodeStyle = QRCodeStyle.BASIC,
    
    /** 像素形状 */
    val pixelShape: QRPixelShape = QRPixelShape.SQUARE,
    
    /** 眼部形状 */
    val eyeShape: QREyeShape = QREyeShape.SQUARE,
    
    /** 背景类型 */
    val backgroundType: QRBackgroundType = QRBackgroundType.SOLID,
    
    /** 前景色 */
    val foregroundColor: Color = Color.Black,
    
    /** 背景色 */
    val backgroundColor: Color = Color.White,
    
    /** 眼部颜色 */
    val eyeColor: Color = Color.Black,
    
    /** 渐变起始色 */
    val gradientStartColor: Color = Color.Black,
    
    /** 渐变结束色 */
    val gradientEndColor: Color = Color.Blue,
    
    /** 内边距 */
    val padding: Int = 10,
    
    /** 尺寸 */
    val size: Int = 512,
    
    /** 容错级别 */
    val errorCorrectionLevel: Int = 3, // 0-3，3表示最高
    
    /** 是否包含Logo */
    val hasLogo: Boolean = false,
    
    /** Logo路径 */
    val logoPath: String? = null,
    
    /** Logo大小比例 (0-1) */
    val logoSizeRatio: Float = 0.2f,
    
    /** 像素圆角程度 (0-1) */
    val pixelCornerRadius: Float = 0f,
    
    /** 眼部圆角程度 (0-1) */
    val eyeCornerRadius: Float = 0f
)