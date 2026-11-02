package com.practicum.practice3

sealed class Screen(val route: String) {

    data object Home : Screen("home")
    data object Favorites : Screen("favorites")
    data object Profile : Screen("profile")
    data object Filters : Screen("filters")

    data object ProductDetails : Screen("product_details/{productCode}") {
        fun createRoute(productCode: String) = "product_details/$productCode"
    }
}
