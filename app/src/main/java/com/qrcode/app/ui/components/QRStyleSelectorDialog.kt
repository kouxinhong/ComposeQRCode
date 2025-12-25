package com.qrcode.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 二维码样式选择器的数据类
 */
data class QRStyleOption(
    val name: String,
    val description: String,
    val style: QRStyle,
    val previewColor: Color
)

/**
 * 二维码样式选择对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRStyleSelectorDialog(
    currentStyle: QRStyle,
    onStyleSelected: (QRStyle) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStyle by remember { mutableStateOf(currentStyle) }
    
    val styleOptions = listOf(
        QRStyleOption(
            name = "经典",
            description = "传统的黑白二维码",
            style = QRStyle.Default,
            previewColor = Color.Black
        ),
        QRStyleOption(
            name = "圆角",
            description = "圆角矩形像素样式",
            style = QRStyle.Rounded(),
            previewColor = Color(0xFF1976D2)
        ),
        QRStyleOption(
            name = "圆形",
            description = "圆形像素样式",
            style = QRStyle.Circular,
            previewColor = Color(0xFF4CAF50)
        ),
        QRStyleOption(
            name = "渐变蓝",
            description = "蓝色渐变效果",
            style = QRStyle.Gradient(
                startColor = Color(0xFF1976D2),
                endColor = Color(0xFF42A5F5)
            ),
            previewColor = Color(0xFF1976D2)
        ),
        QRStyleOption(
            name = "渐变绿",
            description = "绿色渐变效果",
            style = QRStyle.Gradient(
                startColor = Color(0xFF4CAF50),
                endColor = Color(0xFF8BC34A)
            ),
            previewColor = Color(0xFF4CAF50)
        ),
        QRStyleOption(
            name = "渐变红",
            description = "红色渐变效果",
            style = QRStyle.Gradient(
                startColor = Color(0xFFE91E63),
                endColor = Color(0xFFF06292)
            ),
            previewColor = Color(0xFFE91E63)
        ),
        QRStyleOption(
            name = "渐变紫",
            description = "紫色渐变效果",
            style = QRStyle.Gradient(
                startColor = Color(0xFF9C27B0),
                endColor = Color(0xFFBA68C8)
            ),
            previewColor = Color(0xFF9C27B0)
        )
    )

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        content = {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // 标题栏
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "选择二维码样式",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "关闭"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 样式选项网格
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(styleOptions) { option ->
                            StyleOptionCard(
                                option = option,
                                isSelected = selectedStyle::class == option.style::class,
                                onClick = { selectedStyle = option.style }
                            )
                        }
                    }

                    // 圆角样式自定义（如果选中）
                    if (selectedStyle is QRStyle.Rounded) {
                        Spacer(modifier = Modifier.height(16.dp))
                        RoundedStyleCustomizer(
                            currentRadius = (selectedStyle as QRStyle.Rounded).radius,
                            onRadiusChanged = { radius ->
                                selectedStyle = QRStyle.Rounded(radius)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 确认按钮
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                onStyleSelected(selectedStyle)
                                onDismiss()
                            }
                        ) {
                            Text("应用样式")
                        }
                    }
                }
            }
        }
    )
}

/**
 * 单个样式选项卡片
 */
@Composable
private fun StyleOptionCard(
    option: QRStyleOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            ),
            modifier = modifier
                .height(80.dp)
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 预览图标
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(option.previewColor)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = option.name,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

/**
 * 圆角样式自定义滑块
 */
@Composable
private fun RoundedStyleCustomizer(
    currentRadius: Float,
    onRadiusChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "圆角程度: ${(currentRadius * 100).toInt()}%",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Slider(
            value = currentRadius,
            onValueChange = onRadiusChanged,
            valueRange = 0.1f..0.9f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}