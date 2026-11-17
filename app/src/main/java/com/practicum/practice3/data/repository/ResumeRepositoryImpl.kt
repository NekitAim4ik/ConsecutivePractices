package com.practicum.practice3.data.repository

import android.content.Context
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.practicum.practice3.domain.model.DownloadedFile
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.repository.ResumeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException

class ResumeRepositoryImpl(
    context: Context,
    private val client: OkHttpClient
) : ResumeRepository {

    private val appContext = context.applicationContext

    override suspend fun download(url: String): Resource<DownloadedFile> =
        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(url).build()
                val result: Resource<DownloadedFile> = client.newCall(request).execute().use { response ->
                    val body: ResponseBody? = response.body
                    if (!response.isSuccessful || body == null) {
                        return@use Resource.Error("Ошибка сервера: ${response.code}")
                    }

                    val mimeType = resolveMimeType(body, url)
                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "pdf"

                    val dir = File(appContext.cacheDir, RESUMES_DIR).apply { mkdirs() }
                    dir.listFiles()?.forEach { it.delete() }
                    val file = File(dir, "resume.$extension")

                    body.byteStream().use { input ->
                        file.outputStream().use { output -> input.copyTo(output) }
                    }

                    val uri = FileProvider.getUriForFile(
                        appContext,
                        "${appContext.packageName}.fileprovider",
                        file
                    )
                    Resource.Success(DownloadedFile(uri.toString(), mimeType))
                }
                result
            } catch (e: IllegalArgumentException) {
                Resource.Error("Некорректная ссылка на резюме", e)
            } catch (e: IOException) {
                Resource.Error("Не удалось скачать резюме. Проверьте интернет и ссылку", e)
            } catch (e: Exception) {
                Resource.Error("Неизвестная ошибка: ${e.message}", e)
            }
        }

    private fun resolveMimeType(body: ResponseBody, url: String): String {
        val fromHeader = body.contentType()?.let { "${it.type}/${it.subtype}" }
            ?.takeIf { it !in GENERIC_MIME_TYPES }
        if (fromHeader != null) return fromHeader

        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "application/pdf"
    }

    private companion object {
        const val RESUMES_DIR = "resumes"
        val GENERIC_MIME_TYPES = setOf("application/octet-stream", "binary/octet-stream")
    }
}