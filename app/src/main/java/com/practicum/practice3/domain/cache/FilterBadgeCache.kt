package com.practicum.practice3.domain.cache

import com.practicum.practice3.domain.model.ProductFilters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterBadgeCache {

    private val _isBadgeVisible = MutableStateFlow(false)
    val isBadgeVisible: StateFlow<Boolean> = _isBadgeVisible.asStateFlow()

    fun onFiltersChanged(filters: ProductFilters) {
        _isBadgeVisible.value = filters.isActive
    }
}