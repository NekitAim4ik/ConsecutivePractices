package com.practicum.practice3.di

import com.practicum.practice3.data.remote.NetworkClient
import com.practicum.practice3.data.repository.ProductRepositoryImpl
import com.practicum.practice3.domain.repository.ProductRepository
import com.practicum.practice3.domain.usecase.GetProductByCodeUseCase
import com.practicum.practice3.domain.usecase.GetProductsUseCase

object NetworkModule {
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(NetworkClient.api)
    }

    val getProductsUseCase: GetProductsUseCase by lazy {
        GetProductsUseCase(productRepository)
    }

    val getProductByCodeUseCase: GetProductByCodeUseCase by lazy {
        GetProductByCodeUseCase(productRepository)
    }
}