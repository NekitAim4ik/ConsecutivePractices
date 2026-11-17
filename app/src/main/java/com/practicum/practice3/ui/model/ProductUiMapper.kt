package com.practicum.practice3.ui.model

import androidx.compose.ui.graphics.Color
import com.practicum.practice3.domain.model.Product

fun Product.toListItemUi(): ProductListItemUi = ProductListItemUi(
    code = code,
    name = productName,
    brands = brands,
    quantity = quantity,
    nutriscore = nutriscoreGrade.toGradeUi()
)

fun String?.toGradeUi(): GradeUi? {
    val grade = this ?: return null
    return GradeUi(letter = grade.uppercase(), color = gradeColor(grade))
}

private fun gradeColor(grade: String): Color = when (grade.lowercase()) {
    "a" -> Color(0xFF038141)
    "b" -> Color(0xFF85BB2F)
    "c" -> Color(0xFFFECB02)
    "d" -> Color(0xFFEE8100)
    "e" -> Color(0xFFE63E11)
    else -> Color.Gray
}