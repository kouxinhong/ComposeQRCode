package com.qrcode.scanner.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 扫描动画组件
 */
@Composable
fun ScanLineAnimation(
    modifier: Modifier = Modifier,
    color: Color = Color.Green,
    width: Dp = 200.dp,
    height: Dp = 200.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_animation")
    
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scan_progress"
    )

    Canvas(
        modifier = modifier.size(width, height)
    ) {
        val strokeWidth = 2.dp.toPx()
        
        // 绘制扫描框
        drawRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(width.toPx(), height.toPx()),
            style = Stroke(width = strokeWidth)
        )
        
        // 绘制四个角的装饰
        val cornerLength = 20.dp.toPx()
        val cornerWidth = 4.dp.toPx()
        
        // 左上角
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(cornerLength, 0f),
            strokeWidth = cornerWidth
        )
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(0f, cornerLength),
            strokeWidth = cornerWidth
        )
        
        // 右上角
        drawLine(
            color = color,
            start = Offset(width.toPx() - cornerLength, 0f),
            end = Offset(width.toPx(), 0f),
            strokeWidth = cornerWidth
        )
        drawLine(
            color = color,
            start = Offset(width.toPx(), 0f),
            end = Offset(width.toPx(), cornerLength),
            strokeWidth = cornerWidth
        )
        
        // 左下角
        drawLine(
            color = color,
            start = Offset(0f, height.toPx() - cornerLength),
            end = Offset(0f, height.toPx()),
            strokeWidth = cornerWidth
        )
        drawLine(
            color = color,
            start = Offset(0f, height.toPx()),
            end = Offset(cornerLength, height.toPx()),
            strokeWidth = cornerWidth
        )
        
        // 右下角
        drawLine(
            color = color,
            start = Offset(width.toPx() - cornerLength, height.toPx()),
            end = Offset(width.toPx(), height.toPx()),
            strokeWidth = cornerWidth
        )
        drawLine(
            color = color,
            start = Offset(width.toPx(), height.toPx() - cornerLength),
            end = Offset(width.toPx(), height.toPx()),
            strokeWidth = cornerWidth
        )
        
        // 绘制扫描线
        val scanLineY = height.toPx() * animatedProgress
        drawLine(
            color = color,
            start = Offset(0f, scanLineY),
            end = Offset(width.toPx(), scanLineY),
            strokeWidth = strokeWidth
        )
    }
}

/**
 * 扫描遮罩层
 */
@Composable
fun ScanMask(
    modifier: Modifier = Modifier,
    color: Color = Color.Black.copy(alpha = 0.5f)
) {
    Canvas(
        modifier = modifier
    ) {
        // 绘制半透明遮罩
        drawRect(
            color = color,
            topLeft = Offset.Zero,
            size = size
        )
    }
}