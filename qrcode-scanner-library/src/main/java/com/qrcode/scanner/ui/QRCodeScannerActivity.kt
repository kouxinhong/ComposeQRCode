package com.qrcode.scanner.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.qrcode.scanner.result.ScanResult
import com.qrcode.scanner.ui.theme.QRCodeScannerTheme

/**
 * 二维码扫描Activity
 */
class QRCodeScannerActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 设置状态栏颜色
        window.statusBarColor = androidx.compose.ui.graphics.Color.Black.toArgb()
        
        setContent {
            QRCodeScannerTheme {
                QRCodeScannerScreen(
                    onScanResult = { result ->
                        val intent = android.content.Intent().apply {
                            putExtra("scan_result", result)
                        }
                        setResult(android.app.Activity.RESULT_OK, intent)
                        finish()
                    },
                    onBackPressed = {
                        val intent = android.content.Intent().apply {
                            putExtra("scan_result", ScanResult.Cancelled)
                        }
                        setResult(android.app.Activity.RESULT_CANCELED, intent)
                        finish()
                    }
                )
            }
        }
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = android.content.Intent().apply {
            putExtra("scan_result", ScanResult.Cancelled)
        }
        setResult(android.app.Activity.RESULT_CANCELED, intent)
        finish()
    }
}