package com.qrcode.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.qrcode.app.utils.QRBackgroundType

/**
 * 背景选择器组件
 */
@Composable
fun BackgroundSelector(
    currentBackgroundType: QRBackgroundType,
    currentBackgroundColor: Color,
    currentGradientStartColor: Color,
    currentGradientEndColor: Color,
    onBackgroundChanged: (QRBackgroundType, Color, Color, Color) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Palette,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "背景设置",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = getBackgroundTypeName(currentBackgroundType),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // 背景预览
            BackgroundPreview(
                backgroundType = currentBackgroundType,
                backgroundColor = currentBackgroundColor,
                gradientStartColor = currentGradientStartColor,
                gradientEndColor = currentGradientEndColor
            )
        }
    }
    
    if (showDialog) {
        BackgroundSelectionDialog(
            currentBackgroundType = currentBackgroundType,
            currentBackgroundColor = currentBackgroundColor,
            currentGradientStartColor = currentGradientStartColor,
            currentGradientEndColor = currentGradientEndColor,
            onDismiss = { showDialog = false },
            onConfirm = { type, bgColor, startColor, endColor ->
                onBackgroundChanged(type, bgColor, startColor, endColor)
                showDialog = false
            }
        )
    }
}

/**
 * 背景选择对话框
 */
@Composable
private fun BackgroundSelectionDialog(
    currentBackgroundType: QRBackgroundType,
    currentBackgroundColor: Color,
    currentGradientStartColor: Color,
    currentGradientEndColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (QRBackgroundType, Color, Color, Color) -> Unit
) {
    var selectedType by remember { mutableStateOf(currentBackgroundType) }
    var selectedColor by remember { mutableStateOf(currentBackgroundColor) }
    var selectedStartColor by remember { mutableStateOf(currentGradientStartColor) }
    var selectedEndColor by remember { mutableStateOf(currentGradientEndColor) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // 标题
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Palette,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "选择背景",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // 背景类型选择
                Text(
                    text = "背景类型",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BackgroundTypeItem(
                        type = QRBackgroundType.SOLID,
                        selectedType = selectedType,
                        onSelect = { selectedType = it }
                    )
                    BackgroundTypeItem(
                        type = QRBackgroundType.LINEAR_GRADIENT,
                        selectedType = selectedType,
                        onSelect = { selectedType = it }
                    )
                    BackgroundTypeItem(
                        type = QRBackgroundType.RADIAL_GRADIENT,
                        selectedType = selectedType,
                        onSelect = { selectedType = it }
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // 颜色选择
                when (selectedType) {
                    QRBackgroundType.SOLID -> {
                        ColorPickerItem(
                            title = "背景颜色",
                            selectedColor = selectedColor,
                            onColorSelected = { selectedColor = it }
                        )
                    }
                    QRBackgroundType.LINEAR_GRADIENT, QRBackgroundType.RADIAL_GRADIENT -> {
                        ColorPickerItem(
                            title = "起始颜色",
                            selectedColor = selectedStartColor,
                            onColorSelected = { selectedStartColor = it }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ColorPickerItem(
                            title = "结束颜色",
                            selectedColor = selectedEndColor,
                            onColorSelected = { selectedEndColor = it }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 预览
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        BackgroundPreview(
                            backgroundType = selectedType,
                            backgroundColor = selectedColor,
                            gradientStartColor = selectedStartColor,
                            gradientEndColor = selectedEndColor,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // 按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("取消")
                    }
                    Button(
                        onClick = {
                            onConfirm(selectedType, selectedColor, selectedStartColor, selectedEndColor)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("确定")
                    }
                }
            }
        }
    }
}

/**
 * 背景类型选择项
 */
@Composable
private fun BackgroundTypeItem(
    type: QRBackgroundType,
    selectedType: QRBackgroundType,
    onSelect: (QRBackgroundType) -> Unit
) {
    val isSelected = type == selectedType
    
    Card(
        modifier = Modifier
            .size(80.dp, 60.dp)
            .clickable { onSelect(type) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder(true).copy(
                width = 2.dp,
                brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary)
            )
        } else {
            CardDefaults.outlinedCardBorder(true).copy(
                width = 1.dp,
                brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                BackgroundPreview(
                    backgroundType = type,
                    backgroundColor = Color.White,
                    gradientStartColor = Color(0xFF1976D2),
                    gradientEndColor = Color(0xFF42A5F5),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = getBackgroundTypeShortName(type),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}

/**
 * 颜色选择器项
 */
@Composable
private fun ColorPickerItem(
    title: String,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    var showColorPicker by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(selectedColor)
                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .clickable { showColorPicker = true }
        )
    }
    
    if (showColorPicker) {
        ColorPickerDialog(
            currentColor = selectedColor,
            onColorSelected = onColorSelected,
            onDismiss = { showColorPicker = false }
        )
    }
}

/**
 * 背景预览组件
 */
@Composable
private fun BackgroundPreview(
    backgroundType: QRBackgroundType,
    backgroundColor: Color,
    gradientStartColor: Color,
    gradientEndColor: Color,
    modifier: Modifier = Modifier
) {
    when (backgroundType) {
        QRBackgroundType.SOLID -> {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
            )
        }
        QRBackgroundType.LINEAR_GRADIENT -> {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(gradientStartColor, gradientEndColor)
                        )
                    )
            )
        }
        QRBackgroundType.RADIAL_GRADIENT -> {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(gradientStartColor, gradientEndColor)
                        )
                    )
            )
        }
    }
}

/**
 * 颜色选择对话框
 */
@Composable
private fun ColorPickerDialog(
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val predefinedColors = listOf(
        Color.White,
        Color(0xFFF5F5F5),
        Color(0xFF1976D2),
        Color(0xFF2196F3),
        Color(0xFF03A9F4),
        Color(0xFF00BCD4),
        Color(0xFF009688),
        Color(0xFF4CAF50),
        Color(0xFF8BC34A),
        Color(0xFFCDDC39),
        Color(0xFFFFEB3B),
        Color(0xFFFFC107),
        Color(0xFFFF9800),
        Color(0xFFFF5722),
        Color(0xFF795548),
        Color(0xFF9E9E9E),
        Color(0xFF607D8B),
        Color(0xFF000000)
    )
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "选择颜色",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(predefinedColors.size) { index ->
                        val color = predefinedColors[index]
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (color == currentColor) 3.dp else 1.dp,
                                    color = if (color == currentColor) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.outline
                                    },
                                    shape = CircleShape
                                )
                                .clickable {
                                    onColorSelected(color)
                                    onDismiss()
                                }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }
                }
            }
        }
    }
}

/**
 * 获取背景类型名称
 */
private fun getBackgroundTypeName(type: QRBackgroundType): String {
    return when (type) {
        QRBackgroundType.SOLID -> "纯色"
        QRBackgroundType.LINEAR_GRADIENT -> "线性渐变"
        QRBackgroundType.RADIAL_GRADIENT -> "径向渐变"
    }
}

/**
 * 获取背景类型简称
 */
private fun getBackgroundTypeShortName(type: QRBackgroundType): String {
    return when (type) {
        QRBackgroundType.SOLID -> "纯色"
        QRBackgroundType.LINEAR_GRADIENT -> "线渐变"
        QRBackgroundType.RADIAL_GRADIENT -> "径渐变"
    }
}