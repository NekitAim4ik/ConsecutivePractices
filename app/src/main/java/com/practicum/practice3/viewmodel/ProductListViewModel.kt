package com.practicum.practice3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.practice3.domain.model.Product
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.usecase.GetProductsUseCase
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
    val errorMessage: String? = null,
    val selectedCategory: String? = null
)

sealed interface ProductListUiEvent {
    data object LoadProducts : ProductListUiEvent
    data class OnProductClicked(val productCode: String) : ProductListUiEvent
}

class ProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts(category: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = getProductsUseCase(category = category)

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, products = result.data, selectedCategory = category)
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                }
                is Resource.Loading -> {  }
            }
        }
    }
}