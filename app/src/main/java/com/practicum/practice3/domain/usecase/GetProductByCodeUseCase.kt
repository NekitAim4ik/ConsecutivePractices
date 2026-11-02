package com.practicum.practice3.domain.usecase

import com.practicum.practice3.domain.model.Product
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.repository.ProductRepository

class GetProductByCodeUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(code: String): Resource<Product> {
        return repository.getProductByCode(code)
    }
}