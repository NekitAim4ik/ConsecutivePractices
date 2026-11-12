package com.practicum.practice3.domain.usecase

import com.practicum.practice3.domain.model.Product
import com.practicum.practice3.domain.model.ProductFilters
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {

    suspend operator fun invoke(
        filters: ProductFilters = ProductFilters.Default,
        page: Int = 1
    ): Resource<List<Product>> {
        val minGrade = filters.minNutriscore
        val pageSize = if (minGrade != null) FILTERED_PAGE_SIZE else DEFAULT_PAGE_SIZE

        val result = repository.getProducts(
            page = page,
            pageSize = pageSize,
            category = filters.category,
            query = filters.query.trim().takeIf { it.isNotEmpty() }
        )

        if (minGrade == null || result !is Resource.Success) return result

        return Resource.Success(
            result.data.filter { product ->
                val grade = product.nutriscoreGrade
                grade != null && grade <= minGrade
            }
        )
    }

    private companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val FILTERED_PAGE_SIZE = 100
    }
}