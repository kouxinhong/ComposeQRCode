plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("maven-publish")
}

android {
    namespace = "com.qrcode.generator"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.8.2")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    implementation("com.github.alexzhirkevich:custom-qr-generator:1.6.2")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.qrcode"
            artifactId = "qrcode-generator"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name = "QRCode Generator"
                description = "A powerful and customizable QR code generator library for Android"
                url = "https://github.com/qrcode/qrcode-generator"
                licenses {
                    license {
                        name = "MIT"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
                developers {
                    developer {
                        id = "qrcode"
                        name = "QRCode Team"
                        email = "qrcode@example.com"
                    }
                }
                scm {
                    connection = "scm:git@github.com:qrcode/qrcode-generator.git"
                    developerConnection = "scm:git@github.com:qrcode/qrcode-generator.git"
                    url = "https://github.com/qrcode/qrcode-generator"
                }
            }
        }
    }
}
