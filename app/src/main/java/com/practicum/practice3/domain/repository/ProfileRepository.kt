package com.practicum.practice3.domain.repository

import com.practicum.practice3.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile>

    suspend fun save(profile: Profile)

    suspend fun importAvatar(sourceUri: String): String

    suspend fun createCameraPhotoUri(): String
}