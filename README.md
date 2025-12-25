# QRCode3 - Android二维码应用

一个功能完整的Android二维码扫描与生成应用，使用Kotlin和Jetpack Compose开发。

## 功能特性

### 核心功能
- **二维码扫描** - 使用CameraX和Google ML Kit实现快速准确的二维码扫描
- **二维码生成** - 支持自定义样式的二维码生成，包括眼型、像素点、logo等
- **扫描历史** - 自动保存扫描记录，支持历史管理
- **URL处理** - 支持URL编码/解码，处理特殊字符

### 技术特点
- 采用Material Design 3设计规范
- 使用Jetpack Compose构建现代声明式UI
- 基于CameraX的相机预览（优化至640x480分辨率）
- Room数据库持久化存储
- 导航组件实现页面路由

## 项目结构

```
QRCode3/
├── app/                          # 主应用模块
│   ├── src/main/java/com/qrcode/app/
│   │   ├── MainActivity.kt       # 应用入口
│   │   ├── data/
│   │   │   └── model/            # 数据模型
│   │   │       └── ScanHistory.kt
│   │   ├── model/                # 业务模型
│   │   │   └── QRoseStyleConfig.kt
│   │   ├── ui/
│   │   │   ├── components/       # 可复用组件
│   │   │   │   ├── QRCodeGenerator.kt
│   │   │   │   ├── QRStyleSelectorDialog.kt
│   │   │   │   ├── QRoseStyleDialog.kt
│   │   │   │   └── ScanningIndicator.kt
│   │   │   ├── navigation/       # 导航配置
│   │   │   │   ├── QRCodeApp.kt
│   │   │   │   └── Routes.kt
│   │   │   ├── screens/          # 页面
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   ├── ScanScreen.kt
│   │   │   │   ├── GenerateScreen.kt
│   │   │   │   ├── HistoryScreen.kt
│   │   │   │   ├── ResultScreen.kt
│   │   │   │   └── SettingsScreen.kt
│   │   │   └── theme/            # 主题配置
│   │   │       ├── Color.kt
│   │   │       ├── Theme.kt
│   │   │       └── Type.kt
│   │   └── utils/                # 工具类
│   │       ├── CameraPermissionManager.kt
│   │       ├── QRCodeConfig.kt
│   │       ├── QRCodeStyle.kt
│   │       └── QRoseComposeUtils.kt
│   └── build.gradle.kts
├── qrcode-scanner-library/       # 二维码扫描库模块
│   └── src/main/java/com/qrcode/scanner/
│       ├── core/                 # 核心实现
│       │   ├── CameraManager.kt
│       │   └── QRCodeAnalyzer.kt
│       ├── permission/           # 权限管理
│       │   └── CameraPermissionManager.kt
│       ├── ui/                   # 扫描界面
│       │   ├── QRCodeScanner.kt
│       │   ├── QRCodeScannerScreen.kt
│       │   ├── QRCodeScannerActivity.kt
│       │   ├── ScanAnimation.kt
│       │   └── theme/Theme.kt
│       └── result/               # 扫描结果
│           └── ScanResult.kt
├── qrcode-generator-library/     # 二维码生成库模块
│   └── src/main/java/com/qrcode/generator/
│       ├── QRCodeGenerator.kt    # 主入口类
│       ├── models/               # 数据模型
│       │   └── QRGeneratorConfig.kt
│       ├── utils/                # 工具类
│       │   └── QRoseGenerator.kt
│       └── theme/                # 主题配置
│           └── Theme.kt
├── build.gradle.kts              # 项目根配置
├── settings.gradle.kts           # 项目设置
└── libs.versions.toml            # 版本目录
```

## 技术栈

| 技术 | 用途 |
|------|------|
| Kotlin | 主要开发语言 |
| Jetpack Compose | UI框架 |
| CameraX | 相机功能 |
| Google ML Kit | 二维码识别 |
| QRose | 二维码生成 |
| Room | 本地数据库 |
| Coil | 图片加载 |
| Accompanist | 权限处理 |
| Navigation Compose | 页面导航 |

## QRCode Scanner Library - 二维码扫描库

### 概述

`qrcode-scanner-library` 是一个功能完整的二维码扫描库，基于CameraX和Google ML Kit构建，支持Jetpack Compose。

### 主要特性

- **高性能扫描** - 使用CameraX优化预览性能（640x480分辨率）
- **ML Kit识别** - 集成Google ML Kit实现准确的二维码识别
- **权限管理** - 内置相机权限处理
- **自定义配置** - 支持扫描线颜色、提示文字等自定义配置
- **Jetpack Compose支持** - 提供Compose组件和Activity两种使用方式

### 快速开始

#### 添加依赖

在`build.gradle.kts`中添加：

```kotlin
implementation(project(":qrcode-scanner-library"))
```

#### 基本用法

**方式一：使用ActivityResultContract**

