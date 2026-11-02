package com.practicum.practice3.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.practice3.dto.Product
import com.practicum.practice3.storage.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ProductDetailsUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ProductDetailsUiEvent {
    data class LoadProduct(val productCode: String) : ProductDetailsUiEvent
    data object OnBackPressed : ProductDetailsUiEvent
}


class ProductDetailsViewModel(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun loadProduct(productCode: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, product = null) }

        viewModelScope.launch {
            try {
                delay(500)

                val product = repository.getProductByCode(productCode)
                if (product != null) {
                    _uiState.update { it.copy(isLoading = false, product = product) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Продукт не найден") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Ошибка загрузки: ${e.message}") }
            }
        }
    }

}