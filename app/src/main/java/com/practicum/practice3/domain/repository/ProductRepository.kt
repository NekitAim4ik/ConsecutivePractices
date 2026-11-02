package com.practicum.practice3.domain.repository

import com.practicum.practice3.domain.model.Product
import com.practicum.practice3.domain.model.Resource

interface ProductRepository {
    suspend fun getProducts(
        page: Int = 1,
        pageSize: Int = 20,
        category: String? = null
    ): Resource<List<Product>>

    suspend fun getProductByCode(code: String): Resource<Product>
}