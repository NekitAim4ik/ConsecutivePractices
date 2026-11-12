package com.practicum.practice3.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {

    @Query("SELECT * FROM favorite_products ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<FavoriteProductEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_products WHERE code = :code)")
    fun observeIsFavorite(code: String): Flow<Boolean>

    @Query("SELECT * FROM favorite_products WHERE code = :code")
    suspend fun getByCode(code: String): FavoriteProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteProductEntity)

    @Query("DELETE FROM favorite_products WHERE code = :code")
    suspend fun deleteByCode(code: String)
}