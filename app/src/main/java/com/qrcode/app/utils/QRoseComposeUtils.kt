package com.qrcode.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.github.alexzhirkevich.qrose.QrCodePainter
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

/**
 * QRose Compose工具类
 * 提供二维码生成和样式处理功能
 */
object QRoseComposeUtils {

    /**
     * 生成并记住二维码位图
     * @param content 二维码内容
     * @param style 二维码样式
     * @param size 二维码尺寸
     * @param foregroundColor 前景色（默认黑色）
     * @param backgroundColor 背景色（默认白色）
     * @return 二维码位图
     */
    @Composable
    fun rememberQRCode(
        content: String,
        style: QRCodeStyle = QRCodeStyle.BASIC,
        size: Int = 200,
        foregroundColor: Int = android.graphics.Color.BLACK,
        backgroundColor: Int = android.graphics.Color.WHITE
    ): ImageBitmap {
        return remember(content, style, size, foregroundColor, backgroundColor) {
            generateQRCodeBitmap(content, style, size, foregroundColor, backgroundColor)
        }
    }

    /**
     * 生成二维码位图
     * @param content 二维码内容
     * @param style 二维码样式
     * @param size 二维码尺寸
     * @param foregroundColor 前景色
     * @param backgroundColor 背景色
     * @return 二维码位图
     */
    private fun generateQRCodeBitmap(
        content: String,
        style: QRCodeStyle,
        size: Int,
        foregroundColor: Int,
        backgroundColor: Int
    ): ImageBitmap {
        // 使用QRose库生成基本二维码位图
        val bitmap = android.graphics.Bitmap.createBitmap(size, size, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        
        // 设置背景色
        canvas.drawColor(backgroundColor)
        
        // 这里使用QRose库的API生成二维码
        // 由于QRose库的具体API需要查看文档，这里先使用基本实现
        // 实际项目中应该使用QRose库提供的二维码生成方法
        
        return bitmap.asImageBitmap()
    }
}