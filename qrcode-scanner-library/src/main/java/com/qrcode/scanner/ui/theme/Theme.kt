package com.qrcode.scanner.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * 扫描库主题
 */
@Composable
fun QRCodeScannerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}