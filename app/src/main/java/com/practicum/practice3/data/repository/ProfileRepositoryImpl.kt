package com.practicum.practice3.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.practicum.practice3.domain.model.Profile
import com.practicum.practice3.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class ProfileRepositoryImpl(
    context: Context,
    private val dataStore: DataStore<Preferences>
) : ProfileRepository {

    private val appContext = context.applicationContext

    override val profile: Flow<Profile> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            Profile(
                fullName = prefs[FULL_NAME].orEmpty(),
                position = prefs[POSITION].orEmpty(),
                avatarUri = prefs[AVATAR_URI],
                resumeUrl = prefs[RESUME_URL].orEmpty()
            )
        }

    override suspend fun save(profile: Profile) {
        dataStore.edit { prefs ->
            prefs.setOrRemove(FULL_NAME, profile.fullName.trim())
            prefs.setOrRemove(POSITION, profile.position.trim())
            prefs.setOrRemove(AVATAR_URI, profile.avatarUri)
            prefs.setOrRemove(RESUME_URL, profile.resumeUrl.trim())
        }
        deleteUnusedAvatars(keepUri = profile.avatarUri)
    }

    override suspend fun importAvatar(sourceUri: String): String = withContext(Dispatchers.IO) {
        val dir = File(appContext.filesDir, AVATARS_DIR).apply { mkdirs() }
        val target = File(dir, "avatar_${System.currentTimeMillis()}.jpg")

        val input = appContext.contentResolver.openInputStream(Uri.parse(sourceUri))
            ?: throw IOException("Не удалось открыть выбранное фото")
        input.use { source ->
            target.outputStream().use { output -> source.copyTo(output) }
        }
        Uri.fromFile(target).toString()
    }

    override suspend fun createCameraPhotoUri(): String = withContext(Dispatchers.IO) {
        val dir = File(appContext.cacheDir, CAMERA_DIR).apply { mkdirs() }
        dir.listFiles()?.forEach { it.delete() }

        val file = File(dir, "photo_${System.currentTimeMillis()}.jpg")
        file.createNewFile()

        FileProvider.getUriForFile(appContext, "${appContext.packageName}.fileprovider", file)
            .toString()
    }

    private suspend fun deleteUnusedAvatars(keepUri: String?) = withContext(Dispatchers.IO) {
        val keepName = keepUri?.let { Uri.parse(it).lastPathSegment }
        File(appContext.filesDir, AVATARS_DIR).listFiles()?.forEach { file ->
            if (file.name != keepName) file.delete()
        }
    }

    private fun MutablePreferences.setOrRemove(key: Preferences.Key<String>, value: String?) {
        if (value.isNullOrEmpty()) remove(key) else this[key] = value
    }

    private companion object {
        const val AVATARS_DIR = "avatars"
        const val CAMERA_DIR = "camera"

        val FULL_NAME = stringPreferencesKey("full_name")
        val POSITION = stringPreferencesKey("position")
        val AVATAR_URI = stringPreferencesKey("avatar_uri")
        val RESUME_URL = stringPreferencesKey("resume_url")
    }
}