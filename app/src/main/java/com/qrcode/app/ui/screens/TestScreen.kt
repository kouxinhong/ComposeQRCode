package com.qrcode.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@Composable
fun TestScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "二维码测试",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 基础二维码
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("基础二维码", style = MaterialTheme.typography.bodySmall)
                BasicQrCodeView(
                    data = "Hello World",
                    modifier = Modifier.size(120.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("示例二维码", style = MaterialTheme.typography.bodySmall)
                BasicQrCodeView(
                    data = "Example QR",
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 更多基础二维码示例
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("测试二维码1", style = MaterialTheme.typography.bodySmall)
                BasicQrCodeView(
                    data = "QR Code 1",
                    modifier = Modifier.size(120.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("测试二维码2", style = MaterialTheme.typography.bodySmall)
                BasicQrCodeView(
                    data = "QR Code 2",
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 简单的文本显示
        Text(
            text = "此页面展示了多个基础二维码示例",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun BasicQrCodeView(
    data: String,
    modifier: Modifier = Modifier
) {
    // 只使用最基本的rememberQrCodePainter，不添加任何自定义样式
    val qrPainter = rememberQrCodePainter(data = data)
    Image(
        painter = qrPainter,
        contentDescription = "二维码",
        modifier = modifier
    )
}