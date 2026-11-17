package com.practicum.practice3.domain.repository

import com.practicum.practice3.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<Product>>
    fun observeIsFavorite(code: String): Flow<Boolean>
    suspend fun getFavorite(code: String): Product?
    suspend fun add(product: Product)
    suspend fun remove(code: String)
}