package com.practicum.practice3.storage

import com.practicum.practice3.domain.model.Nutriments
import com.practicum.practice3.domain.model.Product


interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getProductByCode(code: String): Product?
}

object MockProductRepository : ProductRepository {
    private val mockProducts = listOf(
        Product(
            code = "3017620422003",
            productName = "Nutella B-Ready",
            brands = "Ferrero",
            quantity = "22 g x 6",
            imageUrl = "https://images.openfoodfacts.org/images/products/301/762/042/2003/front_en.4.400.jpg",
            nutriscoreGrade = "e",
            ecoscoreGrade = "c",
            categories = listOf("Sweet spreads", "Chocolate spreads"),
            ingredientsText = "Sugar, palm oil, hazelnuts (13%), skimmed milk powder...",
            allergens = listOf("Milk", "Hazelnuts"),
            nutriments = Nutriments(
                energyKcal = 530.0,
                fat = 30.0,
                carbohydrates = 56.0,
                sugars = 54.0,
                proteins = 6.1,
                salt = 0.107
            )
        ),
        Product(
            code = "5449000000996",
            productName = "Coca-Cola Zero",
            brands = "The Coca-Cola Company",
            quantity = "330 ml",
            imageUrl = "https://images.openfoodfacts.org/images/products/544/900/000/0996/front_en.22.400.jpg",
            nutriscoreGrade = "b",
            ecoscoreGrade = "d",
            categories = listOf("Beverages"),
            ingredientsText = "Carbonated water, colour (E150d), sweeteners...",
            allergens = emptyList(),
            nutriments = Nutriments(
                energyKcal = 0.4,
                fat = 0.0,
                carbohydrates = 0.0,
                sugars = 0.0,
                proteins = 0.0,
                salt = 0.02
            )
        )
    )

    override suspend fun getProducts(): List<Product> = mockProducts

    override suspend fun getProductByCode(code: String): Product? {
        return mockProducts.find { it.code == code }
    }
}