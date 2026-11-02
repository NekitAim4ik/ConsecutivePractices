package com.practicum.practice3.domain.model

data class ProductFilters(
    val category: String? = null,
    val query: String = "",
    val minNutriscore: String? = null
) {
    val isActive: Boolean
        get() = category != null || query.isNotBlank() || minNutriscore != null

    companion object {
        val Default = ProductFilters()
    }
}

object FilterOptions {
    val categories = listOf("chocolates", "beverages", "cereals")
    val nutriscoreGrades = listOf("a", "b", "c", "d", "e")
}