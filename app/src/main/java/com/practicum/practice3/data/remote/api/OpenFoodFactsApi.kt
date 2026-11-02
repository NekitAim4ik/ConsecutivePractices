package com.practicum.practice3.data.remote.api

import com.practicum.practice3.data.remote.dto.ProductResponseDto
import com.practicum.practice3.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {

    @GET("cgi/search.pl")
    suspend fun searchProducts(
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("page") page: Int = 1,
        @Query("categories_tags_en") categories: String? = null,
        @Query("sort_by") sortBy: String = "popularity_key",

        @Query("fields") fields: String = "code,product_name,brands,quantity,image_front_url,image_url,nutriscore_grade,ecoscore_grade,categories_tags,ingredients_text,allergens_tags,nutriments"
    ): SearchResponseDto

    @GET("api/v3/product/{code}")
    suspend fun getProductByCode(
        @Path("code") code: String,
        @Query("fields") fields: String = "code,product_name,brands,quantity,image_front_url,image_url,nutriscore_grade,ecoscore_grade,categories_tags,ingredients_text,allergens_tags,nutriments"
    ): ProductResponseDto
}