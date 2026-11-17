package com.practicum.practice3.domain.model

import java.util.Locale

data class LessonTime(val hour: Int, val minute: Int) {

    fun format(): String = String.format(Locale.ROOT, "%02d:%02d", hour, minute)

    companion object {
        private val PATTERN = Regex("([01]\\d|2[0-3]):([0-5]\\d)")

        fun parse(text: String): LessonTime? {
            val match = PATTERN.matchEntire(text.trim()) ?: return null
            return LessonTime(
                hour = match.groupValues[1].toInt(),
                minute = match.groupValues[2].toInt()
            )
        }
    }
}