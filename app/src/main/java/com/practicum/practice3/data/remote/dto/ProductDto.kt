package com.practicum.practice3.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("code") val code: String?,
    @SerializedName("product_name") val productName: String?,
    @SerializedName("brands") val brands: String?,
    @SerializedName("quantity") val quantity: String?,
    @SerializedName("image_front_url") val imageFrontUrl: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("nutriscore_grade") val nutriscoreGrade: String?,
    @SerializedName("ecoscore_grade") val ecoscoreGrade: String?,
    @SerializedName("categories_tags") val categoriesTags: List<String>?,
    @SerializedName("ingredients_text") val ingredientsText: String?,
    @SerializedName("allergens_tags") val allergensTags: List<String>?,
    @SerializedName("nutriments") val nutriments: NutrimentsDto?
)