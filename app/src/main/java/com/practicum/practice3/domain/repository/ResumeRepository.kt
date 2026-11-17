package com.practicum.practice3.domain.repository

import com.practicum.practice3.domain.model.DownloadedFile
import com.practicum.practice3.domain.model.Resource

interface ResumeRepository {
    suspend fun download(url: String): Resource<DownloadedFile>
}