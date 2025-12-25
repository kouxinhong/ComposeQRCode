package com.qrcode.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.qrcode.app.utils.QRoseComposeUtils
import com.qrcode.app.utils.QRCodeStyle

/**
 * 二维码样式枚举
 */
sealed class QRStyle {
    data object Default : QRStyle()
    data class Rounded(val radius: Float = 0.5f) : QRStyle()
    data object Circular : QRStyle()
    data class Gradient(val startColor: androidx.compose.ui.graphics.Color, val endColor: androidx.compose.ui.graphics.Color) : QRStyle()
}

/**
 * 功能完整的二维码生成器组件
 * 支持多种样式和颜色配置
 */
@Composable
fun QRCodeGenerator(
    data: String,
    size: Int = 200,
    style: QRStyle = QRStyle.Default,
    onSaveClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val qrStyle = when (style) {
        is QRStyle.Default -> QRCodeStyle.BASIC
        is QRStyle.Rounded -> QRCodeStyle.ROUNDED
        is QRStyle.Circular -> QRCodeStyle.CIRCLE
        is QRStyle.Gradient -> QRCodeStyle.GRADIENT
    }

    val foregroundColor = when (style) {
        is QRStyle.Gradient -> android.graphics.Color.rgb(
            (style.startColor.red * 255).toInt(),
            (style.startColor.green * 255).toInt(),
            (style.startColor.blue * 255).toInt()
        )
        else -> android.graphics.Color.BLACK
    }

    val backgroundColor = when (style) {
        is QRStyle.Gradient -> android.graphics.Color.rgb(
            (style.endColor.red * 255).toInt(),
            (style.endColor.green * 255).toInt(),
            (style.endColor.blue * 255).toInt()
        )
        else -> android.graphics.Color.WHITE
    }

    val bitmap = QRoseComposeUtils.rememberQRCode(
        content = data,
        style = qrStyle,
        size = size,
        foregroundColor = foregroundColor,
        backgroundColor = backgroundColor
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                bitmap = bitmap,
                contentDescription = "二维码",
                modifier = Modifier.size(size.dp)
            )
            
            if (onSaveClick != null || onShareClick != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    onSaveClick?.let {
                        Button(onClick = it) {
                            Text("保存")
                        }
                    }
                    onShareClick?.let {
                        Button(onClick = it) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "分享",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Text("分享")
                        }
                    }
                }
            }
        }
    }
}



/**
 * 简化版本的二维码生成器
 * 仅显示基础二维码，无样式配置
 */
@Composable
fun SimpleQRCodeGenerator(
    data: String,
    size: Int = 200,
    modifier: Modifier = Modifier
) {
    val bitmap = QRoseComposeUtils.rememberQRCode(
        content = data,
        style = QRCodeStyle.BASIC,
        size = size
    )
    
    Image(
        bitmap = bitmap,
        contentDescription = "二维码",
        modifier = modifier.size(size.dp)
    )
}