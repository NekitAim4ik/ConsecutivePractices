package com.practicum.practice3.domain.usecase

import com.practicum.practice3.domain.model.DownloadedFile
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.repository.ResumeRepository

class DownloadResumeUseCase(private val repository: ResumeRepository) {

    suspend operator fun invoke(url: String): Resource<DownloadedFile> {
        val trimmed = url.trim()
        val isHttp = trimmed.startsWith("http://", ignoreCase = true) ||
                trimmed.startsWith("https://", ignoreCase = true)

        if (!isHttp) {
            return Resource.Error("Ссылка на резюме должна начинаться с http:// или https://")
        }
        return repository.download(trimmed)
    }
}