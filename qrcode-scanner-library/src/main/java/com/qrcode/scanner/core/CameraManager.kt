package com.qrcode.scanner.core

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * 相机管理器
 * 负责相机的初始化和配置
 */
class CameraManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {

    private var cameraProvider: ProcessCameraProvider? = null

    companion object {
        private var instance: CameraManager? = null
        
        /**
         * 释放相机管理器实例
         */
        fun releaseInstance() {
            instance?.shutdown()
            instance = null
        }
    }

    /**
     * 初始化相机
     */
    suspend fun initializeCamera(
        preview: Preview,
        imageAnalysis: ImageAnalysis
    ) = suspendCancellableCoroutine { continuation ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )

                instance = this
                continuation.resume(Unit)
            } catch (e: Exception) {
                continuation.cancel(e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    /**
     * 关闭相机
     */
    fun shutdown() {
        cameraProvider?.unbindAll()
        cameraProvider = null
    }
}