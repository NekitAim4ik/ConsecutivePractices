package com.practicum.practice3.domain.model

data class Product(
    val code: String,
    val productName: String,
    val brands: String,
    val quantity: String,
    val imageUrl: String,
    val nutriscoreGrade: String?,
    val ecoscoreGrade: String?,
    val categories: List<String>,
    val ingredientsText: String,
    val allergens: List<String>,
    val nutriments: Nutriments
)
