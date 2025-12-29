package com.qrcode.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qrcode.app.utils.QRCodeStyle

/**
 * 简化的二维码样式卡片组件
 * 只保留边框颜色效果
 */
@Composable
fun AnimatedStyleCard(
    name: String,
    style: QRCodeStyle,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 边框颜色 - 仅根据选中状态变化
    val borderColor = if (isSelected) {
        when (style) {
            QRCodeStyle.BASIC -> Color(0xFF000000)
            QRCodeStyle.CIRCLE -> Color(0xFF1976D2)
            QRCodeStyle.ROUNDED -> Color(0xFF424242)
            QRCodeStyle.GRADIENT -> Color(0xFF9C27B0)
            QRCodeStyle.ARTISTIC -> Color(0xFFE91E63)
            QRCodeStyle.DOT -> Color(0xFF00BCD4)
            QRCodeStyle.RHOMBUS -> Color(0xFF4CAF50)
            QRCodeStyle.STAR -> Color(0xFFFFC107)
            QRCodeStyle.ROUNDED_VERTICAL -> Color(0xFF607D8B)
            QRCodeStyle.ROUNDED_HORIZONTAL -> Color(0xFF795548)
            QRCodeStyle.CIRCLE_FRAME -> Color(0xFF2196F3)
            QRCodeStyle.ROUNDED_FRAME -> Color(0xFF607D8B)
            QRCodeStyle.CIRCLE_BALL -> Color(0xFFF44336)
            QRCodeStyle.ROUNDED_BALL -> Color(0xFF9E9E9E)
            QRCodeStyle.RHOMBUS_BALL -> Color(0xFF4CAF50)
        }
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }
    
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 2.dp,
            color = borderColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 样式预览图标 - 静态
                StylePreviewIcon(style = style)
                
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}

/**
 * 样式预览图标组件 - 静态版本
 */
@Composable
private fun StylePreviewIcon(style: QRCodeStyle) {
    val colors = when (style) {
        QRCodeStyle.BASIC -> Color(0xFF000000)
        QRCodeStyle.CIRCLE -> Color(0xFF1976D2)
        QRCodeStyle.ROUNDED -> Color(0xFF424242)
        QRCodeStyle.GRADIENT -> Color(0xFF9C27B0)
        QRCodeStyle.ARTISTIC -> Color(0xFFE91E63)
        QRCodeStyle.DOT -> Color(0xFF00BCD4)
        QRCodeStyle.RHOMBUS -> Color(0xFF4CAF50)
        QRCodeStyle.STAR -> Color(0xFFFFC107)
        QRCodeStyle.ROUNDED_VERTICAL -> Color(0xFF607D8B)
        QRCodeStyle.ROUNDED_HORIZONTAL -> Color(0xFF795548)
        QRCodeStyle.CIRCLE_FRAME -> Color(0xFF2196F3)
        QRCodeStyle.ROUNDED_FRAME -> Color(0xFF607D8B)
        QRCodeStyle.CIRCLE_BALL -> Color(0xFFF44336)
        QRCodeStyle.ROUNDED_BALL -> Color(0xFF9E9E9E)
        QRCodeStyle.RHOMBUS_BALL -> Color(0xFF4CAF50)
    }
    
    Card(
        modifier = Modifier.size(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = when (style) {
            QRCodeStyle.CIRCLE, QRCodeStyle.CIRCLE_BALL, QRCodeStyle.CIRCLE_FRAME -> CircleShape
            QRCodeStyle.ROUNDED, QRCodeStyle.ROUNDED_BALL, QRCodeStyle.ROUNDED_FRAME -> MaterialTheme.shapes.medium
            QRCodeStyle.STAR -> MaterialTheme.shapes.small
            else -> MaterialTheme.shapes.small
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        color = colors,
                        shape = when (style) {
                            QRCodeStyle.CIRCLE, QRCodeStyle.CIRCLE_BALL, QRCodeStyle.CIRCLE_FRAME -> CircleShape
                            QRCodeStyle.RHOMBUS, QRCodeStyle.RHOMBUS_BALL -> MaterialTheme.shapes.small
                            QRCodeStyle.STAR -> MaterialTheme.shapes.extraSmall
                            else -> MaterialTheme.shapes.extraSmall
                        }
                    )
            )
        }
    }
}