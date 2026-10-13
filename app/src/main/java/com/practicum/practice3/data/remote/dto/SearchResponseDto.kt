package com.practicum.practice3.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchResponseDto(
    @SerializedName("count") val count: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("page_size") val pageSize: Int,
    @SerializedName("page_count") val pageCount: Int,
    @SerializedName("products") val products: List<ProductDto>
)