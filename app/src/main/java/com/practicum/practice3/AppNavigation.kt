package com.practicum.practice3

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.practicum.practice3.screens.ProductDetailsScreen
import com.practicum.practice3.screens.ProductListScreen


sealed class Screen(val route: String) {

    data object Home : Screen("home")
    data object Favorites : Screen("favorites")
    data object Profile : Screen("profile")


    data object ProductDetails : Screen("product_details/{productCode}") {
        fun createRoute(productCode: String) = "product_details/$productCode"
    }
}


/*
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "product_list") {

        composable("product_list") {
            ProductListScreen(
                onNavigateToDetails = { productCode ->
                    navController.navigate("product_details/$productCode")
                }
            )
        }

        composable(
            route = "product_details/{productCode}",
            arguments = listOf(navArgument("productCode") { type = NavType.StringType })
        ) { backStackEntry ->

            val productCode = backStackEntry.arguments?.getString("productCode") ?: ""

            ProductDetailsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}*/
