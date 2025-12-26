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
import com.qrcode.app.utils.QRoseComposeUtils
import com.qrcode.app.utils.QRCodeStyle
import com.qrcode.generator.models.ShapePreset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    onBackClick: () -> Unit
) {
    var content by remember { mutableStateOf("") }
    var showStyleDialog by remember { mutableStateOf(false) }
    var currentConfig by remember { mutableStateOf(QRoseStyleConfig.DEFAULT) }
    var selectedStyle by remember { mutableStateOf<QRCodeStyle>(QRCodeStyle.BASIC) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.generate_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = stringResource(R.string.close),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 欢迎标题
            WelcomeSection()
            
            // 内容输入区域
            ContentInputSection(
                content = content,
                onContentChanged = { content = it }
            )
            
            // 样式选择区域
            StyleSelectionSection(
                selectedStyle = selectedStyle,
                onStyleSelected = { selectedStyle = it },
                onCustomizeClick = { showStyleDialog = true }
            )
            
            // 二维码显示区域
            if (content.isNotBlank()) {
                QRCodeDisplaySection(
                    content = content,
                    config = currentConfig,
                    selectedStyle = selectedStyle,
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
private fun WelcomeSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "创建个性化二维码",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "输入内容，选择样式，生成专属二维码",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.generate_hint),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            OutlinedTextField(
                value = content,
                onValueChange = onContentChanged,
                label = { Text(stringResource(R.string.qr_code_content)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 4,
                placeholder = { 
                    Text(
                        stringResource(R.string.generate_hint),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}

@Composable
private fun StyleSelectionSection(
    selectedStyle: QRCodeStyle,
    onStyleSelected: (QRCodeStyle) -> Unit,
    onCustomizeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Style,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.qr_style_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                FilledTonalButton(
                    onClick = onCustomizeClick,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        Icons.Default.Tune,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stringResource(R.string.generate_custom_style))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 预设样式网格 - 基于custom-qr-generator的所有可用形状
            val styleItems = listOf(
                "基础" to QRCodeStyle.BASIC,
                "圆形" to QRCodeStyle.CIRCLE,
                "圆角" to QRCodeStyle.ROUNDED,
                "渐变" to QRCodeStyle.GRADIENT,
                "艺术" to QRCodeStyle.ARTISTIC,
                "点阵" to QRCodeStyle.DOT,
                "菱形" to QRCodeStyle.RHOMBUS,
                "星形" to QRCodeStyle.STAR,
                "垂直圆角" to QRCodeStyle.ROUNDED_VERTICAL,
                "水平圆角" to QRCodeStyle.ROUNDED_HORIZONTAL,
                "圆形框架" to QRCodeStyle.CIRCLE_FRAME,
                "圆角框架" to QRCodeStyle.ROUNDED_FRAME,
                "圆形眼球" to QRCodeStyle.CIRCLE_BALL,
                "圆角眼球" to QRCodeStyle.ROUNDED_BALL,
                "菱形眼球" to QRCodeStyle.RHOMBUS_BALL
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.heightIn(min = 200.dp, max = 400.dp)
            ) {
                items(styleItems) { (name, style) ->
                    StyleCard(
                        name = name,
                        style = style,
                        isSelected = selectedStyle == style,
                        onClick = { onStyleSelected(style) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StyleCard(
    name: String,
    style: QRCodeStyle,
    isSelected: Boolean,
    onClick: () -> Unit
) {
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
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = if (isSelected) {
                CardDefaults.outlinedCardBorder(true).copy(
                    width = 3.dp, 
                    brush = androidx.compose.ui.graphics.SolidColor(colors)
                )
            } else {
                CardDefaults.outlinedCardBorder(true).copy(
                    width = 1.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                )
            },
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
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // 样式预览图标
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
                
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                
                if (isSelected) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "已选择",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QRCodeDisplaySection(
    content: String,
    config: QRoseStyleConfig,
    selectedStyle: QRCodeStyle,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Icon(
                    Icons.Default.QrCode,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.button_preview),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // 二维码预览卡片
            Card(
                modifier = Modifier.size(240.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val qrBitmap = QRoseComposeUtils.rememberQRCode(
                        content = content,
                        style = selectedStyle,
                        size = 200
                    )
                    
                    Image(
                        bitmap = qrBitmap,
                        contentDescription = stringResource(R.string.qr_style_title),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 当前样式信息
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Style,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "当前样式: ${getStyleDisplayName(selectedStyle)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    onClick = onSaveClick,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        stringResource(R.string.generate_save),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                
                OutlinedButton(
                    onClick = onShareClick,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    )
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        stringResource(R.string.generate_share),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

private fun getStyleDisplayName(style: QRCodeStyle): String {
    return when (style) {
        QRCodeStyle.BASIC -> "基础方形"
        QRCodeStyle.CIRCLE -> "圆形像素"
        QRCodeStyle.ROUNDED -> "圆角样式"
        QRCodeStyle.GRADIENT -> "渐变效果"
        QRCodeStyle.ARTISTIC -> "艺术风格"
        QRCodeStyle.DOT -> "点阵样式"
        QRCodeStyle.RHOMBUS -> "菱形像素"
        QRCodeStyle.STAR -> "星形样式"
        QRCodeStyle.ROUNDED_VERTICAL -> "垂直圆角"
        QRCodeStyle.ROUNDED_HORIZONTAL -> "水平圆角"
        QRCodeStyle.CIRCLE_FRAME -> "圆形框架"
        QRCodeStyle.ROUNDED_FRAME -> "圆角框架"
        QRCodeStyle.CIRCLE_BALL -> "圆形眼球"
        QRCodeStyle.ROUNDED_BALL -> "圆角眼球"
        QRCodeStyle.RHOMBUS_BALL -> "菱形眼球"
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