package com.practicum.practice3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.practice3.domain.model.Profile
import com.practicum.practice3.domain.model.Resource
import com.practicum.practice3.domain.repository.ProfileRepository
import com.practicum.practice3.domain.usecase.DownloadResumeUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: Profile = Profile(),
    val isLoading: Boolean = true,
    val isDownloadingResume: Boolean = false
)

sealed interface ProfileEvent {
    data class OpenFile(val uri: String, val mimeType: String) : ProfileEvent
    data class ShowMessage(val text: String) : ProfileEvent
}

class ProfileViewModel(
    profileRepository: ProfileRepository,
    private val downloadResumeUseCase: DownloadResumeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProfileEvent>(Channel.BUFFERED)
    val events: Flow<ProfileEvent> = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            profileRepository.profile.collect { profile ->
                _uiState.update { it.copy(profile = profile, isLoading = false) }
            }
        }
    }

    fun onResumeClicked() {
        val state = _uiState.value
        val url = state.profile.resumeUrl
        if (state.isDownloadingResume || url.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isDownloadingResume = true) }
            val result = downloadResumeUseCase(url)
            _uiState.update { it.copy(isDownloadingResume = false) }

            when (result) {
                is Resource.Success -> _events.send(
                    ProfileEvent.OpenFile(result.data.uri, result.data.mimeType)
                )
                is Resource.Error -> _events.send(ProfileEvent.ShowMessage(result.message))
                is Resource.Loading -> Unit
            }
        }
    }
}