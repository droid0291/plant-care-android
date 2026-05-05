package com.plantcare.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.plantcare.app.ui.screens.home.HomeScreen
import com.plantcare.app.ui.screens.result.ResultScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Result : Screen("result/{analysisJson}") {
        fun createRoute(analysisJson: String): String {
            val encoded = URLEncoder.encode(analysisJson, StandardCharsets.UTF_8.toString())
            return "result/$encoded"
        }
    }
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(onAnalysisComplete = { analysisJson ->
                navController.navigate(Screen.Result.createRoute(analysisJson))
            })
        }
        composable(
            route = Screen.Result.route,
            arguments = listOf(navArgument("analysisJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("analysisJson") ?: return@composable
            val decoded = URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
            ResultScreen(
                analysisJson = decoded,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
