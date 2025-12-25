package com.qrcode.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.qrcode.app.ui.navigation.QRCodeApp
import com.qrcode.app.ui.theme.QRCodeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 启用边缘到边缘显示，但使用Theme.kt中定义的颜色
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QRCodeApp()
                }
            }
        }
    }
}