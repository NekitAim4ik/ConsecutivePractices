package com.practicum.practice3.data.repository

import com.practicum.practice3.data.remote.api.OpenFoodFactsApi
import com.practicum.practice3.data.remote.dto.toDomain
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.practicum.practice3.domain.model.Product
import retrofit2.HttpException
import java.io.IOException

class ProductRepositoryImpl(
    private val api: OpenFoodFactsApi
) : ProductRepository {

    override suspend fun getProducts(
        page: Int,
        pageSize: Int,
        category: String?
    ): Resource<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchProducts(
                page = page,
                pageSize = pageSize,
                categories = category
            )
            Resource.Success(response.products.map { it.toDomain() })
        } catch (e: HttpException) {
            Resource.Error("Ошибка сервера: ${e.code()}", e)
        } catch (e: IOException) {
            Resource.Error("Нет подключения к интернету", e)
        } catch (e: Exception) {
            Resource.Error("Неизвестная ошибка: ${e.message}", e)
        }
    }

    override suspend fun getProductByCode(code: String): Resource<Product> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getProductByCode(code)
                val productDto = response.product
                if (productDto != null) {
                    Resource.Success(productDto.toDomain())
                } else {
                    Resource.Error("Продукт не найден")
                }
            } catch (e: HttpException) {
                Resource.Error("Ошибка сервера: ${e.code()}", e)
            } catch (e: IOException) {
                Resource.Error("Нет подключения к интернету", e)
            } catch (e: Exception) {
                Resource.Error("Неизвестная ошибка: ${e.message}", e)
            }
        }
}