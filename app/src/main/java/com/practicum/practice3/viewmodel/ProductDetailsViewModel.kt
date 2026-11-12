package com.practicum.practice3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.practice3.domain.model.Product
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.repository.FavoritesRepository
import com.practicum.practice3.domain.usecase.GetProductByCodeUseCase
import com.practicum.practice3.ui.model.GradeUi
import com.practicum.practice3.ui.model.toGradeUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ProductDetailsUiState(
    val product: Product? = null,
    val nutriscore: GradeUi? = null,
    val ecoscore: GradeUi? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ProductDetailsViewModel(
    private val getProductByCodeUseCase: GetProductByCodeUseCase,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private var favoriteJob: Job? = null

    fun loadProduct(productCode: String) {
        observeFavorite(productCode)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getProductByCodeUseCase(productCode)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            product = result.data,
                            nutriscore = result.data.nutriscoreGrade.toGradeUi(),
                            ecoscore = result.data.ecoscoreGrade.toGradeUi()
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun onFavoriteClicked() {
        val state = _uiState.value
        val product = state.product ?: return

        viewModelScope.launch {
            if (state.isFavorite) {
                favoritesRepository.remove(product.code)
            } else {
                favoritesRepository.add(product)
            }
        }
    }

    private fun observeFavorite(productCode: String) {
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch {
            favoritesRepository.observeIsFavorite(productCode).collect { isFavorite ->
                _uiState.update { it.copy(isFavorite = isFavorite) }
            }
        }
    }
}