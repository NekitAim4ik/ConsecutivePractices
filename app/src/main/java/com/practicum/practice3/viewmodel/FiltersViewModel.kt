package com.practicum.practice3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.practice3.domain.cache.FilterBadgeCache
import com.practicum.practice3.domain.model.ProductFilters
import com.practicum.practice3.domain.repository.FiltersRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FiltersUiState(
    val filters: ProductFilters = ProductFilters.Default,
    val isLoading: Boolean = true
)

class FiltersViewModel(
    private val filtersRepository: FiltersRepository,
    private val badgeCache: FilterBadgeCache
) : ViewModel() {

    private val _uiState = MutableStateFlow(FiltersUiState())
    val uiState: StateFlow<FiltersUiState> = _uiState.asStateFlow()

    private val _applied = Channel<Unit>(Channel.BUFFERED)

    /** Одноразовое событие «настройки применены, экран можно закрыть». */
    val applied: Flow<Unit> = _applied.receiveAsFlow()

    init {
        viewModelScope.launch {
            val saved = filtersRepository.filters.first()
            _uiState.update { it.copy(filters = saved, isLoading = false) }
        }
    }

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(filters = it.filters.copy(query = query)) }
    }

    fun onCategorySelected(category: String?) {
        _uiState.update { it.copy(filters = it.filters.copy(category = category)) }
    }

    fun onMinNutriscoreSelected(grade: String?) {
        _uiState.update { it.copy(filters = it.filters.copy(minNutriscore = grade)) }
    }

    fun onResetClicked() {
        _uiState.update { it.copy(filters = ProductFilters.Default) }
    }

    fun onApplyClicked() {
        val filters = _uiState.value.filters
        viewModelScope.launch {
            filtersRepository.save(filters)
            badgeCache.onFiltersChanged(filters)
            _applied.send(Unit)
        }
    }
}