package com.practicum.practice3.domain.repository

import com.practicum.practice3.domain.model.ProductFilters
import kotlinx.coroutines.flow.Flow

interface FiltersRepository {
    val filters: Flow<ProductFilters>
    suspend fun save(filters: ProductFilters)
}