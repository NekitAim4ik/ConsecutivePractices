package com.practicum.practice3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.practice3.domain.model.Product
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.usecase.GetProductByCodeUseCase
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
    private val getProductByCodeUseCase: GetProductByCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun loadProduct(productCode: String) {

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = getProductByCodeUseCase(productCode)

            when (result) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, product = result.data) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is Resource.Loading -> {  }
            }
        }
    }
}