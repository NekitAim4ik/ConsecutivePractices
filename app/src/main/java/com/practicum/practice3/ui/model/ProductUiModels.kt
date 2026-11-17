package com.practicum.practice3.ui.model

import androidx.compose.ui.graphics.Color

data class GradeUi(
    val letter: String,
    val color: Color
)

data class ProductListItemUi(
    val code: String,
    val name: String,
    val brands: String,
    val quantity: String,
    val nutriscore: GradeUi?
)