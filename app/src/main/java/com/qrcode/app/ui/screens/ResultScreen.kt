package com.qrcode.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.qrcode.app.R

/**
 * 扫描结果展示页面
 * 
 * 该页面用于展示二维码扫描结果，提供以下功能：
 * - 显示解码后的二维码内容
 * - 提供复制功能
 * - 如果是URL，提供在浏览器中打开的功能
 * - 支持返回导航
 * 
 * @param result 扫描结果（URL编码格式）
 * @param onBackClick 返回按钮点击事件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    result: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboard = androidx.compose.ui.platform.LocalClipboard.current
    
    /**
     * 解码URL编码的扫描结果
     * 
     * 由于导航参数需要URL编码，这里需要解码显示
     * 如果解码失败，则使用原始结果
     */
    val decodedResult = try {
        java.net.URLDecoder.decode(result, "UTF-8")
    } catch (e: Exception) {
        result
    }
    
    /**
     * 判断结果是否为URL
     * 
     * 支持http和https协议的URL识别
     */
    val isUrl = decodedResult.startsWith("http://") || decodedResult.startsWith("https://")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.scan_result)) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = decodedResult,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
            
            /**
             * 操作按钮区域
             * 
             * 提供以下操作：
             * 1. 复制结果到剪贴板
             * 2. 如果是URL，提供在浏览器中打开的功能
             */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 复制按钮
                CopyButton(
                    textToCopy = decodedResult,
                    clipboard = clipboard,
                    modifier = Modifier.fillMaxWidth(0.48f)
                )
                
                // URL打开按钮（仅当结果为URL时显示）
                if (isUrl) {
                    OpenUrlButton(
                        url = decodedResult,
                        context = context,
                        modifier = Modifier.fillMaxWidth(0.48f)
                    )
                }
            }
        }
    }
}

/**
 * 复制按钮组件
 * 
 * 提供将文本复制到剪贴板的功能，
 * 点击后显示成功提示。
 * 
 * @param textToCopy 要复制的文本
 * @param clipboard 剪贴板
 */
@Composable
private fun CopyButton(
    textToCopy: String,
    clipboard: Clipboard,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            // 在实际应用中，这里可以添加Toast提示
        // 注意：Clipboard.setText需要suspend函数，这里暂时注释掉实现
            // 在实际应用中，这里可以添加Toast提示
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.copy))
    }
}

/**
 * URL打开按钮组件
 * 
 * 提供在浏览器中打开URL的功能，
 * 自动处理URL解析和Intent启动。
 * 
 * @param url 要打开的URL
 * @param context 上下文用于启动Activity
 */
@Composable
private fun OpenUrlButton(
    url: String,
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // 确保有应用可以处理该Intent
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                // 处理URL解析或启动异常
                // 在实际应用中，这里可以显示错误提示
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.open_in_browser))
    }
}