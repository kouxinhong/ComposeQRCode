package com.qrcode.app.ui.screens

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.qrcode.app.R
import com.qrcode.app.model.QRoseStyleConfig
import com.qrcode.app.model.StylePreset
import com.qrcode.app.ui.components.QRoseStyleDialog
import com.qrcode.app.ui.components.StylePreviewCard
import com.qrcode.app.utils.QRoseComposeUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    onBackClick: () -> Unit,
    content: String = ""
) {
    var inputContent by remember { mutableStateOf(content) }
    var currentConfig by remember { mutableStateOf(QRoseStyleConfig.DEFAULT.copy(
        darkPixelColor = Color(0xFF000000),  // 纯黑色
        lightPixelColor = Color(0xFFFFFFFF),  // 纯白色
        backgroundColor = Color(0xFFF5F5F5)   // 浅灰色背景
    )) }
    var selectedPreset by remember { mutableStateOf<StylePreset?>(null) }
    var showStyleDialog by remember { mutableStateOf(false) }
    var isGenerating by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val qrBitmap = QRoseComposeUtils.rememberQRCode(
        content = inputContent,
        config = currentConfig,
        onGenerating = { isGenerating = it }
    )

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
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (inputContent.isNotBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("已刷新二维码")
                            }
                        }
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "刷新")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QRCodePreviewSection(
                    bitmap = qrBitmap,
                    isGenerating = isGenerating,
                    config = currentConfig,
                    inputContent = inputContent,
                    onStyleClick = { showStyleDialog = true }
                )

                Spacer(modifier = Modifier.height(20.dp))

                ContentInputSection(
                    content = inputContent,
                    onContentChanged = { inputContent = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PresetSelectorSection(
                    selectedPreset = selectedPreset,
                    currentConfig = currentConfig,
                    onPresetSelected = { preset ->
                        selectedPreset = preset
                        currentConfig = when (preset) {
                            is StylePreset.Business -> QRoseStyleConfig.BUSINESS
                            is StylePreset.Artistic -> QRoseStyleConfig.ARTISTIC
                            is StylePreset.Elegant -> QRoseStyleConfig.ELEGANT
                            is StylePreset.Vibrant -> QRoseStyleConfig.VIBRANT
                            is StylePreset.Minimal -> QRoseStyleConfig.MINIMAL
                            is StylePreset.Rainbow -> QRoseStyleConfig.RAINBOW
                            is StylePreset.Neon -> QRoseStyleConfig.NEON
                            is StylePreset.Custom -> preset.config
                        }
                    },
                    onCustomizeClick = { showStyleDialog = true }
                )
            }
        }
    }

    if (showStyleDialog) {
        QRoseStyleDialog(
            onDismiss = { showStyleDialog = false },
            onStyleSelected = { config ->
                currentConfig = config
                selectedPreset = null
                showStyleDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("样式已更新")
                }
            },
            initialConfig = currentConfig
        )
    }
}

@Composable
private fun QRCodePreviewSection(
    bitmap: ImageBitmap,
    isGenerating: Boolean,
    config: QRoseStyleConfig,
    inputContent: String,
    onStyleClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isGenerating) 0.95f else 1f,
        animationSpec = tween(150),
        label = "qr_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onStyleClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(scale)
                    .clip(RoundedCornerShape(16.dp))
                    .background(getBackgroundBrush(config))
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                android.util.Log.d("GenerateScreen", "QRCodePreviewSection: inputContent='$inputContent', isGenerating=$isGenerating, bitmap=${bitmap.width}x${bitmap.height}, background=${config.backgroundColor}")
                if (!isGenerating && bitmap.width > 1) {
                    // 显示QR码，确保合适的尺寸
                    Image(
                        bitmap = bitmap,
                        contentDescription = "二维码预览",
                        modifier = Modifier
                            .size(200.dp)  // 固定尺寸确保可见
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else if (!isGenerating) {
                    // 显示调试信息
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "位图异常: ${bitmap.width}x${bitmap.height}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "输入内容: '$inputContent'",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (inputContent.isBlank()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "输入内容生成二维码",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "点击自定义样式",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ContentInputSection(
    content: String,
    onContentChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
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
                    text = stringResource(R.string.qr_code_content),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedTextField(
                value = content,
                onValueChange = onContentChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        stringResource(R.string.generate_hint),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                maxLines = 3,
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            if (content.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "字符数: ${content.length}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun PresetSelectorSection(
    selectedPreset: StylePreset?,
    currentConfig: QRoseStyleConfig,
    onPresetSelected: (StylePreset) -> Unit,
    onCustomizeClick: () -> Unit
) {
    val presets = listOf(
        StylePreset.Business to "商务",
        StylePreset.Artistic to "艺术",
        StylePreset.Elegant to "优雅",
        StylePreset.Vibrant to "活力",
        StylePreset.Minimal to "极简",
        StylePreset.Rainbow to "彩虹",
        StylePreset.Neon to "霓虹"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Style,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "选择样式",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                FilledTonalButton(
                    onClick = onCustomizeClick,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Default.Tune,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("自定义", style = MaterialTheme.typography.labelMedium)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(presets) { (preset, name) ->
                    val presetConfig = when (preset) {
                        is StylePreset.Business -> QRoseStyleConfig.BUSINESS
                        is StylePreset.Artistic -> QRoseStyleConfig.ARTISTIC
                        is StylePreset.Elegant -> QRoseStyleConfig.ELEGANT
                        is StylePreset.Vibrant -> QRoseStyleConfig.VIBRANT
                        is StylePreset.Minimal -> QRoseStyleConfig.MINIMAL
                        is StylePreset.Rainbow -> QRoseStyleConfig.RAINBOW
                        is StylePreset.Neon -> QRoseStyleConfig.NEON
                        is StylePreset.Custom -> preset.config
                    }

                    StylePreviewCard(
                        name = name,
                        config = presetConfig,
                        isSelected = selectedPreset == preset,
                        onClick = { onPresetSelected(preset) }
                    )
                }
            }
        }
    }
}

private fun getBackgroundBrush(config: QRoseStyleConfig): Brush {
    // 简化背景：直接使用纯色避免复杂渲染问题
    return Brush.linearGradient(
        colors = listOf(config.backgroundColor, config.backgroundColor)
    )
}
