package com.qrcode.scanner

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.qrcode.scanner.ui.QRCodeScannerActivity
import com.qrcode.scanner.result.ScanResult

/**
 * 二维码扫描库的主入口类
 * 提供简洁的API供开发者调用
 */
object QRCodeScanner {

    /**
     * 获取扫描结果的ActivityResultContract
     * 用于在Activity或Fragment中启动扫描
     */
    fun getScanContract(): ActivityResultContract<Unit, ScanResult> {
        return QRCodeScanContract()
    }

    /**
     * 启动扫描Activity的Intent
     */
    fun createScanIntent(context: Context): Intent {
        return Intent(context, QRCodeScannerActivity::class.java)
    }
}

/**
 * 扫描结果的ActivityResultContract
 */
private class QRCodeScanContract : ActivityResultContract<Unit, ScanResult>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return QRCodeScanner.createScanIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ScanResult {
        return intent?.getParcelableExtra("scan_result") ?: ScanResult.Cancelled
    }
}

/**
 * 扫描配置类
 * @param showTorchButton 是否显示手电筒按钮
 * @param showGalleryButton 是否显示相册按钮
 * @param scanHintText 扫描提示文字
 * @param scanLineColor 扫描线颜色
 */
data class ScanConfig(
    val showTorchButton: Boolean = true,
    val showGalleryButton: Boolean = true,
    val scanHintText: String = "将二维码放入框内即可自动扫描",
    val scanLineColor: Int = 0xFF00FF00.toInt()
)