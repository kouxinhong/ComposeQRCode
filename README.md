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

## 许可证

MIT License
