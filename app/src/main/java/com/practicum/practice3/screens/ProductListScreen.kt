package com.practicum.practice3.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.practicum.practice3.ui.model.ProductListItemUi
import com.practicum.practice3.viewmodel.ProductListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (String) -> Unit,
    viewModel: ProductListViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.errorMessage != null -> {
                ErrorState(
                    message = state.errorMessage!!,
                    onRetry = viewModel::loadProducts
                )
            }
            state.products.isEmpty() -> {
                Text(
                    text = "Ничего не найдено. Попробуйте изменить фильтры",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            else -> {
                LazyColumn {
                    items(state.products) { product ->
                        ProductListItem(product = product) { onNavigateToDetails(product.code) }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(message, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry) { Text("Повторить") }
        }
    }
}

@Composable
fun ProductListItem(
    product: ProductListItemUi,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = product.brands,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(text = product.quantity, style = MaterialTheme.typography.bodySmall)
            }
            product.nutriscore?.let { grade ->
                Surface(
                    color = grade.color,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = grade.letter,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            trailing?.invoke()
        }
    }
}
