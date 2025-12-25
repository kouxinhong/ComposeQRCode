package com.qrcode.generator.theme

import androidx.compose.ui.graphics.Color

/**
 * 二维码生成库主题定义
 * 提供预定义的颜色方案
 */
object QRGeneratorTheme {

    /**
     * 商务风格颜色
     */
    object Business {
        val Primary = Color(0xFF1976D2)
        val Secondary = Color(0xFF424242)
        val Background = Color(0xFFFFFFFF)
        val Foreground = Color(0xFF000000)
    }

    /**
     * 艺术风格颜色
     */
    object Artistic {
        val Primary = Color(0xFFE91E63)
        val Secondary = Color(0xFF9C27B0)
        val BackgroundStart = Color(0xFFFCE4EC)
        val BackgroundEnd = Color(0xFFF8BBD9)
    }

    /**
     * 优雅风格颜色
     */
    object Elegant {
        val Primary = Color(0xFF424242)
        val Secondary = Color(0xFF757575)
        val Background = Color(0xFFF5F5F5)
    }

    /**
     * 活力风格颜色
     */
    object Vibrant {
        val Primary = Color(0xFFFF5722)
        val Secondary = Color(0xFFFF9800)
        val BackgroundStart = Color(0xFFFFF3E0)
        val BackgroundEnd = Color(0xFFFFE0B2)
    }

    /**
     * 极简风格颜色
     */
    object Minimal {
        val Primary = Color(0xFF000000)
        val Background = Color(0xFFFFFFFF)
    }

    /**
     * 节日风格颜色
     */
    object Christmas {
        val Primary = Color(0xFFD32F2F)
        val Secondary = Color(0xFF1B5E20)
        val Background = Color(0xFFFFFFFF)
    }
}
