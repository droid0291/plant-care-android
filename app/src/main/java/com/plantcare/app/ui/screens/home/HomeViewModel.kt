package com.plantcare.app.ui.screens.home

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantcare.app.data.model.PlantAnalysis
import com.plantcare.app.data.repository.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

sealed interface HomeUiState {
    object Idle : HomeUiState
    object Loading : HomeUiState
    data class Error(val message: String) : HomeUiState
    data class Success(val analysisJson: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PlantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    fun onImageSelected(uri: Uri) {
        _selectedImageUri.value = uri
        _uiState.value = HomeUiState.Idle
    }

    fun analyzeImage(context: Context) {
        val uri = _selectedImageUri.value ?: return
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repository.analyzeImage(context, uri)
                .onSuccess { result ->
                    val json = Json.encodeToString(result)
                    _uiState.value = HomeUiState.Success(json)
                }
                .onFailure { error ->
                    val message = when {
                        error.message?.contains("connect", ignoreCase = true) == true ->
                            "Cannot connect to server. Make sure the backend is running."
                        error.message?.contains("timeout", ignoreCase = true) == true ->
                            "Request timed out. Please try again."
                        else -> error.message ?: "An unexpected error occurred."
                    }
                    _uiState.value = HomeUiState.Error(message)
                }
        }
    }

    fun clearError() {
        if (_uiState.value is HomeUiState.Error) {
            _uiState.value = HomeUiState.Idle
        }
    }
}
