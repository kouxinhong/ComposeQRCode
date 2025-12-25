package com.qrcode.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.qrcode.app.R
import com.qrcode.app.utils.*

/**
 * QRCode样式自定义对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRStyleCustomizationDialog(
    isVisible: Boolean,
    currentConfig: QRCodeConfig,
    onConfigChanged: (QRCodeConfig) -> Unit,
    onDismiss: () -> Unit,
    onApply: (QRCodeConfig) -> Unit
) {
    if (!isVisible) return
    
    var config by remember(currentConfig) { mutableStateOf(currentConfig) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // 标题栏
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.qr_style_settings),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
                    }
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                // 滚动内容
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // 像素形状设置
                    PixelShapeSection(
                        selectedShape = config.pixelShape,
                        onShapeSelected = { config = config.copy(pixelShape = it) }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 眼部形状设置  
                    EyeShapeSection(
                        selectedShape = config.eyeShape,
                        onShapeSelected = { config = config.copy(eyeShape = it) }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 背景类型设置
                    BackgroundTypeSection(
                        selectedType = config.backgroundType,
                        onTypeSelected = { config = config.copy(backgroundType = it) }
                    )
                }
                
                // 底部按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { config = QRCodeConfig() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.reset))
                    }
                    
                    Button(
                        onClick = { onApply(config) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.apply))
                    }
                }
            }
        }
    }
    
    // 实时更新配置
    LaunchedEffect(config) {
        onConfigChanged(config)
    }
}

@Composable
private fun PixelShapeSection(
    selectedShape: QRPixelShape,
    onShapeSelected: (QRPixelShape) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.pixel_shape),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Column(modifier = Modifier.selectableGroup()) {
            QRPixelShape.values().forEach { shape ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedShape == shape,
                            onClick = { onShapeSelected(shape) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedShape == shape,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = getPixelShapeDisplayName(shape),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun EyeShapeSection(
    selectedShape: QREyeShape,
    onShapeSelected: (QREyeShape) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.eye_shape),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Column(modifier = Modifier.selectableGroup()) {
            QREyeShape.values().forEach { shape ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedShape == shape,
                            onClick = { onShapeSelected(shape) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedShape == shape,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = getEyeShapeDisplayName(shape),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun BackgroundTypeSection(
    selectedType: QRBackgroundType,
    onTypeSelected: (QRBackgroundType) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.background_type),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Column(modifier = Modifier.selectableGroup()) {
            QRBackgroundType.values().forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedType == type,
                            onClick = { onTypeSelected(type) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedType == type,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = getBackgroundTypeDisplayName(type),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// 辅助函数
private fun getPixelShapeDisplayName(shape: QRPixelShape): String {
    return when (shape) {
        QRPixelShape.SQUARE -> "方形"
        QRPixelShape.CIRCLE -> "圆形"
        QRPixelShape.ROUNDED_CORNERS -> "圆角"
    }
}

private fun getEyeShapeDisplayName(shape: QREyeShape): String {
    return when (shape) {
        QREyeShape.SQUARE -> "方形"
        QREyeShape.CIRCLE -> "圆形"
        QREyeShape.ROUNDED_CORNERS -> "圆角"
    }
}

private fun getBackgroundTypeDisplayName(type: QRBackgroundType): String {
    return when (type) {
        QRBackgroundType.SOLID -> "纯色"
        QRBackgroundType.LINEAR_GRADIENT -> "线性渐变"
        QRBackgroundType.RADIAL_GRADIENT -> "径向渐变"
    }
}