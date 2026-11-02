package com.practicum.practice3.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    @SerializedName("status") val status: String?,
    @SerializedName("product") val product: ProductDto?
)