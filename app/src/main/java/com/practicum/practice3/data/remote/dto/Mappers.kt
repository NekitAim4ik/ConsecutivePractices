package com.practicum.practice3.data.remote.dto

import com.practicum.practice3.domain.model.Nutriments
import com.practicum.practice3.domain.model.Product


private fun String?.toValidGrade(): String? {
    return this?.lowercase()?.takeIf { it in listOf("a", "b", "c", "d", "e") }
}

private fun cleanTag(tag: String): String {
    return tag.removePrefix("en:")
        .removePrefix("fr:")
        .removePrefix("ru:")
        .replace("-", " ")
        .replace("_", " ")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

fun ProductDto.toDomain(): Product {
    return Product(
        code = code.orEmpty(),
        productName = productName ?: "Без названия",
        brands = brands ?: "Неизвестный бренд",
        quantity = quantity ?: "",
        imageUrl = (imageFrontUrl ?: imageUrl)?.takeIf { it.isNotBlank() } ?: "",
        nutriscoreGrade = nutriscoreGrade.toValidGrade(),
        ecoscoreGrade = ecoscoreGrade.toValidGrade(),
        categories = categoriesTags?.map { cleanTag(it) } ?: emptyList(),
        ingredientsText = ingredientsText ?: "Состав не указан",
        allergens = allergensTags?.map { cleanTag(it) } ?: emptyList(),
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