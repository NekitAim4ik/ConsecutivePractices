package com.practicum.practice3.domain.model

data class Profile(
    val fullName: String = "",
    val position: String = "",
    val avatarUri: String? = null,
    val resumeUrl: String = ""
)