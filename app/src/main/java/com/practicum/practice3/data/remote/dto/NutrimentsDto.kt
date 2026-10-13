package com.practicum.practice3.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NutrimentsDto(
    @SerializedName("energy-kcal_100g") val energyKcal: Double?,
    @SerializedName("fat_100g") val fat: Double?,
    @SerializedName("carbohydrates_100g") val carbohydrates: Double?,
    @SerializedName("sugars_100g") val sugars: Double?,
    @SerializedName("proteins_100g") val proteins: Double?,
    @SerializedName("salt_100g") val salt: Double?
)