```kotlin
import androidx.activity.result.contract.ActivityResultContracts
import com.qrcode.scanner.QRCodeScanner
import com.qrcode.scanner.result.ScanResult

class MainActivity : ComponentActivity() {
    
    private val scanLauncher = registerForActivityResult(
        QRCodeScanner.getScanContract()
    ) { result ->
        when (result) {
            is ScanResult.Success -> {
                val content = result.content
                // 处理扫描结果
            }
            is ScanResult.Cancelled -> {
                // 用户取消扫描
            }
            is ScanResult.Error -> {
                val error = result.exception
                // 处理错误
            }
        }
    }
    
    fun startScan() {
        scanLauncher.launch(Unit)
    }
}
```

**方式二：使用Compose组件**

```kotlin
import com.qrcode.scanner.ui.QRCodeScannerScreen

@Composable
fun ScanScreen(
    onScanResult: (ScanResult) -> Unit
) {
    QRCodeScannerScreen(
        onScanResult = onScanResult,
        showTorchButton = true,
        showGalleryButton = true,
        scanHintText = "将二维码放入框内即可自动扫描"
    )
}
```

**方式三：启动Activity**

```kotlin
val intent = QRCodeScanner.createScanIntent(context)
startActivity(intent)
```

### API 参考

#### QRCodeScanner

| 方法 | 说明 |
|------|------|
| `getScanContract()` | 获取ActivityResultContract |
| `createScanIntent(context: Context)` | 创建启动Intent |

#### ScanResult

```kotlin
sealed class ScanResult {
    data class Success(val content: String) : ScanResult()
    data object Cancelled : ScanResult()
    data class Error(val exception: Exception) : ScanResult()
}
```

#### ScanConfig

```kotlin
data class ScanConfig(
    val showTorchButton: Boolean = true,
    val showGalleryButton: Boolean = true,
    val scanHintText: String = "将二维码放入框内即可自动扫描",
    val scanLineColor: Int = 0xFF00FF00.toInt()
)
```

### 权限配置

库会自动处理相机权限，但需要在`AndroidManifest.xml`中声明：

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera.any" />
```

## QRCode Generator Library - 二维码生成库

### 概述

`qrcode-generator-library` 是一个功能强大的二维码生成库，基于QRose构建，支持丰富的自定义样式和Jetpack Compose集成。

### 主要特性

- **多种样式** - 支持方形、圆形、圆角、菱形等多种像素形状
- **眼部样式** - 支持方形、圆形、圆角等眼部形状
- **渐变效果** - 支持线性渐变、径向渐变背景
- **Logo支持** - 支持在二维码中心添加Logo
- **预设样式** - 内置商务、艺术、优雅、活力、极简等多种预设
- **Jetpack Compose** - 提供Compose组件支持

### 快速开始

#### 添加依赖

在`build.gradle.kts`中添加：

```kotlin
implementation(project(":qrcode-generator-library"))
```

#### 基本用法

**方式一：使用QRCodeGenerator对象**

```kotlin
import com.qrcode.generator.QRCodeGenerator
import com.qrcode.generator.models.ShapePreset

// 生成基础二维码
val bitmap = QRCodeGenerator.generateBitmap(
    content = "https://example.com",
    config = QRGeneratorConfig(size = 512)
)

// 使用预设样式生成
val businessQRCode = QRCodeGenerator.generateWithPreset(
    content = "https://example.com",
    preset = ShapePreset.Business,
    size = 512
)

val artisticQRCode = QRCodeGenerator.generateWithPreset(
    content = "https://example.com",
    preset = ShapePreset.Artistic,
    size = 512
)
```

**方式二：使用Compose组件**

```kotlin
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import com.qrcode.generator.QRCodeGenerator
import com.qrcode.generator.models.QRGeneratorConfig
import com.qrcode.generator.models.ShapePreset

@Composable
fun QRCodeDisplay(content: String) {
    val bitmap = QRCodeGenerator(
        content = content,
        config = QRGeneratorConfig(size = 256)
    )
    
    Image(
        bitmap = bitmap,
        contentDescription = "二维码"
    )
}

// 使用简化版本
@Composable
fun SimpleQRCodeDisplay(content: String) {
    val bitmap = SimpleQRCodeGenerator(
        content = content,
        size = 200,
        foregroundColor = Color.Black,
        backgroundColor = Color.White
    )
    
    Image(
        bitmap = bitmap,
        contentDescription = "二维码"
    )
}

