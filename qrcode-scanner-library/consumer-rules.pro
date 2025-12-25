# 保持ML Kit相关类不被混淆
-keep class com.google.mlkit.vision.barcode.** { *; }
-keep class com.google.mlkit.vision.common.** { *; }

# 保持CameraX相关类
-keep class androidx.camera.** { *; }

# 保持库的核心API类
-keep class com.qrcode.scanner.** { *; }