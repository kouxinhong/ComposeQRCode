# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep QRose library classes
-keep class io.github.alexzhirkevich.qrose.** { *; }

# Keep library public API
-keep public class com.qrcode.generator.** { *; }
