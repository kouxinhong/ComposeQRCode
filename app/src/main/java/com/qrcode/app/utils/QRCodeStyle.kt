package com.qrcode.app.utils

/**
 * 二维码样式相关枚举定义
 */

/**
 * 二维码样式枚举 - 基于 custom-qr-generator 的所有可用形状
 */
enum class QRCodeStyle {
    /** 基础样式 - 方形像素 */
    BASIC,
    
    /** 圆形像素样式 */
    CIRCLE,
    
    /** 圆角样式 */
    ROUNDED,
    
    /** 渐变样式 */
    GRADIENT,
    
    /** 艺术样式 */
    ARTISTIC,
    
    /** 点阵样式 */
    DOT,
    
    /** 菱形样式 */
    RHOMBUS,
    
    /** 星形样式 */
    STAR,
    
    /** 垂直圆角样式 */
    ROUNDED_VERTICAL,
    
    /** 水平圆角样式 */
    ROUNDED_HORIZONTAL,
    
    /** 圆形框架样式 */
    CIRCLE_FRAME,
    
    /** 圆角框架样式 */
    ROUNDED_FRAME,
    
    /** 圆形眼球样式 */
    CIRCLE_BALL,
    
    /** 圆角眼球样式 */
    ROUNDED_BALL,
    
    /** 菱形眼球样式 */
    RHOMBUS_BALL
}

/**
 * 二维码像素形状枚举
 */
enum class QRPixelShape {
    /** 方形 */
    SQUARE,
    
    /** 圆形 */
    CIRCLE,
    
    /** 圆角 */
    ROUNDED_CORNERS
}

/**
 * 二维码眼部形状枚举
 */
enum class QREyeShape {
    /** 方形 */
    SQUARE,
    
    /** 圆形 */
    CIRCLE,
    
    /** 圆角 */
    ROUNDED_CORNERS
}

/**
 * 二维码背景类型枚举
 */
enum class QRBackgroundType {
    /** 纯色背景 */
    SOLID,
    
    /** 线性渐变背景 */
    LINEAR_GRADIENT,
    
    /** 径向渐变背景 */
    RADIAL_GRADIENT
}

/**
 * 二维码眼部样式枚举
 */
enum class QREyeStyle {
    /** 方形 */
    SQUARE,
    
    /** 圆形 */
    CIRCLE,
    
    /** 圆角 */
    ROUNDED
}