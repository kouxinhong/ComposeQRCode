package com.qrcode.app.ui.screens

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.qrcode.scanner.result.ScanResult
import com.qrcode.scanner.ui.QRCodeScannerScreen

/**
 * 测试二维码扫描页面
 * 
 * 用于测试qrcode-scanner-library库的功能
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TestScanScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var scanResult by remember { mutableStateOf<String?>(null) }
    
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "二维码扫描测试",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (cameraPermissionState.status.isGranted) {
            // 权限已授予，显示扫描界面
            QRCodeScannerScreen(
                onScanResult = { result ->
                    when (result) {
                        is ScanResult.Success -> {
                            scanResult = result.content
                            showToast(context, "扫描成功: ${result.content}")
                        }
                        is ScanResult.Cancelled -> {
                            showToast(context, "扫描取消")
                        }
                        is ScanResult.Error -> {
                            showToast(context, "扫描错误: ${result.message}")
                        }
                    }
                },
                onBackPressed = onBackClick
            )
        } else {
            // 请求权限
            Button(
                onClick = { cameraPermissionState.launchPermissionRequest() }
            ) {
                Text("请求相机权限")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        scanResult?.let { result ->
            Text(
                text = "扫描结果: $result",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/**
 * 显示Toast消息
 */
private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}