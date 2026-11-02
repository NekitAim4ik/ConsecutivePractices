package com.practicum.practice3.data.local.db

import com.practicum.practice3.domain.model.Nutriments
import com.practicum.practice3.domain.model.Product

fun Product.toFavoriteEntity(addedAt: Long): FavoriteProductEntity = FavoriteProductEntity(
    code = code,
    productName = productName,
    brands = brands,
    quantity = quantity,
    imageUrl = imageUrl,
    nutriscoreGrade = nutriscoreGrade,
    ecoscoreGrade = ecoscoreGrade,
    categories = categories,
    ingredientsText = ingredientsText,
    allergens = allergens,
    energyKcal = nutriments.energyKcal,
    fat = nutriments.fat,
    carbohydrates = nutriments.carbohydrates,
    sugars = nutriments.sugars,
    proteins = nutriments.proteins,
    salt = nutriments.salt,
    addedAt = addedAt
)

fun FavoriteProductEntity.toDomain(): Product = Product(
    code = code,
    productName = productName,
    brands = brands,
    quantity = quantity,
    imageUrl = imageUrl,
    nutriscoreGrade = nutriscoreGrade,
    ecoscoreGrade = ecoscoreGrade,
    categories = categories,
    ingredientsText = ingredientsText,
    allergens = allergens,
    nutriments = Nutriments(
        energyKcal = energyKcal,
        fat = fat,
        carbohydrates = carbohydrates,
        sugars = sugars,
        proteins = proteins,
        salt = salt
    )
)