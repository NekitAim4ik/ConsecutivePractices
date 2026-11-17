package com.practicum.practice3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.practice3.domain.repository.FavoritesRepository
import com.practicum.practice3.ui.model.ProductListItemUi
import com.practicum.practice3.ui.model.toListItemUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val items: List<ProductListItemUi> = emptyList(),
    val isLoading: Boolean = true
)

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = favoritesRepository.observeFavorites()
        .map { products ->
            FavoritesUiState(
                items = products.map { product -> product.toListItemUi() },
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState()
        )

    fun onRemoveClicked(productCode: String) {
        viewModelScope.launch { favoritesRepository.remove(productCode) }
    }
}