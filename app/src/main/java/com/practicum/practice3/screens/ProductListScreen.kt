package com.practicum.practice3.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.practice3.domain.model.Product
import com.practicum.practice3.storage.MockProductRepository
import com.practicum.practice3.viewmodel.ProductListUiEvent
import com.practicum.practice3.viewmodel.ProductListViewModel
import com.practicum.practice3.viewmodel.ProductListViewModelFactory

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (String) -> Unit,
    viewModel: ProductListViewModel = viewModel(factory = ProductListViewModelFactory()),
) {

    val state by viewModel.uiState.collectAsState()

    val categories = listOf(null, "chocolates", "beverages", "cereals")
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = category
                        viewModel.loadProducts(category)
                    },
                    label = { Text(category ?: "Все") }
                )
            }
        }

        when {
            state.isLoading -> {  }
            state.errorMessage != null -> {
                ErrorState(
                    message = state.errorMessage!!,
                    onRetry = { viewModel.loadProducts(selectedCategory) }
                )
            }
            else -> {
                LazyColumn {
                    items(state.products) { product ->
                        ProductListItem(product) { onNavigateToDetails(product.code) }
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
fun ProductListItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.productName, style = MaterialTheme.typography.titleMedium)
                Text(text = product.brands, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = product.quantity, style = MaterialTheme.typography.bodySmall)
            }
            if (product.nutriscoreGrade != null) {
                Surface(
                    color = when(product.nutriscoreGrade.lowercase()) {
                        "a" -> Color.Green
                        "b" -> Color(0xFF8BC34A)
                        "c" -> Color.Yellow
                        "d" -> Color(0xFFFF9800)
                        "e" -> Color.Red
                        else -> Color.Gray
                    },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = product.nutriscoreGrade.uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
