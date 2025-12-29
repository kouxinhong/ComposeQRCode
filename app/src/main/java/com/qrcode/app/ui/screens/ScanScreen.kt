package com.qrcode.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qrcode.app.R
import com.qrcode.scanner.ui.QRCodeScannerScreen
import com.qrcode.scanner.result.ScanResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.net.URLEncoder

/**
 * 二维码扫描页面
 * 
 * 使用qrcode-scanner-library库的扫描功能
 * 
 * @param onBackClick 返回按钮点击事件
 * @param onScanResult 扫描结果回调，返回扫描到的二维码内容
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    onBackClick: () -> Unit,
    onScanResult: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.scan_qr)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            QRCodeScannerScreen(
                onScanResult = { result ->
                    when (result) {
                        is ScanResult.Success -> {
                            val decodedContent = try {
                                String(result.content.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
                            } catch (e: Exception) {
                                result.content
                            }
                            onScanResult(decodedContent)
                        }
                        is ScanResult.Cancelled -> {
                            // 扫描取消，不做处理
                        }
                        is ScanResult.Error -> {
                            // 扫描错误，可以在这里显示错误信息
                        }
                    }
                },
                onBackPressed = onBackClick
            )
        }
    }
}