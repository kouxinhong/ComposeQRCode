package com.qrcode.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * 扫描框指示器组件
 *
 * 该组件显示一个透明的正方形扫描框，具有上下扫描的动画效果。
 * 扫描框由边框和移动的扫描线组成，用户可以通过点击关闭。
 *
 * @param isScanning 是否显示扫描框
 * @param onDismiss 关闭扫描框的回调
 */
@Composable
fun ScanningIndicator(
    isScanning: Boolean,
    onDismiss: () -> Unit
) {
    if (isScanning) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            ScanningBox()
        }
    }
}

/**
 * 扫描框绘制组件
 *
 * 绘制一个带有动画扫描线的正方形扫描框。
 */
@Composable
private fun ScanningBox() {
    val infiniteTransition = rememberInfiniteTransition(label = "scanning")
    
    // 扫描线位置动画：从顶部移动到底部
    val scanLinePosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scanLine"
    )

    // 扫描框大小
    val scanBoxSize = 240.dp

    Box(
        modifier = Modifier.size(scanBoxSize)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 4.dp.toPx()
            
            // 绘制扫描框边框
            drawRect(
                color = Color.White,
                size = size,
                style = Stroke(width = strokeWidth)
            )
            
            // 绘制四个角的延长线
            val cornerLength = 20.dp.toPx()
            
            // 左上角
            drawLine(
                color = Color.White,
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(cornerLength, 0f),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(0f, cornerLength),
                strokeWidth = strokeWidth
            )
            
            // 右上角
            drawLine(
                color = Color.White,
                start = androidx.compose.ui.geometry.Offset(size.width, 0f),
                end = androidx.compose.ui.geometry.Offset(size.width - cornerLength, 0f),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = androidx.compose.ui.geometry.Offset(size.width, 0f),
                end = androidx.compose.ui.geometry.Offset(size.width, cornerLength),
                strokeWidth = strokeWidth
            )
            
            // 左下角
            drawLine(
                color = Color.White,
                start = androidx.compose.ui.geometry.Offset(0f, size.height),
                end = androidx.compose.ui.geometry.Offset(cornerLength, size.height),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = androidx.compose.ui.geometry.Offset(0f, size.height),
                end = androidx.compose.ui.geometry.Offset(0f, size.height - cornerLength),
                strokeWidth = strokeWidth
            )
            
            // 右下角
            drawLine(
                color = Color.White,
                start = androidx.compose.ui.geometry.Offset(size.width, size.height),
                end = androidx.compose.ui.geometry.Offset(size.width - cornerLength, size.height),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = androidx.compose.ui.geometry.Offset(size.width, size.height),
                end = androidx.compose.ui.geometry.Offset(size.width, size.height - cornerLength),
                strokeWidth = strokeWidth
            )
            
            // 绘制扫描线
            val scanLineY = size.height * scanLinePosition
            drawLine(
                color = Color.Green,
                start = androidx.compose.ui.geometry.Offset(0f, scanLineY),
                end = androidx.compose.ui.geometry.Offset(size.width, scanLineY),
                strokeWidth = strokeWidth
            )
        }
    }
}