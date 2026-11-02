package com.practicum.practice3.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProductEntity(
    @PrimaryKey val code: String,
    val productName: String,
    val brands: String,
    val quantity: String,
    val imageUrl: String,
    val nutriscoreGrade: String?,
    val ecoscoreGrade: String?,
    val categories: List<String>,
    val ingredientsText: String,
    val allergens: List<String>,
    val energyKcal: Double,
    val fat: Double,
    val carbohydrates: Double,
    val sugars: Double,
    val proteins: Double,
    val salt: Double,
    val addedAt: Long
)