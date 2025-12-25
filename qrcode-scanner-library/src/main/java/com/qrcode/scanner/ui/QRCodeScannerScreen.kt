package com.qrcode.scanner.ui

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.qrcode.scanner.R
import com.qrcode.scanner.core.CameraManager
import com.qrcode.scanner.core.QRCodeAnalyzer
import com.qrcode.scanner.permission.CameraPermissionManager
import com.qrcode.scanner.permission.rememberCameraPermissionState
import com.qrcode.scanner.result.ScanResult

/**
 * 二维码扫描界面
 * 
 * @param onScanResult 扫描结果回调
 * @param onBackPressed 返回键回调，用于处理返回时的资源释放
 * @param modifier 布局修饰符
 */
@Composable
fun QRCodeScannerScreen(
    onScanResult: (ScanResult) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var hasPermission by remember {
        mutableStateOf(CameraPermissionManager.hasCameraPermission(context))
    }
    
    val (permissionState, requestPermission) = rememberCameraPermissionState(
        onPermissionGranted = { hasPermission = true },
        onPermissionDenied = { onBackPressed() }
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (hasPermission) {
            CameraPreview(
                onScanResult = onScanResult,
                modifier = Modifier.fillMaxSize()
            )
            
            ScanOverlay(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            // 清理相机资源
            CameraManager.releaseInstance()
        }
    }
}

/**
 * 相机预览
 */
@Composable
private fun CameraPreview(
    onScanResult: (ScanResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraManager by remember { mutableStateOf<CameraManager?>(null) }
    
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
            
            val cameraManagerInstance = CameraManager(context, lifecycleOwner)
            cameraManager = cameraManagerInstance
            
            val preview = androidx.camera.core.Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        QRCodeAnalyzer { qrCode ->
                            onScanResult(ScanResult.Success(qrCode))
                        }
                    )
                }
            
            lifecycleOwner.lifecycleScope.launchWhenStarted {
                try {
                    cameraManagerInstance.initializeCamera(preview, imageAnalysis)
                } catch (e: Exception) {
                    // 处理相机初始化错误
                }
            }
            
            previewView
        },
        modifier = modifier
    )
    
    DisposableEffect(Unit) {
        onDispose {
            cameraManager?.shutdown()
        }
    }
}

/**
 * 扫描界面覆盖层
 * 
 * @param modifier 布局修饰符
 */
@Composable
private fun ScanOverlay(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        // 扫描框
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
        ) {
            ScanLineAnimation(
                modifier = Modifier.fillMaxSize(),
                color = Color.Green
            )
        }
        
        // 提示文字
        Text(
            text = "将二维码放入框内即可自动扫描",
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
    }
}