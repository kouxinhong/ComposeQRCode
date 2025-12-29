package com.qrcode.app.ui.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.qrcode.app.R
import com.qrcode.app.model.BackgroundType
import com.qrcode.app.model.BallShape
import com.qrcode.app.model.ErrorCorrectionLevel
import com.qrcode.app.model.FrameShape
import com.qrcode.app.model.LogoShape
import com.qrcode.app.model.PixelShape
import com.qrcode.app.model.QRoseStyleConfig
import com.qrcode.app.model.StylePreset

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        stringResource(R.string.qr_style_title),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = onDismiss) {
                                        Icon(Icons.Default.Close, contentDescription = "关闭")
                                    }
                                },
                                actions = {
                                    TextButton(onClick = { onStyleSelected(currentConfig) }) {
                                        Text(
                                            "应用",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            ScrollableTabRow(
                                selectedTabIndex = selectedTab.ordinal,
                                tabs = StyleTab.entries.toTypedArray(),
                                onTabSelected = { selectedTab = it }
                            )

                            AnimatedContent(
                                targetState = selectedTab,
                                transitionSpec = {
                                    if (targetState.ordinal > initialState.ordinal) {
                                        slideInHorizontally { it } + fadeIn() togetherWith
                                                slideOutHorizontally { -it } + fadeOut()
                                    } else {
                                        slideInHorizontally { -it } + fadeIn() togetherWith
                                                slideOutHorizontally { it } + fadeOut()
                                    }
                                },
                                label = "tab_content"
                            ) { tab ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                ) {
                                    when (tab) {
                                        StyleTab.PRESETS -> PresetsTab(
                                            onConfigSelected = { currentConfig = it }
                                        )
                                        StyleTab.BASIC -> BasicTab(
                                            config = currentConfig,
                                            onConfigChanged = { currentConfig = it }
                                        )
                                        StyleTab.COLORS -> ColorsTab(
                                            config = currentConfig,
                                            onConfigChanged = { currentConfig = it }
                                        )
                                        StyleTab.BALL -> BallTab(
                                            config = currentConfig,
                                            onConfigChanged = { currentConfig = it }
                                        )
                                        StyleTab.PIXEL -> PixelTab(
                                            config = currentConfig,
                                            onConfigChanged = { currentConfig = it }
                                        )
                                        StyleTab.FRAME -> FrameTab(
                                            config = currentConfig,
                                            onConfigChanged = { currentConfig = it }
                                        )
                                        StyleTab.LOGO -> LogoTab(
                                            config = currentConfig,
                                            onConfigChanged = { currentConfig = it }
                                        )
                                        StyleTab.BORDER -> BorderTab(
                                            config = currentConfig,
                                            onConfigChanged = { currentConfig = it }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScrollableTabRow(
    selectedTabIndex: Int,
    tabs: Array<StyleTab>,
    onTabSelected: (StyleTab) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(tabs) { tab ->
            FilterChip(
                selected = selectedTabIndex == tab.ordinal,
                onClick = { onTabSelected(tab) },
                label = { Text(tab.displayName, style = MaterialTheme.typography.labelMedium) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

private enum class StyleTab(val displayName: String) {
    PRESETS("预设"),
    BASIC("基础"),
    COLORS("颜色"),
    BALL("眼部"),
    PIXEL("像素"),
    FRAME("框架"),
    LOGO("Logo"),
    BORDER("边框")
}

@Composable
private fun PresetsTab(onConfigSelected: (QRoseStyleConfig) -> Unit) {
    val presets = listOf(
        Triple(StylePreset.Business, "商务", QRoseStyleConfig.BUSINESS),
        Triple(StylePreset.Artistic, "艺术", QRoseStyleConfig.ARTISTIC),
        Triple(StylePreset.Elegant, "优雅", QRoseStyleConfig.ELEGANT),
        Triple(StylePreset.Vibrant, "活力", QRoseStyleConfig.VIBRANT),
        Triple(StylePreset.Minimal, "极简", QRoseStyleConfig.MINIMAL),
        Triple(StylePreset.Rainbow, "彩虹", QRoseStyleConfig.RAINBOW),
        Triple(StylePreset.Neon, "霓虹", QRoseStyleConfig.NEON)
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(presets) { (_, name, config) ->
            PresetConfigCard(
                name = name,
                config = config,
                onClick = { onConfigSelected(config) }
            )
        }
    }
}

@Composable
private fun PresetConfigCard(
    name: String,
    config: QRoseStyleConfig,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(getConfigBackground(config)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(getPreviewShape(config))
                        .background(config.darkPixelColor)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = getPresetDescription(preset = name),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun BasicTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle("二维码尺寸")
            Text(
                text = "尺寸: ${config.size}px",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Slider(
                value = config.size.toFloat(),
                onValueChange = {
                    onConfigChanged(config.copy(size = it.toInt()))
                },
                valueRange = 200f..1024f,
                steps = 16,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("200px", style = MaterialTheme.typography.labelSmall)
                Text("1024px", style = MaterialTheme.typography.labelSmall)
            }
        }

        item {
            SectionTitle("边距")
            Text(
                text = "边距: ${(config.padding * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Slider(
                value = config.padding,
                onValueChange = {
                    onConfigChanged(config.copy(padding = it))
                },
                valueRange = 0f..0.25f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        item {
            SectionTitle("纠错级别")
            Text(
                text = getErrorCorrectionDescription(config.errorCorrectionLevel),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ErrorCorrectionLevel.entries.forEach { level ->
                    FilterChip(
                        selected = config.errorCorrectionLevel == level,
                        onClick = {
                            onConfigChanged(config.copy(errorCorrectionLevel = level))
                        },
                        label = { Text(getLevelShortName(level)) }
                    )
                }
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
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle("暗像素颜色（前景色）")
            ColorSelector(
                colors = getDefaultColors(),
                selectedColor = config.darkPixelColor,
                onColorSelected = {
                    onConfigChanged(
                        config.copy(
                            darkPixelColor = it,
                            isDarkPixelGradient = false
                        )
                    )
                },
                onGradientToggle = {
                    onConfigChanged(config.copy(isDarkPixelGradient = !config.isDarkPixelGradient))
                },
                isGradient = config.isDarkPixelGradient
            )
            if (config.isDarkPixelGradient) {
                Spacer(modifier = Modifier.height(8.dp))
                GradientColorSelector(
                    colors = config.darkPixelGradientColors,
                    onColorsChanged = {
                        onConfigChanged(config.copy(darkPixelGradientColors = it))
                    },
                    maxColors = 4
                )
                Spacer(modifier = Modifier.height(8.dp))
                SectionTitle("渐变角度")
                Slider(
                    value = config.darkPixelGradientAngle,
                    onValueChange = {
                        onConfigChanged(config.copy(darkPixelGradientAngle = it))
                    },
                    valueRange = 0f..360f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "角度: ${config.darkPixelGradientAngle.toInt()}°",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            SectionTitle("亮像素颜色（背景色）")
            ColorSelector(
                colors = getDefaultColors(),
                selectedColor = config.lightPixelColor,
                onColorSelected = {
                    onConfigChanged(
                        config.copy(
                            lightPixelColor = it,
                            isLightPixelGradient = false
                        )
                    )
                },
                onGradientToggle = {
                    onConfigChanged(config.copy(isLightPixelGradient = !config.isLightPixelGradient))
                },
                isGradient = config.isLightPixelGradient
            )
            if (config.isLightPixelGradient) {
                Spacer(modifier = Modifier.height(8.dp))
                GradientColorSelector(
                    colors = config.lightPixelGradientColors,
                    onColorsChanged = {
                        onConfigChanged(config.copy(lightPixelGradientColors = it))
                    },
                    maxColors = 4
                )
                Spacer(modifier = Modifier.height(8.dp))
                SectionTitle("渐变角度")
                Slider(
                    value = config.lightPixelGradientAngle,
                    onValueChange = {
                        onConfigChanged(config.copy(lightPixelGradientAngle = it))
                    },
                    valueRange = 0f..360f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "角度: ${config.lightPixelGradientAngle.toInt()}°",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            SectionTitle("背景设置")
            BackgroundTypeSelector(
                selectedType = config.backgroundType,
                onTypeSelected = {
                    onConfigChanged(config.copy(backgroundType = it))
                }
            )
        }

        item {
            SectionTitle("背景颜色")
            ColorSelector(
                colors = getDefaultColors(),
                selectedColor = config.backgroundColor,
                onColorSelected = {
                    onConfigChanged(
                        config.copy(
                            backgroundColor = it,
                            isBackgroundGradient = false
                        )
                    )
                },
                onGradientToggle = {
                    onConfigChanged(config.copy(isBackgroundGradient = !config.isBackgroundGradient))
                },
                isGradient = config.isBackgroundGradient
            )
            if (config.isBackgroundGradient) {
                Spacer(modifier = Modifier.height(8.dp))
                GradientColorSelector(
                    colors = config.backgroundGradientColors,
                    onColorsChanged = {
                        onConfigChanged(config.copy(backgroundGradientColors = it))
                    },
                    maxColors = 4
                )
                Spacer(modifier = Modifier.height(8.dp))
                SectionTitle("渐变角度")
                Slider(
                    value = config.backgroundGradientAngle,
                    onValueChange = {
                        onConfigChanged(config.copy(backgroundGradientAngle = it))
                    },
                    valueRange = 0f..360f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "角度: ${config.backgroundGradientAngle.toInt()}°",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun BallTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle("眼部形状")
            val shapes = BallShape.entries.toList()
            ShapeSelectorBall(
                shapes = shapes,
                selectedShape = config.ballShape,
                shapeNames = mapOf(
                    BallShape.DEFAULT to "默认",
                    BallShape.CIRCLE to "圆形",
                    BallShape.ROUNDED_CORNERS to "圆角",
                    BallShape.VERTICAL to "垂直",
                    BallShape.HORIZONTAL to "水平",
                    BallShape.ARC to "弧形"
                ),
                onShapeSelected = {
                    onConfigChanged(config.copy(ballShape = it))
                }
            )
        }

        item {
            SectionTitle("眼部颜色")
            ColorSelector(
                colors = getDefaultColors(),
                selectedColor = config.ballColor,
                onColorSelected = {
                    onConfigChanged(config.copy(ballColor = it))
                },
                onGradientToggle = {
                    onConfigChanged(config.copy(isBallGradient = !config.isBallGradient))
                },
                isGradient = config.isBallGradient
            )
            if (config.isBallGradient) {
                Spacer(modifier = Modifier.height(8.dp))
                GradientColorSelector(
                    colors = config.ballGradientColors,
                    onColorsChanged = {
                        onConfigChanged(config.copy(ballGradientColors = it))
                    },
                    maxColors = 4
                )
                Spacer(modifier = Modifier.height(8.dp))
                SectionTitle("渐变角度")
                Slider(
                    value = config.ballGradientAngle,
                    onValueChange = {
                        onConfigChanged(config.copy(ballGradientAngle = it))
                    },
                    valueRange = 0f..360f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "角度: ${config.ballGradientAngle.toInt()}°",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            SectionTitle("圆角程度")
            Slider(
                value = config.ballCornerRadius,
                onValueChange = {
                    onConfigChanged(config.copy(ballCornerRadius = it))
                },
                valueRange = 0f..0.5f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "圆角: ${(config.ballCornerRadius * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun PixelTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle("像素形状")
            val shapes = PixelShape.entries.toList()
            ShapeSelectorPixel(
                shapes = shapes,
                selectedShape = config.pixelShape,
                shapeNames = mapOf(
                    PixelShape.DEFAULT to "默认",
                    PixelShape.CIRCLE to "圆形",
                    PixelShape.ROUNDED_CORNERS to "圆角",
                    PixelShape.ARC to "弧形",
                    PixelShape.VERTICAL to "垂直",
                    PixelShape.HORIZONTAL to "水平",
                    PixelShape.DIAMOND to "菱形"
                ),
                onShapeSelected = {
                    onConfigChanged(config.copy(pixelShape = it))
                }
            )
        }

        item {
            SectionTitle("圆角程度")
            Slider(
                value = config.pixelCornerRadius,
                onValueChange = {
                    onConfigChanged(config.copy(pixelCornerRadius = it))
                },
                valueRange = 0f..0.5f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "圆角: ${(config.pixelCornerRadius * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SectionTitle("像素渐变")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = config.isPixelGradient,
                    onClick = {
                        onConfigChanged(config.copy(isPixelGradient = !config.isPixelGradient))
                    },
                    label = { Text("启用渐变") }
                )
            }
            if (config.isPixelGradient) {
                Spacer(modifier = Modifier.height(8.dp))
                GradientColorSelector(
                    colors = config.pixelGradientColors,
                    onColorsChanged = {
                        onConfigChanged(config.copy(pixelGradientColors = it))
                    },
                    maxColors = 4
                )
                Spacer(modifier = Modifier.height(8.dp))
                SectionTitle("渐变角度")
                Slider(
                    value = config.pixelGradientAngle,
                    onValueChange = {
                        onConfigChanged(config.copy(pixelGradientAngle = it))
                    },
                    valueRange = 0f..360f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "角度: ${config.pixelGradientAngle.toInt()}°",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun FrameTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle("框架形状")
            val shapes = FrameShape.entries.toList()
            ShapeSelectorFrame(
                shapes = shapes,
                selectedShape = config.frameShape,
                shapeNames = mapOf(
                    FrameShape.DEFAULT to "默认",
                    FrameShape.ROUNDED_CORNERS to "圆角",
                    FrameShape.ROUNDED_VERTICAL to "垂直圆角",
                    FrameShape.ROUNDED_HORIZONTAL to "水平圆角"
                ),
                onShapeSelected = {
                    onConfigChanged(config.copy(frameShape = it))
                }
            )
        }

        item {
            SectionTitle("框架颜色")
            ColorSelector(
                colors = getDefaultColors(),
                selectedColor = config.frameColor,
                onColorSelected = {
                    onConfigChanged(config.copy(frameColor = it))
                },
                onGradientToggle = {},
                isGradient = false
            )
        }

        item {
            SectionTitle("框架宽度")
            Slider(
                value = config.frameWidth,
                onValueChange = {
                    onConfigChanged(config.copy(frameWidth = it))
                },
                valueRange = 0f..0.5f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
private fun LogoTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    val context = LocalContext.current
    var selectedBitmap by remember { mutableStateOf(config.logoBitmap) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val imageLoader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(it)
                    .allowHardware(false)
                    .build()
                val result = imageLoader.execute(request)
                if (result is SuccessResult) {
                    val bitmap = (result.drawable as BitmapDrawable).bitmap
                    withContext(Dispatchers.Main) {
                        selectedBitmap = bitmap
                        onConfigChanged(config.copy(logoBitmap = bitmap))
                    }
                }
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle("Logo图片")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("选择图片")
                }
                if (selectedBitmap != null) {
                    OutlinedButton(
                        onClick = {
                            selectedBitmap = null
                            onConfigChanged(config.copy(logoBitmap = null))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("移除")
                    }
                }
            }

            if (selectedBitmap != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.foundation.Image(
                            bitmap = selectedBitmap!!.asImageBitmap(),
                            contentDescription = "Logo预览",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }

        item {
            SectionTitle("Logo大小")
            Slider(
                value = config.logoSize,
                onValueChange = {
                    onConfigChanged(config.copy(logoSize = it))
                },
                valueRange = 0.1f..0.4f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "大小: ${(config.logoSize * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SectionTitle("Logo边距")
            Slider(
                value = config.logoPadding,
                onValueChange = {
                    onConfigChanged(config.copy(logoPadding = it))
                },
                valueRange = 0f..0.2f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "边距: ${(config.logoPadding * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SectionTitle("Logo形状")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LogoShape.entries.forEach { shape ->
                    FilterChip(
                        selected = config.logoShape == shape,
                        onClick = {
                            onConfigChanged(config.copy(logoShape = shape))
                        },
                        label = { Text(getLogoShapeName(shape)) }
                    )
                }
            }
        }

        item {
            SectionTitle("Logo背景色")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = config.logoBackgroundColor != null,
                    onClick = {
                        if (config.logoBackgroundColor != null) {
                            onConfigChanged(config.copy(logoBackgroundColor = null))
                        } else {
                            onConfigChanged(config.copy(logoBackgroundColor = Color.White))
                        }
                    },
                    label = { Text("启用背景色") }
                )
            }
            if (config.logoBackgroundColor != null) {
                Spacer(modifier = Modifier.height(8.dp))
                ColorSelector(
                    colors = getDefaultColors(),
                    selectedColor = config.logoBackgroundColor!!,
                    onColorSelected = {
                        onConfigChanged(config.copy(logoBackgroundColor = it))
                    },
                    onGradientToggle = {},
                    isGradient = false
                )
            }
        }

        item {
            SectionTitle("Logo圆角")
            Slider(
                value = config.logoCornerRadius,
                onValueChange = {
                    onConfigChanged(config.copy(logoCornerRadius = it))
                },
                valueRange = 0f..0.5f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "圆角: ${(config.logoCornerRadius * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun BorderTab(
    config: QRoseStyleConfig,
    onConfigChanged: (QRoseStyleConfig) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle("边框颜色")
            ColorSelector(
                colors = getDefaultColors(),
                selectedColor = config.outerBorderColor,
                onColorSelected = {
                    onConfigChanged(config.copy(outerBorderColor = it))
                },
                onGradientToggle = {},
                isGradient = false
            )
        }

        item {
            SectionTitle("边框宽度")
            Slider(
                value = config.outerBorderWidth,
                onValueChange = {
                    onConfigChanged(config.copy(outerBorderWidth = it))
                },
                valueRange = 0f..0.1f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "宽度: ${(config.outerBorderWidth * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SectionTitle("边框圆角")
            Slider(
                value = config.outerBorderCornerRadius,
                onValueChange = {
                    onConfigChanged(config.copy(outerBorderCornerRadius = it))
                },
                valueRange = 0f..0.3f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        item {
            SectionTitle("高级设置")
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionTitle("透明度")
                Slider(
                    value = config.transparency,
                    onValueChange = {
                        onConfigChanged(config.copy(transparency = it))
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "透明度: ${(config.transparency * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun ColorSelector(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    onGradientToggle: () -> Unit,
    isGradient: Boolean
) {
    Column {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colors.size) { index ->
                val color = colors[index]
                val isSelected = color == selectedColor

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outline
                            },
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(color) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "已选择",
                            modifier = Modifier.size(20.dp),
                            tint = if (color.luminance() > 0.5f) Color.Black else Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GradientColorSelector(
    colors: List<Color>,
    onColorsChanged: (List<Color>) -> Unit,
    maxColors: Int
) {
    Column {
        Text(
            text = "渐变色",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colors.size) { index ->
                var color by remember { mutableStateOf(colors[index]) }
                SimpleColorPicker(
                    color = color,
                    onColorChanged = { newColor ->
                        val newColors = colors.toMutableList()
                        if (index < newColors.size) {
                            newColors[index] = newColor
                            onColorsChanged(newColors)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SimpleColorPicker(
    color: Color,
    onColorChanged: (Color) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable { showDialog = true }
    )

    if (showDialog) {
        SimpleColorDialog(
            currentColor = color,
            onColorSelected = {
                onColorChanged(it)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
private fun SimpleColorDialog(
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val predefinedColors = listOf(
        Color.White, Color(0xFFF5F5F5), Color(0xFF000000), Color(0xFFFF0000),
        Color(0xFFFF7F00), Color(0xFFFFFF00), Color(0xFF00FF00), Color(0xFF00FFFF),
        Color(0xFF0000FF), Color(0xFFFF00FF), Color(0xFF1976D2), Color(0xFF4CAF50),
        Color(0xFFFF5722), Color(0xFF9C27B0), Color(0xFF607D8B), Color(0xFF795548)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "选择颜色",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(predefinedColors) { color ->
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
                                .clickable { onColorSelected(color) }
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

@Composable
private fun ShapeSelectorBall(
    shapes: List<BallShape>,
    selectedShape: BallShape,
    shapeNames: Map<BallShape, String>,
    onShapeSelected: (BallShape) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(shapes) { shape ->
            FilterChip(
                selected = shape == selectedShape,
                onClick = { onShapeSelected(shape) },
                label = { Text(shapeNames[shape] ?: shape.toString()) }
            )
        }
    }
}

@Composable
private fun ShapeSelectorPixel(
    shapes: List<PixelShape>,
    selectedShape: PixelShape,
    shapeNames: Map<PixelShape, String>,
    onShapeSelected: (PixelShape) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(shapes) { shape ->
            FilterChip(
                selected = shape == selectedShape,
                onClick = { onShapeSelected(shape) },
                label = { Text(shapeNames[shape] ?: shape.toString()) }
            )
        }
    }
}

@Composable
private fun ShapeSelectorFrame(
    shapes: List<FrameShape>,
    selectedShape: FrameShape,
    shapeNames: Map<FrameShape, String>,
    onShapeSelected: (FrameShape) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(shapes) { shape ->
            FilterChip(
                selected = shape == selectedShape,
                onClick = { onShapeSelected(shape) },
                label = { Text(shapeNames[shape] ?: shape.toString()) }
            )
        }
    }
}

@Composable
private fun BackgroundTypeSelector(
    selectedType: BackgroundType,
    onTypeSelected: (BackgroundType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BackgroundType.entries.forEach { type ->
            FilterChip(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
                label = { Text(getBackgroundTypeName(type)) }
            )
        }
    }
}

private fun getDefaultColors(): List<Color> {
    return listOf(
        Color.Black, Color(0xFF1976D2), Color(0xFF2196F3), Color(0xFF03A9F4),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFFFEB3B),
        Color(0xFFFF9800), Color(0xFFFF5722), Color(0xFFE91E63), Color(0xFF9C27B0),
        Color(0xFF607D8B), Color(0xFF795548), Color(0xFF000000), Color.White
    )
}

private fun getConfigBackground(config: QRoseStyleConfig): Brush {
    return if (config.isBackgroundGradient) {
        Brush.linearGradient(config.backgroundGradientColors)
    } else {
        Brush.linearGradient(listOf(config.backgroundColor, config.backgroundColor))
    }
}

private fun getPreviewShape(config: QRoseStyleConfig): RoundedCornerShape {
    return when (config.pixelShape) {
        PixelShape.CIRCLE -> RoundedCornerShape(50)
        PixelShape.ROUNDED_CORNERS -> RoundedCornerShape((config.pixelCornerRadius * 20).dp)
        else -> RoundedCornerShape(4.dp)
    }
}

private fun getPresetDescription(preset: String): String {
    return when (preset) {
        "商务" -> "经典蓝色，适合商务场景"
        "艺术" -> "粉色圆形，创意十足"
        "优雅" -> "深灰圆角，低调奢华"
        "活力" -> "橙色渐变，活力四射"
        "极简" -> "黑白分明，简洁大方"
        "彩虹" -> "多彩渐变，绚丽多彩"
        "霓虹" -> "霓虹灯光，赛博朋克"
        else -> "默认样式"
    }
}

private fun getErrorCorrectionDescription(level: ErrorCorrectionLevel): String {
    return when (level) {
        ErrorCorrectionLevel.LOW -> "低纠错 (7%) - 适合清晰环境"
        ErrorCorrectionLevel.MEDIUM -> "中等纠错 (15%) - 默认选择"
        ErrorCorrectionLevel.QUARTILE -> "高纠错 (25%) - 适合中等损坏"
        ErrorCorrectionLevel.HIGH -> "极高纠错 (30%) - 适合严重损坏"
    }
}

private fun getLevelShortName(level: ErrorCorrectionLevel): String {
    return when (level) {
        ErrorCorrectionLevel.LOW -> "L(7%)"
        ErrorCorrectionLevel.MEDIUM -> "M(15%)"
        ErrorCorrectionLevel.QUARTILE -> "Q(25%)"
        ErrorCorrectionLevel.HIGH -> "H(30%)"
    }
}

private fun getBackgroundTypeName(type: BackgroundType): String {
    return when (type) {
        BackgroundType.SOLID -> "纯色"
        BackgroundType.LINEAR_GRADIENT -> "线性渐变"
        BackgroundType.RADIAL_GRADIENT -> "径向渐变"
        BackgroundType.IMAGE -> "图片"
        BackgroundType.TRANSPARENT -> "透明"
    }
}

private fun getLogoShapeName(shape: LogoShape): String {
    return when (shape) {
        LogoShape.SQUARE -> "方形"
        LogoShape.CIRCLE -> "圆形"
        LogoShape.ROUNDED_CORNERS -> "圆角"
        LogoShape.DIAMOND -> "菱形"
    }
}

private fun Color.luminance(): Float {
    return (0.299f * red + 0.587f * green + 0.114f * blue)
}
