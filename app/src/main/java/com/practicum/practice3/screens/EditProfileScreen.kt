package com.practicum.practice3.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.practicum.practice3.viewmodel.EditProfileEvent
import com.practicum.practice3.viewmodel.EditProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showSourceDialog by rememberSaveable { mutableStateOf(false) }

    // Photo Picker не требует runtime-разрешений.
    val pickPhoto = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) viewModel.onAvatarPicked(uri.toString())
    }

    // Съёмка через системное приложение камеры: разрешение CAMERA не нужно,
    // пока оно не объявлено в манифесте нашего приложения.
    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        viewModel.onCameraResult(success)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is EditProfileEvent.LaunchCamera -> takePicture.launch(Uri.parse(event.photoUri))
                is EditProfileEvent.Applied -> onNavigateBack()
                is EditProfileEvent.ShowMessage ->
                    Toast.makeText(context, event.text, Toast.LENGTH_LONG).show()
            }
        }
    }

    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clickable { showSourceDialog = true }
                .padding(8.dp)
        ) {
            AvatarImage(avatarUri = state.avatarUri, size = 140.dp)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Изменить фото",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = state.fullName,
            onValueChange = viewModel::onFullNameChanged,
            label = { Text("ФИО") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.position,
            onValueChange = viewModel::onPositionChanged,
            label = { Text("Должность") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.resumeUrl,
            onValueChange = viewModel::onResumeUrlChanged,
            label = { Text("Ссылка на резюме/портфолио") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = viewModel::onApplyClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Готово")
        }
    }

    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text("Выберите источник фото") },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            showSourceDialog = false
                            pickPhoto.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) { Text("Галерея") }
                    TextButton(
                        onClick = {
                            showSourceDialog = false
                            viewModel.onCameraClicked()
                        }
                    ) { Text("Камера") }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSourceDialog = false }) { Text("Отмена") }
            }
        )
    }
}