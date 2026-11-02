package com.practicum.practice3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.practice3.dto.Product
import com.practicum.practice3.storage.MockProductRepository
import com.practicum.practice3.storage.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ProductListUiEvent {
    data object LoadProducts : ProductListUiEvent
    data class OnProductClicked(val productCode: String) : ProductListUiEvent
}

class ProductListViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    init {
        onEvent(ProductListUiEvent.LoadProducts)
    }

    fun onEvent(event: ProductListUiEvent) {
        when (event) {
            is ProductListUiEvent.LoadProducts -> loadProducts()
            is ProductListUiEvent.OnProductClicked -> {}
        }
    }

    private fun loadProducts() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                delay(800)

                val products = repository.getProducts()
                _uiState.update { it.copy(products = products, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}