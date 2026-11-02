package com.practicum.practice3.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()
    private val listType = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        gson.fromJson<List<String>>(value, listType) ?: emptyList()
}