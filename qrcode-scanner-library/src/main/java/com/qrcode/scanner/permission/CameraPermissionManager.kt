package com.qrcode.scanner.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * 相机权限管理器
 */
object CameraPermissionManager {

    /**
     * 检查相机权限是否已授予
     */
    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
}

/**
 * 相机权限状态
 */
sealed class PermissionState {
    data object Granted : PermissionState()
    data object Denied : PermissionState()
    data object ShouldShowRationale : PermissionState()
}

/**
 * 相机权限处理的Composable
 */
@Composable
fun rememberCameraPermissionState(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
): Pair<PermissionState, () -> Unit> {
    val context = LocalContext.current
    var permissionState by remember {
        mutableStateOf<PermissionState>(
            if (CameraPermissionManager.hasCameraPermission(context)) {
                PermissionState.Granted
            } else {
                PermissionState.Denied
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionState = if (isGranted) {
                onPermissionGranted()
                PermissionState.Granted
            } else {
                onPermissionDenied()
                PermissionState.Denied
            }
        }
    )

    val requestPermission = {
        launcher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(Unit) {
        if (permissionState is PermissionState.Denied) {
            requestPermission()
        }
    }

    return permissionState to requestPermission
}