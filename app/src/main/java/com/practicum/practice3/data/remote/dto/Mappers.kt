package com.practicum.practice3.data.remote.dto

import com.practicum.practice3.domain.model.Nutriments
import com.practicum.practice3.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        code = code.orEmpty(),
        productName = productName ?: "Без названия",
        brands = brands ?: "Неизвестный бренд",
        quantity = quantity ?: "",
        imageUrl = imageFrontUrl ?: "",
        nutriscoreGrade = nutriscoreGrade,
        ecoscoreGrade = ecoscoreGrade,
        categories = categoriesTags ?: emptyList(),
        ingredientsText = ingredientsText ?: "",
        allergens = allergensTags?.map { it.removePrefix("en:") } ?: emptyList(),
        nutriments = nutriments?.toDomain() ?: Nutriments(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    )
}

fun NutrimentsDto.toDomain(): Nutriments {
    return Nutriments(
        energyKcal = energyKcal ?: 0.0,
        fat = fat ?: 0.0,
        carbohydrates = carbohydrates ?: 0.0,
        sugars = sugars ?: 0.0,
        proteins = proteins ?: 0.0,
        salt = salt ?: 0.0
    )
}