package com.qrcode.app.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.ImageBitmap
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.QrErrorCorrectionLevel
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.createQrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColor
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorPixelShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorBallShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorFrameShape
import com.qrcode.app.model.QRoseStyleConfig
import com.qrcode.app.model.BallShape
import com.qrcode.app.model.FrameShape
import com.qrcode.app.model.PixelShape
import com.qrcode.app.model.ErrorCorrectionLevel as AppErrorCorrectionLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object QRoseComposeUtils {

    @Composable
    fun rememberQRCode(
        content: String,
        config: QRoseStyleConfig,
        onGenerating: ((Boolean) -> Unit)? = null
    ): ImageBitmap {
        var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }

        LaunchedEffect(content, config) {
            onGenerating?.invoke(true)
            try {
                Log.d("QRoseComposeUtils", "Generating QR code with content: '$content', config: $config")
                val generatedBitmap = withContext(Dispatchers.Default) {
                    generateQRCodeBitmap(content, config)
                }
                Log.d("QRoseComposeUtils", "QR code generated successfully")
                bitmap = generatedBitmap
            } catch (e: Exception) {
                // 发生错误时显示占位图
                Log.e("QRoseComposeUtils", "Error generating QR code: ${e.message}", e)
                bitmap = createPlaceholderBitmap(config.size)
                e.printStackTrace()
            } finally {
                onGenerating?.invoke(false)
            }
        }

        return bitmap ?: createPlaceholderBitmap(config.size)
    }

    private fun generateQRCodeBitmap(
        content: String,
        config: QRoseStyleConfig
    ): ImageBitmap {
        Log.d("QRoseComposeUtils", "generateQRCodeBitmap called with content: '$content', blank: ${content.isBlank()}")
        
        if (content.isBlank()) {
            Log.d("QRoseComposeUtils", "Content is blank, returning placeholder")
            return createPlaceholderBitmap(config.size)
        }
        
        Log.d("QRoseComposeUtils", "Content is valid, proceeding with QR generation")
        
        // 添加详细调试信息
        Log.d("QRoseComposeUtils", "Config details: size=${config.size}, darkColor=${config.darkPixelColor}, lightColor=${config.lightPixelColor}")
        Log.d("QRoseComposeUtils", "Dark color value: ${config.darkPixelColor.value}, Light color value: ${config.lightPixelColor.value}")

        try {
            Log.d("QRoseComposeUtils", "Creating QR data...")
            Log.d("QRoseComposeUtils", "Original content: '$content'")
            Log.d("QRoseComposeUtils", "Content length: ${content.length}")
            
            val utf8Bytes = content.toByteArray(Charsets.UTF_8)
            val isoString = String(utf8Bytes, Charsets.ISO_8859_1)
            Log.d("QRoseComposeUtils", "ISO-8859-1 string: '$isoString'")
            
            val data = QrData.Text(isoString)
            Log.d("QRoseComposeUtils", "QR data created with Text mode (UTF-8 bytes encoded as ISO-8859-1)")

            Log.d("QRoseComposeUtils", "Creating QR options with config: size=${config.size}, padding=${config.padding}")
            Log.d("QRoseComposeUtils", "Colors: dark=${config.darkPixelColor}, light=${config.lightPixelColor}, ball=${config.ballColor}, frame=${config.frameColor}")
            
            // 使用配置中的颜色设置
            val options = createQrVectorOptions {
                padding = config.padding
                errorCorrectionLevel = config.errorCorrectionLevel.toCustomQrLevel()

                colors {
                    dark = if (config.isDarkPixelGradient) {
                        QrVectorColor.LinearGradient(
                            config.darkPixelGradientColors.mapIndexed { index, color ->
                                index.toFloat() / (config.darkPixelGradientColors.size - 1).coerceAtLeast(1) to android.graphics.Color.argb(
                                    ((config.darkPixelColor.alpha * config.transparency) * 255).toInt(),
                                    (color.red * 255).toInt(),
                                    (color.green * 255).toInt(),
                                    (color.blue * 255).toInt()
                                )
                            },
                            getGradientOrientation(config.darkPixelGradientAngle)
                        )
                    } else {
                        QrVectorColor.Solid(android.graphics.Color.argb(
                            ((config.darkPixelColor.alpha * config.transparency) * 255).toInt(),
                            (config.darkPixelColor.red * 255).toInt(),
                            (config.darkPixelColor.green * 255).toInt(),
                            (config.darkPixelColor.blue * 255).toInt()
                        ))
                    }
                    light = if (config.isLightPixelGradient) {
                        QrVectorColor.LinearGradient(
                            config.lightPixelGradientColors.mapIndexed { index, color ->
                                index.toFloat() / (config.lightPixelGradientColors.size - 1).coerceAtLeast(1) to android.graphics.Color.argb(
                                    ((config.lightPixelColor.alpha * config.transparency) * 255).toInt(),
                                    (color.red * 255).toInt(),
                                    (color.green * 255).toInt(),
                                    (color.blue * 255).toInt()
                                )
                            },
                            getGradientOrientation(config.lightPixelGradientAngle)
                        )
                    } else {
                        QrVectorColor.Solid(android.graphics.Color.argb(
                            ((config.lightPixelColor.alpha * config.transparency) * 255).toInt(),
                            (config.lightPixelColor.red * 255).toInt(),
                            (config.lightPixelColor.green * 255).toInt(),
                            (config.lightPixelColor.blue * 255).toInt()
                        ))
                    }
                    ball = if (config.isBallGradient) {
                        QrVectorColor.LinearGradient(
                            config.ballGradientColors.mapIndexed { index, color ->
                                index.toFloat() / (config.ballGradientColors.size - 1).coerceAtLeast(1) to android.graphics.Color.argb(
                                    ((config.ballColor.alpha * config.transparency) * 255).toInt(),
                                    (color.red * 255).toInt(),
                                    (color.green * 255).toInt(),
                                    (color.blue * 255).toInt()
                                )
                            },
                            getGradientOrientation(config.ballGradientAngle)
                        )
                    } else {
                        QrVectorColor.Solid(android.graphics.Color.argb(
                            ((config.ballColor.alpha * config.transparency) * 255).toInt(),
                            (config.ballColor.red * 255).toInt(),
                            (config.ballColor.green * 255).toInt(),
                            (config.ballColor.blue * 255).toInt()
                        ))
                    }
                    frame = QrVectorColor.Solid(android.graphics.Color.argb(
                        ((config.frameColor.alpha * config.transparency) * 255).toInt(),
                        (config.frameColor.red * 255).toInt(),
                        (config.frameColor.green * 255).toInt(),
                        (config.frameColor.blue * 255).toInt()
                    ))
                }

                shapes {
                    darkPixel = createPixelShape(config.pixelShape, config.pixelCornerRadius)
                    lightPixel = QrVectorPixelShape.Default
                    ball = createBallShape(config.ballShape, config.ballCornerRadius)
                    frame = createFrameShape(config.frameShape, config.frameCornerRadius, config.frameWidth)
                }
            }
            Log.d("QRoseComposeUtils", "QR options created")

            Log.d("QRoseComposeUtils", "Creating QrCodeDrawable...")
            val drawable = QrCodeDrawable(data, options)
            Log.d("QRoseComposeUtils", "QrCodeDrawable created: $drawable")

            Log.d("QRoseComposeUtils", "Creating bitmap with size: ${config.size}")
            val bitmap = Bitmap.createBitmap(config.size, config.size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            
            // 先填充白色背景
            canvas.drawColor(android.graphics.Color.WHITE)

            Log.d("QRoseComposeUtils", "Drawing QR code to canvas...")
            drawable.setBounds(0, 0, config.size, config.size)
            drawable.draw(canvas)
            Log.d("QRoseComposeUtils", "QR code drawn successfully")
            
            // 检查位图内容
            val pixel = bitmap.getPixel(config.size / 2, config.size / 2)
            Log.d("QRoseComposeUtils", "Center pixel color: ${android.graphics.Color.red(pixel)}, ${android.graphics.Color.green(pixel)}, ${android.graphics.Color.blue(pixel)}")
            
            // 检查位图是否全白
            var allWhite = true
            for (i in 0 until 10) {
                for (j in 0 until 10) {
                    val testPixel = bitmap.getPixel(i * config.size / 10, j * config.size / 10)
                    if (testPixel != android.graphics.Color.WHITE) {
                        allWhite = false
                        Log.d("QRoseComposeUtils", "Found non-white pixel at ($i, $j): ${android.graphics.Color.red(testPixel)}, ${android.graphics.Color.green(testPixel)}, ${android.graphics.Color.blue(testPixel)}")
                        break
                    }
                }
                if (!allWhite) break
            }
            Log.d("QRoseComposeUtils", "Bitmap all white: $allWhite")

            return bitmap.asImageBitmap()
        } catch (e: Exception) {
            Log.e("QRoseComposeUtils", "Error generating QR code: ${e.message}", e)
            // 返回一个测试位图来验证渲染
            return createTestBitmap(config.size)
        }
    }
    
    private fun createTestBitmap(size: Int): ImageBitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        // 绘制一个简单的测试图案
        canvas.drawColor(android.graphics.Color.WHITE)
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            style = android.graphics.Paint.Style.FILL
        }
        canvas.drawRect(50f, 50f, 150f, 150f, paint)
        
        return bitmap.asImageBitmap()
    }

    private fun createPixelShape(shape: PixelShape, cornerRadius: Float): QrVectorPixelShape {
        return when (shape) {
            PixelShape.DEFAULT -> QrVectorPixelShape.Default
            PixelShape.CIRCLE -> QrVectorPixelShape.Circle()
            PixelShape.ROUNDED_CORNERS -> QrVectorPixelShape.RoundCorners(cornerRadius.coerceIn(0f, 1f))
            PixelShape.ARC -> QrVectorPixelShape.Default
            PixelShape.VERTICAL -> QrVectorPixelShape.Default
            PixelShape.HORIZONTAL -> QrVectorPixelShape.Default
            PixelShape.DIAMOND -> QrVectorPixelShape.Rhombus()
        }
    }

    private fun createBallShape(shape: BallShape, cornerRadius: Float): QrVectorBallShape {
        return when (shape) {
            BallShape.DEFAULT -> QrVectorBallShape.Default
            BallShape.CIRCLE -> QrVectorBallShape.Circle(1f)
            BallShape.ROUNDED_CORNERS -> QrVectorBallShape.RoundCorners(cornerRadius.coerceIn(0f, 1f))
            BallShape.VERTICAL -> QrVectorBallShape.Default
            BallShape.HORIZONTAL -> QrVectorBallShape.Default
            BallShape.ARC -> QrVectorBallShape.Default
        }
    }

    private fun createFrameShape(shape: FrameShape, cornerRadius: Float, width: Float): QrVectorFrameShape {
        return when (shape) {
            FrameShape.DEFAULT -> QrVectorFrameShape.Default
            FrameShape.ROUNDED_CORNERS -> QrVectorFrameShape.RoundCorners(cornerRadius.coerceIn(0f, 1f), width.coerceIn(0f, 1f))
            FrameShape.ROUNDED_VERTICAL -> QrVectorFrameShape.Default
            FrameShape.ROUNDED_HORIZONTAL -> QrVectorFrameShape.Default
        }
    }

    private fun createPlaceholderBitmap(size: Int): ImageBitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(AndroidColor.WHITE)
        return bitmap.asImageBitmap()
    }

    @Composable
    fun rememberQRCode(
        content: String,
        style: QRCodeStyle = QRCodeStyle.BASIC,
        size: Int = 200,
        foregroundColor: Int = AndroidColor.BLACK,
        backgroundColor: Int = AndroidColor.WHITE,
        backgroundType: QRBackgroundType = QRBackgroundType.SOLID,
        gradientStartColor: Int = AndroidColor.WHITE,
        gradientEndColor: Int = AndroidColor.LTGRAY
    ): ImageBitmap {
        return remember(content, style, size, foregroundColor, backgroundColor, backgroundType, gradientStartColor, gradientEndColor) {
            generateQRCodeBitmapLegacy(content, style, size, foregroundColor, backgroundColor, backgroundType, gradientStartColor, gradientEndColor)
        }
    }

    @Composable
    fun rememberQRCode(
        content: String,
        style: QRCodeStyle = QRCodeStyle.BASIC,
        size: Int = 200
    ): ImageBitmap {
        return remember(content, style, size) {
            generateQRCodeBitmapLegacy(content, style, size, AndroidColor.BLACK, AndroidColor.WHITE)
        }
    }

    private fun generateQRCodeBitmapLegacy(
        content: String,
        style: QRCodeStyle,
        size: Int,
        foregroundColor: Int,
        backgroundColor: Int,
        backgroundType: QRBackgroundType = QRBackgroundType.SOLID,
        gradientStartColor: Int = AndroidColor.WHITE,
        gradientEndColor: Int = AndroidColor.LTGRAY
    ): ImageBitmap {
        if (content.isBlank()) {
            return createPlaceholderBitmap(size)
        }

        val data = QrData.Text(content)

        val options = createQrVectorOptions {
            padding = 0.1f

            colors {
                dark = QrVectorColor.Solid(foregroundColor)
                light = when (backgroundType) {
                    QRBackgroundType.SOLID -> QrVectorColor.Solid(backgroundColor)
                    QRBackgroundType.LINEAR_GRADIENT -> QrVectorColor.LinearGradient(
                        listOf(0f to gradientStartColor, 1f to gradientEndColor),
                        QrVectorColor.LinearGradient.Orientation.Vertical
                    )
                    QRBackgroundType.RADIAL_GRADIENT -> QrVectorColor.RadialGradient(
                        listOf(0f to gradientStartColor, 1f to gradientEndColor)
                    )
                }
            }

            shapes {
                darkPixel = getPixelShape(style)
                lightPixel = QrVectorPixelShape.Default
                ball = getBallShape(style)
                frame = getFrameShape(style)
            }
        }

        val drawable = QrCodeDrawable(data, options)

        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawable.setBounds(0, 0, size, size)
        drawable.draw(canvas)

        return bitmap.asImageBitmap()
    }

    private fun getPixelShape(style: QRCodeStyle): QrVectorPixelShape {
        return when (style) {
            QRCodeStyle.BASIC -> QrVectorPixelShape.Default
            QRCodeStyle.CIRCLE -> QrVectorPixelShape.Circle()
            QRCodeStyle.ROUNDED -> QrVectorPixelShape.RoundCorners(0.5f)
            QRCodeStyle.GRADIENT -> QrVectorPixelShape.Default
            QRCodeStyle.ARTISTIC -> QrVectorPixelShape.Circle()
            QRCodeStyle.DOT -> QrVectorPixelShape.Circle()
            QRCodeStyle.RHOMBUS -> QrVectorPixelShape.Rhombus()
            QRCodeStyle.STAR -> QrVectorPixelShape.Default
            QRCodeStyle.ROUNDED_VERTICAL -> QrVectorPixelShape.Default
            QRCodeStyle.ROUNDED_HORIZONTAL -> QrVectorPixelShape.Default
            else -> QrVectorPixelShape.Default
        }
    }

    private fun getBallShape(style: QRCodeStyle): QrVectorBallShape {
        return when (style) {
            QRCodeStyle.CIRCLE_BALL -> QrVectorBallShape.Circle(1f)
            QRCodeStyle.ROUNDED_BALL -> QrVectorBallShape.RoundCorners(0.25f)
            QRCodeStyle.RHOMBUS_BALL -> QrVectorBallShape.Rhombus()
            else -> QrVectorBallShape.RoundCorners(0.25f)
        }
    }

    private fun getFrameShape(style: QRCodeStyle): QrVectorFrameShape {
        return when (style) {
            QRCodeStyle.CIRCLE_FRAME -> QrVectorFrameShape.Default
            QRCodeStyle.ROUNDED_FRAME -> QrVectorFrameShape.RoundCorners(0.25f, 1f)
            else -> QrVectorFrameShape.RoundCorners(0.25f, 1f)
        }
    }

    private fun AppErrorCorrectionLevel.toCustomQrLevel(): QrErrorCorrectionLevel {
        return when (this) {
            AppErrorCorrectionLevel.LOW -> QrErrorCorrectionLevel.Low
            AppErrorCorrectionLevel.MEDIUM -> QrErrorCorrectionLevel.Medium
            AppErrorCorrectionLevel.QUARTILE -> QrErrorCorrectionLevel.MediumHigh
            AppErrorCorrectionLevel.HIGH -> QrErrorCorrectionLevel.High
        }
    }

    private fun getGradientOrientation(angle: Float): QrVectorColor.LinearGradient.Orientation {
        val normalizedAngle = angle % 360.0
        return when {
            normalizedAngle in 337.5..360.0 || normalizedAngle in 0.0..22.5 -> QrVectorColor.LinearGradient.Orientation.Vertical
            normalizedAngle in 22.5..67.5 -> QrVectorColor.LinearGradient.Orientation.Horizontal
            normalizedAngle in 67.5..112.5 -> QrVectorColor.LinearGradient.Orientation.Horizontal
            normalizedAngle in 112.5..157.5 -> QrVectorColor.LinearGradient.Orientation.Horizontal
            normalizedAngle in 157.5..202.5 -> QrVectorColor.LinearGradient.Orientation.Vertical
            normalizedAngle in 202.5..247.5 -> QrVectorColor.LinearGradient.Orientation.Horizontal
            normalizedAngle in 247.5..292.5 -> QrVectorColor.LinearGradient.Orientation.Horizontal
            else -> QrVectorColor.LinearGradient.Orientation.Vertical
        }
    }
}
