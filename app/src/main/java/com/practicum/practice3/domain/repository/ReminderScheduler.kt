package com.practicum.practice3.domain.repository

import com.practicum.practice3.domain.model.LessonTime

interface ReminderScheduler {
    fun schedule(time: LessonTime, ownerName: String)
}