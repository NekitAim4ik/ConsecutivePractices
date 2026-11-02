package com.practicum.practice3.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.practicum.practice3.Screen


data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)


val BottomNavItems = listOf(
    BottomNavItem("Главная", Icons.Filled.Home, Screen.Home.route),
    BottomNavItem("Избранное", Icons.Filled.Favorite, Screen.Favorites.route),
    BottomNavItem("Профиль", Icons.Filled.AccountCircle, Screen.Profile.route)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.Favorites.route,
        Screen.Profile.route
    )

    val showBottomBar = currentDestination?.route in bottomBarRoutes

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Open Food Facts",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },

        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    BottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                ProductListScreen(
                    onNavigateToDetails = { productCode ->
                        navController.navigate(
                            Screen.ProductDetails.createRoute(productCode)
                        )
                    }
                )
            }

            composable(
                route = Screen.ProductDetails.route,
                arguments = listOf(
                    navArgument("productCode") { type = NavType.StringType }
                )
            ) {
                ProductDetailsScreen(
                    productCode = it.arguments?.getString("productCode")!!,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Favorites.route) {
                PlaceholderScreen(
                    title = "Избранное",
                    subtitle = "Здесь будут ваши любимые продукты"
                )
            }

            composable(Screen.Profile.route) {
                PlaceholderScreen(
                    title = "Профиль",
                    subtitle = "Настройки и информация"
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, subtitle: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
