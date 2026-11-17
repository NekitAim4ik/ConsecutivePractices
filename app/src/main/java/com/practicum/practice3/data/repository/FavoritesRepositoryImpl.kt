package com.practicum.practice3.data.repository

import com.practicum.practice3.data.local.db.FavoriteProductDao
import com.practicum.practice3.data.local.db.toDomain
import com.practicum.practice3.data.local.db.toFavoriteEntity
import com.practicum.practice3.domain.model.Product
import com.practicum.practice3.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val dao: FavoriteProductDao
) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<Product>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun observeIsFavorite(code: String): Flow<Boolean> = dao.observeIsFavorite(code)

    override suspend fun getFavorite(code: String): Product? = dao.getByCode(code)?.toDomain()

    override suspend fun add(product: Product) {
        dao.insert(product.toFavoriteEntity(addedAt = System.currentTimeMillis()))
    }

    override suspend fun remove(code: String) {
        dao.deleteByCode(code)
    }
}