package com.practicum.practice3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.practice3.domain.cache.FilterBadgeCache
import com.practicum.practice3.domain.model.ProductFilters
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.repository.FiltersRepository
import com.practicum.practice3.domain.usecase.GetProductsUseCase
import com.practicum.practice3.ui.model.ProductListItemUi
import com.practicum.practice3.ui.model.toListItemUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductListUiState(
    val products: List<ProductListItemUi> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class ProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val filtersRepository: FiltersRepository,
    private val badgeCache: FilterBadgeCache
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private var currentFilters = ProductFilters.Default
    private var loadJob: Job? = null

    init {
        viewModelScope.launch {
            filtersRepository.filters
                .distinctUntilChanged()
                .collect { filters ->
                    currentFilters = filters
                    badgeCache.onFiltersChanged(filters)
                    loadProducts()
                }
        }
    }

    fun loadProducts() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getProductsUseCase(currentFilters)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            products = result.data.map { product -> product.toListItemUi() }
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
}