// 使用预设样式
@Composable
fun PresetQRCodeDisplay(content: String) {
    val bitmap = PresetQRCodeGenerator(
        content = content,
        preset = ShapePreset.Business
    )
    
    Image(
        bitmap = bitmap,
        contentDescription = "二维码"
    )
}
```

### 预设样式

库内置5种预设样式，可直接使用：

| 预设 | 特点 | 适用场景 |
|------|------|----------|
| `Business` | 蓝色方形，简洁专业 | 企业应用、名片 |
| `Artistic` | 粉色圆形，活泼艺术 | 创意应用、艺术场景 |
| `Elegant` | 深灰色圆角，优雅低调 | 高端应用、时尚场景 |
| `Vibrant` | 橙色圆形，活力四射 | 促销活动、活动场景 |
| `Minimal` | 黑色方形，极简主义 | 极简应用、黑白设计 |

### 自定义配置

```kotlin
import com.qrcode.generator.models.QRGeneratorConfig
import com.qrcode.generator.models.ErrorCorrectionLevel
import io.github.alexzhirkevich.qrose.models.QrCodeShapes
import io.github.alexzhirkevich.qrose.models.QrCodeColors
import io.github.alexzhirkevich.qrose.models.QrBackground
import androidx.compose.ui.graphics.Color

// 使用构建器创建配置
val config = QRGeneratorConfig.Builder()
    .setSize(512)
    .setPadding(0.1f)
    .setErrorCorrectionLevel(ErrorCorrectionLevel.HIGH)
    .setShapes(QrCodeShapes.Default.copy(
        darkPixel = QrCodeShapes.Default.darkPixel.copy(
            shape = Shape.Rounded(0.3f)
        )
    ))
    .setColors(QrCodeColors.Solid(Color(0xFF1976D2)))
    .setBackground(QrBackground.Solid(Color.White))
    .build()

// 生成二维码
val bitmap = QRCodeGenerator.generateBitmap("内容", config)
```

### 高级功能

#### 生成带Logo的二维码

```kotlin
import androidx.compose.ui.graphics.asImageBitmap
import io.github.alexzhirkevich.qrose.models.QrLogo

val logoBitmap = // 获取Logo位图
val config = QRGeneratorConfig(
    size = 512,
    logo = QrLogo(
        bitmap = logoBitmap.asAndroidBitmap(),
        size = 0.2f,  // Logo占二维码比例
        padding = 0.05f
    )
)
val bitmap = QRCodeGenerator.generateBitmap("内容", config)
```

#### 生成渐变二维码

```kotlin
import io.github.alexzhirkevich.qrose.models.QrBackground
import androidx.compose.ui.graphics.Color

val config = QRGeneratorConfig(
    size = 512,
    colors = QrCodeColors.Solid(Color(0xFF1976D2)),
    background = QrBackground.LinearGradient(
        colors = listOf(Color(0xFFFCE4EC), Color(0xFFF8BBD9)),
        angle = 45f
    )
)
val bitmap = QRCodeGenerator.generateBitmap("内容", config)
```

### API 参考

#### QRCodeGenerator

| 方法 | 说明 |
|------|------|
| `generateBitmap(content: String, config: QRGeneratorConfig)` | 生成二维码位图 |
| `configBuilder()` | 获取配置构建器 |
| `generateWithPreset(content: String, preset: ShapePreset, size: Int)` | 使用预设生成 |

#### QRGeneratorConfig

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `size` | Int | 512 | 二维码尺寸 |
| `padding` | Float | 0.1f | 边距比例 |
| `errorCorrectionLevel` | ErrorCorrectionLevel | MEDIUM | 错误纠正级别 |
| `shapes` | QrCodeShapes | Default | 形状配置 |
| `colors` | QrCodeColors | Default | 颜色配置 |
| `background` | QrBackground | Default | 背景配置 |
| `logo` | QrLogo? | null | Logo配置 |
| `frame` | QrFrame? | null | 边框配置 |
| `isInverted` | Boolean | false | 是否反色 |

#### ErrorCorrectionLevel

| 级别 | 纠错率 | 适用场景 |
|------|--------|----------|
| `LOW` | 7% | 干净、清晰的二维码 |
| `MEDIUM` | 15% | 默认选择 |
| `QUARTILE` | 25% | 中等损坏环境 |
| `HIGH` | 30% | 严重损坏环境 |

## 环境要求

- **Android SDK**: 26+ (Android 8.0)
- **目标SDK**: 35 (Android 15)
- **JDK**: 17
- **Gradle**: 8.x

## 快速开始

### 构建项目
```bash
./gradlew build
```

### 安装调试版
```bash
./gradlew installDebug
```

### 运行测试
```bash
./gradlew test
```

### 清理构建
```bash
./gradlew clean
```

## 权限说明

应用需要以下权限：
- `CAMERA` - 扫描二维码
- `INTERNET` - 处理网络相关内容
- `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE` - 读写存储

## 最近更新

- 集成Google ML Kit替代ZXing进行二维码扫描
- 修复导航路由参数编码问题
- 优化相机预览性能（640x480分辨率）
- 添加URL编码/解码处理特殊字符
- 新增二维码生成库模块（qrcode-generator-library）
- 生成库支持5种预设样式和丰富的自定义配置

## 许可证

MIT License
