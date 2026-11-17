package com.practicum.practice3.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.practicum.practice3.viewmodel.FavoritesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoritesScreen(
    onNavigateToDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.items.isEmpty() -> {
            PlaceholderScreen(
                title = "Избранное",
                subtitle = "Здесь будут ваши любимые продукты"
            )
        }
        else -> {
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(state.items) { product ->
                    ProductListItem(
                        product = product,
                        trailing = {
                            IconButton(onClick = { viewModel.onRemoveClicked(product.code) }) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Убрать из избранного",
                                    tint = Color.Red
                                )
                            }
                        },
                        onClick = { onNavigateToDetails(product.code) }
                    )
                }
            }
        }
    }
}