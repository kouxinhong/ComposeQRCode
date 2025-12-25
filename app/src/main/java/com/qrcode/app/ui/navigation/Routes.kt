package com.qrcode.app.ui.navigation

/**
 * 应用路由常量定义
 * 
 * 遵循Android最佳实践，将所有路由路径定义为常量，
 * 便于统一管理和维护，避免硬编码字符串。
 */
object Routes {
    // 主页面路由
    const val HOME = "home"
    const val SCAN = "scan"
    const val GENERATE = "generate"
    const val HISTORY = "history"
    const val SETTINGS = "settings"
    
    // 结果页面路由（带参数）
    const val RESULT = "result/{scanResult}"
    
    /**
     * 构建结果页面路由路径
     * 
     * @param scanResult 扫描结果内容
     * @return 完整的路由路径
     */
    fun buildResultRoute(scanResult: String): String {
        return "result/${scanResult}"
    }
    
    /**
     * 从结果页面路由中提取扫描结果
     * 
     * @param route 路由路径
     * @return 扫描结果内容，如果格式不匹配则返回空字符串
     */
    fun parseResultFromRoute(route: String): String {
        return if (route.startsWith("result/")) {
            route.substring("result/".length)
        } else {
            ""
        }
    }
}