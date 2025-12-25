package com.qrcode.app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * 相机权限管理器
 * 
 * 负责处理相机权限的请求和状态管理，
 * 提供简洁的权限检查接口。
 */
class CameraPermissionManager {
    
    /**
     * 检查相机权限是否已授予
     */
    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * 创建权限请求启动器
     */
    @Composable
    fun rememberPermissionLauncher(
        onPermissionResult: (Boolean) -> Unit
    ): androidx.activity.compose.ManagedActivityResultLauncher<String, Boolean> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = onPermissionResult
        )
    }
}

/**
 * 记住相机权限状态
 * 
 * 在Composable中记住相机权限的当前状态，
 * 并在权限变化时自动更新。
 */
@Composable
fun rememberCameraPermissionState(): CameraPermissionState {
    val context = LocalContext.current
    val permissionManager = remember { CameraPermissionManager() }
    
    var hasPermission by remember {
        mutableStateOf(permissionManager.hasCameraPermission(context))
    }
    
    val launcher = permissionManager.rememberPermissionLauncher { isGranted ->
        hasPermission = isGranted
    }
    
    return CameraPermissionState(
        hasPermission = hasPermission,
        launcher = launcher
    )
}

/**
 * 相机权限状态
 * 
 * 封装相机权限的状态和操作
 */
data class CameraPermissionState(
    val hasPermission: Boolean,
    val launcher: androidx.activity.compose.ManagedActivityResultLauncher<String, Boolean>
) {
    /**
     * 请求相机权限
     */
    fun requestPermission() {
        launcher.launch(Manifest.permission.CAMERA)
    }
}