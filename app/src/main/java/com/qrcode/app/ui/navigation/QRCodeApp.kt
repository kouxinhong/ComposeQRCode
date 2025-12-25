package com.qrcode.app.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qrcode.app.ui.screens.GenerateScreen
import com.qrcode.app.ui.screens.HistoryScreen
import com.qrcode.app.ui.screens.HomeScreen
import com.qrcode.app.ui.screens.ResultScreen
import com.qrcode.app.ui.screens.ScanScreen
import com.qrcode.app.ui.screens.SettingsScreen
import com.qrcode.app.ui.navigation.Routes

@Composable
fun QRCodeApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300)) }
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onScanClick = { navController.navigate(Routes.SCAN) },
                onGenerateClick = { navController.navigate(Routes.GENERATE) },
                onHistoryClick = { navController.navigate(Routes.HISTORY) },
                onSettingsClick = { navController.navigate(Routes.SETTINGS) }
            )
        }
        composable(Routes.SCAN) {
            ScanScreen(
                onBackClick = { navController.popBackStack() },
                onScanResult = { result -> 
                    navController.navigate(Routes.buildResultRoute(result)) 
                }
            )
        }
        composable(Routes.GENERATE) {
            GenerateScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.HISTORY) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.RESULT) { backStackEntry ->
            val scanResult = backStackEntry.arguments?.getString("scanResult") ?: ""
            ResultScreen(
                result = scanResult,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}