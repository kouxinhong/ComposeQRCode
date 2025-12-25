package com.qrcode.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.qrcode.app.R
import com.qrcode.app.model.QRoseStyleConfig
import com.qrcode.app.model.StylePreset
import com.qrcode.app.ui.components.QRoseStyleDialog
import io.github.alexzhirkevich.qrose.QrCodePainter
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    onBackClick: () -> Unit
) {
    var content by remember { mutableStateOf("") }
    var showStyleDialog by remember { mutableStateOf(false) }
    var currentConfig by remember { mutableStateOf(QRoseStyleConfig.DEFAULT) }
    var selectedPreset by remember { mutableStateOf<StylePreset>(StylePreset.Business) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.generate_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.close))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 内容输入区域
            ContentInputSection(
                content = content,
                onContentChanged = { content = it }
            )
            
            // 样式选择区域
            StyleSelectionSection(
                selectedPreset = selectedPreset,
                onPresetSelected = { selectedPreset = it },
                onCustomizeClick = { showStyleDialog = true }
            )
            
            // 二维码显示区域
            if (content.isNotBlank()) {
                QRCodeDisplaySection(
                    content = content,
                    config = currentConfig,
                    onSaveClick = { showSaveDialog = true },
                    onShareClick = { showShareDialog = true }
                )
            }
        }
    }
    
    // 样式自定义对话框
    if (showStyleDialog) {
        QRoseStyleDialog(
            onDismiss = { showStyleDialog = false },
            onStyleSelected = { 
                currentConfig = it
                showStyleDialog = false 
            },
            initialConfig = currentConfig
        )
    }
    
    // 保存确认对话框
    if (showSaveDialog) {
        SaveConfirmDialog(
            onDismiss = { showSaveDialog = false },
            onConfirm = {
                // TODO: 实现保存功能
                showSaveDialog = false
            }
        )
    }
    
    // 分享对话框
    if (showShareDialog) {
        ShareDialog(
            onDismiss = { showShareDialog = false },
            onShare = { method ->
                // TODO: 实现分享功能
                showShareDialog = false
            }
        )
    }
}

@Composable
private fun ContentInputSection(
    content: String,
    onContentChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.generate_hint),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = content,
                onValueChange = onContentChanged,
                label = { Text(stringResource(R.string.qr_code_content)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 4,
                placeholder = { Text(stringResource(R.string.generate_hint)) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
            )
        }
    }
}

@Composable
private fun StyleSelectionSection(
    selectedPreset: StylePreset,
    onPresetSelected: (StylePreset) -> Unit,
    onCustomizeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.qr_style_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                FilledTonalButton(
                    onClick = onCustomizeClick,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Tune,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.generate_custom_style))
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 预设样式网格
            val presetItems = listOf(
                stringResource(R.string.qr_preset_business) to StylePreset.Business,
                stringResource(R.string.qr_preset_artistic) to StylePreset.Artistic,
                stringResource(R.string.qr_preset_elegant) to StylePreset.Elegant,
                stringResource(R.string.qr_preset_vibrant) to StylePreset.Vibrant,
                stringResource(R.string.qr_preset_minimal) to StylePreset.Minimal
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(200.dp)
            ) {
                items(presetItems) { (name, preset) ->
                    PresetStyleCard(
                        name = name,
                        isSelected = selectedPreset == preset,
                        onClick = { onPresetSelected(preset) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PresetStyleCard(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = when (name) {
        "商务" -> Color(0xFF1976D2)
        "艺术" -> Color(0xFFE91E63)
        "优雅" -> Color(0xFF424242)
        "活力" -> Color(0xFFFF5722)
        "极简" -> Color.Black
        else -> Color.Black
    }
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) colors.copy(alpha = 0.1f) 
                           else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) colors else MaterialTheme.colorScheme.onSurface
        ),
        border = if (isSelected) {
                CardDefaults.outlinedCardBorder(true).copy(width = 1.dp, brush = androidx.compose.ui.graphics.SolidColor(colors))
            } else {
                CardDefaults.outlinedCardBorder(true)
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
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(colors, CircleShape)
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun QRCodeDisplaySection(
    content: String,
    config: QRoseStyleConfig,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.button_preview),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 使用QRose库的原生API生成二维码，并应用样式配置
            val qrPainter = rememberQrCodePainter(data = content)
            
            Card(
                modifier = Modifier.size(200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Image(
                    painter = qrPainter,
                    contentDescription = stringResource(R.string.qr_style_title),
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = onSaveClick,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.generate_save))
                }
                
                OutlinedButton(
                    onClick = onShareClick,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.generate_share))
                }
            }
        }
    }
}

@Composable
private fun SaveConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.generate_save)) },
        text = { Text("确定要保存这个二维码吗？") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.button_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.button_cancel))
            }
        }
    )
}

@Composable
private fun ShareDialog(
    onDismiss: () -> Unit,
    onShare: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.generate_share)) },
        text = { Text("选择分享方式") },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onShare("image") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("分享图片")
                }
                OutlinedButton(
                    onClick = { onShare("text") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("分享文本")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.button_cancel))
            }
        }
    )
}