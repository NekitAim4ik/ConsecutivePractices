package com.practicum.practice3.domain.usecase

import com.practicum.practice3.domain.model.LessonTime
import com.practicum.practice3.domain.model.Profile
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.repository.ProfileRepository
import com.practicum.practice3.domain.repository.ReminderScheduler

class SaveProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val reminderScheduler: ReminderScheduler
) {

    suspend operator fun invoke(profile: Profile): Resource<Unit> {
        val lessonTime = LessonTime.parse(profile.lessonTime)
            ?: return Resource.Error("Время пары должно быть в формате HH:mm")

        val normalized = profile.copy(
            fullName = profile.fullName.trim(),
            lessonTime = lessonTime.format()
        )
        profileRepository.save(normalized)
        reminderScheduler.schedule(lessonTime, normalized.fullName)
        return Resource.Success(Unit)
    }
}
