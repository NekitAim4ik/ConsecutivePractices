package com.practicum.practice3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.practice3.domain.model.Profile
import com.practicum.practice3.domain.repository.ProfileRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val fullName: String = "",
    val position: String = "",
    val avatarUri: String? = null,
    val resumeUrl: String = "",
    val isLoading: Boolean = true
)

sealed interface EditProfileEvent {
    data class LaunchCamera(val photoUri: String) : EditProfileEvent
    data object Applied : EditProfileEvent
    data class ShowMessage(val text: String) : EditProfileEvent
}

class EditProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _events = Channel<EditProfileEvent>(Channel.BUFFERED)
    val events: Flow<EditProfileEvent> = _events.receiveAsFlow()

    private var pendingCameraUri: String? = null

    init {
        viewModelScope.launch {
            val saved = profileRepository.profile.first()
            _uiState.update {
                it.copy(
                    fullName = saved.fullName,
                    position = saved.position,
                    avatarUri = saved.avatarUri,
                    resumeUrl = saved.resumeUrl,
                    isLoading = false
                )
            }
        }
    }

    fun onFullNameChanged(value: String) = _uiState.update { it.copy(fullName = value) }

    fun onPositionChanged(value: String) = _uiState.update { it.copy(position = value) }

    fun onResumeUrlChanged(value: String) = _uiState.update { it.copy(resumeUrl = value) }

    /** Фото выбрано в галерее (Photo Picker). */
    fun onAvatarPicked(sourceUri: String) {
        importAvatar(sourceUri)
    }

    fun onCameraClicked() {
        viewModelScope.launch {
            try {
                val photoUri = profileRepository.createCameraPhotoUri()
                pendingCameraUri = photoUri
                _events.send(EditProfileEvent.LaunchCamera(photoUri))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _events.send(EditProfileEvent.ShowMessage("Не удалось подготовить камеру"))
            }
        }
    }

    fun onCameraResult(success: Boolean) {
        val photoUri = pendingCameraUri
        pendingCameraUri = null
        if (success && photoUri != null) importAvatar(photoUri)
    }

    fun onApplyClicked() {
        val state = _uiState.value
        viewModelScope.launch {
            profileRepository.save(
                Profile(
                    fullName = state.fullName,
                    position = state.position,
                    avatarUri = state.avatarUri,
                    resumeUrl = state.resumeUrl
                )
            )
            _events.send(EditProfileEvent.Applied)
        }
    }

    private fun importAvatar(sourceUri: String) {
        viewModelScope.launch {
            try {
                val avatarUri = profileRepository.importAvatar(sourceUri)
                _uiState.update { it.copy(avatarUri = avatarUri) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _events.send(EditProfileEvent.ShowMessage("Не удалось загрузить фото"))
            }
        }
    }
}