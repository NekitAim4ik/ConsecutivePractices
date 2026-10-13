package com.practicum.practice3.domain.usecase

import com.practicum.practice3.domain.model.Product
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(
        page: Int = 1,
        pageSize: Int = 20,
        category: String? = null
    ): Resource<List<Product>> {
        return repository.getProducts(page, pageSize, category)
    }
}