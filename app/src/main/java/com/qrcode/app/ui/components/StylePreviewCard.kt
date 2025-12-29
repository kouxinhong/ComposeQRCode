package com.qrcode.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qrcode.app.model.QRoseStyleConfig

@Composable
fun StylePreviewCard(
    name: String,
    config: QRoseStyleConfig,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = tween(150),
        label = "card_scale"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = tween(150),
        label = "border_color"
    )

    Card(
        modifier = Modifier
            .width(80.dp)
            .scale(scale)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder().copy(
                width = 2.dp,
                brush = androidx.compose.ui.graphics.SolidColor(borderColor)
            )
        } else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(getConfigPreviewBackground(config))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(getPreviewShape(config))
                        .background(getConfigPreviewColor(config))
                )

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "已选择",
                            modifier = Modifier
                                .padding(2.dp)
                                .size(16.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape
                                )
                                .padding(2.dp),
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                },
                textAlign = TextAlign.Center,
                maxLines = 1,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun getConfigPreviewBackground(config: QRoseStyleConfig): Brush {
    return if (config.isBackgroundGradient) {
        Brush.linearGradient(
            colors = config.backgroundGradientColors.map { androidx.compose.ui.graphics.Color(it.value.toULong()) }
        )
    } else {
        Brush.linearGradient(
            colors = listOf(config.backgroundColor, config.backgroundColor)
        )
    }
}

@Composable
private fun getConfigPreviewColor(config: QRoseStyleConfig): Color {
    return when {
        config.isDarkPixelGradient && config.darkPixelGradientColors.isNotEmpty() -> {
            config.darkPixelGradientColors.first()
        }
        else -> config.darkPixelColor
    }
}

private fun getPreviewShape(config: QRoseStyleConfig): RoundedCornerShape {
    return when (config.pixelShape) {
        com.qrcode.app.model.PixelShape.CIRCLE -> RoundedCornerShape(50)
        com.qrcode.app.model.PixelShape.ROUNDED_CORNERS -> {
            RoundedCornerShape((config.pixelCornerRadius * 20).dp)
        }
        else -> RoundedCornerShape(4.dp)
    }
}
