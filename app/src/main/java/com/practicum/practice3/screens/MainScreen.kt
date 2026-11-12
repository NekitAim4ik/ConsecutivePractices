package com.practicum.practice3.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
import com.practicum.practice3.domain.cache.FilterBadgeCache
import org.koin.compose.koinInject


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
fun MainScreen(
    badgeCache: FilterBadgeCache = koinInject()
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val isBadgeVisible by badgeCache.isBadgeVisible.collectAsState()

    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.Favorites.route,
        Screen.Profile.route
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            Screen.Filters.route -> "Фильтры"
                            Screen.Profile.route -> "Профиль"
                            Screen.EditProfile.route -> "Редактирование профиля"
                            else -> "Open Food Facts"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (currentRoute == Screen.Filters.route || currentRoute == Screen.EditProfile.route) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад"
                            )
                        }
                    }
                },
                actions = {
                    if (currentRoute == Screen.Profile.route) {
                        IconButton(onClick = { navController.navigate(Screen.EditProfile.route) }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Редактировать профиль"
                            )
                        }
                    }
                    if (currentRoute == Screen.Home.route) {
                        IconButton(onClick = { navController.navigate(Screen.Filters.route) }) {
                            BadgedBox(badge = { if (isBadgeVisible) Badge() }) {
                                Icon(
                                    imageVector = Icons.Filled.FilterList,
                                    contentDescription = "Фильтры"
                                )
                            }
                        }
                    }
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

            composable(Screen.Filters.route) {
                FiltersScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onNavigateToDetails = { productCode ->
                        navController.navigate(
                            Screen.ProductDetails.createRoute(productCode)
                        )
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            composable(Screen.EditProfile.route) {
                EditProfileScreen(onNavigateBack = { navController.popBackStack() })
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
