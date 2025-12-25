package com.qrcode.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.qrcode.app.R
import com.qrcode.app.model.*

/**
 * QRose样式选择对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRoseStyleDialog(
    onDismiss: () -> Unit,
    onStyleSelected: (QRoseStyleConfig) -> Unit,
    initialConfig: QRoseStyleConfig = QRoseStyleConfig.DEFAULT
) {
    var currentConfig by remember { mutableStateOf(initialConfig) }
    var selectedTab by remember { mutableStateOf(StyleTab.PRESETS) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column {
                // 标题栏
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.qr_style_title)) },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "关闭")
                        }
                    },
                    actions = {
                        TextButton(onClick = { onStyleSelected(currentConfig) }) {
                            Text(stringResource(R.string.button_apply))
                        }
                    }
                )

                // 标签页
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    StyleTab.values().forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = { Text(tab.title) }
                        )
                    }
                }

                // 内容区域
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    when (selectedTab) {
                        StyleTab.PRESETS -> PresetsTab(
                            onConfigSelected = { currentConfig = it }
                        )
                        StyleTab.PIXEL -> PixelTab(
                            config = currentConfig,
                            onConfigChanged = { currentConfig = it }
                        )
                        StyleTab.EYE -> EyeTab(
                            config = currentConfig,
                            onConfigChanged = { currentConfig = it }
                        )
                        StyleTab.BACKGROUND -> BackgroundTab(
                            config = currentConfig,
                            onConfigChanged = { currentConfig = it }
                        )
                        StyleTab.COLORS -> ColorsTab(
                            config = currentConfig,
                            onConfigChanged = { currentConfig = it }
                        )
                        StyleTab.LOGO -> LogoTab(
                            config = currentConfig,
                            onConfigChanged = { currentConfig = it }
                        )
                        StyleTab.ADVANCED -> AdvancedTab(
                            config = currentConfig,
                            onConfigChanged = { currentConfig = it }
                        )
                    }
                }
            }
        }
    }
}

private enum class StyleTab(val title: String) {
    PRESETS("预设"),
    PIXEL("像素"),
    EYE("眼部"),
    BACKGROUND("背景"),
    COLORS("颜色"),
    LOGO("Logo"),
    ADVANCED("高级")
}

@Composable
private fun PresetsTab(onConfigSelected: (QRoseStyleConfig) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            listOf(
                "商务" to QRoseStyleConfig.BUSINESS,
                "艺术" to QRoseStyleConfig.ARTISTIC,
                "优雅" to QRoseStyleConfig.ELEGANT,
                "活力" to QRoseStyleConfig.VIBRANT,
                "极简" to QRoseStyleConfig.MINIMAL,
                "默认" to QRoseStyleConfig.DEFAULT
            )
        ) { (name, config) ->
            PresetCard(
                name = name,
                config = config,
                onClick = { onConfigSelected(config) }
            )
        }
    }
}

@Composable
private fun PresetCard(
    name: String,
    config: QRoseStyleConfig,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(config.backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(config.pixelColor, CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (config.backgroundColor == Color.White) Color.Black else Color.White
                )
            }
        }
    }
}

@Composable
private fun PixelTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = stringResource(R.string.qr_pixel_shape),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PixelShape.values().forEach { shape ->
                    FilterChip(
                        selected = config.pixelShape == shape,
                        onClick = {
                            onConfigChanged(config.copy(pixelShape = shape))
                        },
                        label = { Text(shape.name) }
                    )
                }
            }
        }

        item {
            Column {
                Text(
                    text = stringResource(R.string.qr_corner_radius),
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = config.pixelCornerRadius,
                    onValueChange = {
                        onConfigChanged(config.copy(pixelCornerRadius = it))
                    },
                    valueRange = 0f..1f
                )
            }
        }
    }
}

@Composable
private fun EyeTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = stringResource(R.string.qr_eye_shape),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EyeShape.values().forEach { shape ->
                    FilterChip(
                        selected = config.eyeShape == shape,
                        onClick = {
                            onConfigChanged(config.copy(eyeShape = shape))
                        },
                        label = { Text(shape.name) }
                    )
                }
            }
        }

        item {
            Column {
                Text(
                    text = stringResource(R.string.qr_eye_corner_radius),
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = config.eyeCornerRadius,
                    onValueChange = {
                        onConfigChanged(config.copy(eyeCornerRadius = it))
                    },
                    valueRange = 0f..1f
                )
            }
        }
    }
}

@Composable
private fun BackgroundTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = stringResource(R.string.qr_background_type),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BackgroundType.values().take(3).forEach { type ->
                    FilterChip(
                        selected = config.backgroundType == type,
                        onClick = {
                            onConfigChanged(config.copy(backgroundType = type))
                        },
                        label = { Text(type.name) }
                    )
                }
            }
        }

        if (config.backgroundType != BackgroundType.SOLID) {
            item {
                ColorPickerRow(
                    label = stringResource(R.string.gradient_start_color),
                    color = config.gradientStartColor,
                    onColorSelected = {
                        onConfigChanged(config.copy(gradientStartColor = it))
                    }
                )
            }
            
            item {
                ColorPickerRow(
                    label = stringResource(R.string.gradient_end_color),
                    color = config.gradientEndColor,
                    onColorSelected = {
                        onConfigChanged(config.copy(gradientEndColor = it))
                    }
                )
            }
        }
    }
}

@Composable
private fun ColorsTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ColorPickerRow(
                label = stringResource(R.string.qr_foreground_color),
                color = config.pixelColor,
                onColorSelected = {
                    onConfigChanged(config.copy(pixelColor = it))
                }
            )
        }
        
        item {
            ColorPickerRow(
                label = stringResource(R.string.qr_background_color),
                color = config.backgroundColor,
                onColorSelected = {
                    onConfigChanged(config.copy(backgroundColor = it))
                }
            )
        }
        
        item {
            ColorPickerRow(
                label = stringResource(R.string.qr_eye_color),
                color = config.eyeColor,
                onColorSelected = {
                    onConfigChanged(config.copy(eyeColor = it))
                }
            )
        }
    }
}

@Composable
private fun LogoTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = stringResource(R.string.qr_logo),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* TODO: 选择Logo */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.qr_logo_select))
                }
                
                OutlinedButton(
                    onClick = { onConfigChanged(config.copy(logoData = null)) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.qr_logo_remove))
                }
            }
        }

        item {
            Column {
                Text(
                    text = stringResource(R.string.qr_logo_size),
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = config.logoSize,
                    onValueChange = {
                        onConfigChanged(config.copy(logoSize = it))
                    },
                    valueRange = 0.1f..0.4f
                )
            }
        }
    }
}

@Composable
private fun AdvancedTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column {
                Text(
                    text = stringResource(R.string.qr_padding),
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = config.padding,
                    onValueChange = {
                        onConfigChanged(config.copy(padding = it))
                    },
                    valueRange = 0f..0.2f
                )
            }
        }
        
        item {
            Text(
                text = stringResource(R.string.qr_error_correction),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ErrorCorrectionLevel.values().forEach { level ->
                    FilterChip(
                        selected = config.errorCorrectionLevel == level,
                        onClick = {
                            onConfigChanged(config.copy(errorCorrectionLevel = level))
                        },
                        label = { Text(level.name) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorPickerRow(
    label: String,
    color: Color,
    onColorSelected: (Color) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), CircleShape)
                .clickable { /* TODO: 打开颜色选择器 */ }
        )
    }
}