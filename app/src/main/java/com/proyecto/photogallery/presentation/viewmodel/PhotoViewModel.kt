package com.proyecto.photogallery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.domain.model.exception.MaxPhotosException
import com.proyecto.photogallery.domain.usecase.AddPhotoUseCase
import com.proyecto.photogallery.domain.usecase.DeletePhotoUseCase
import com.proyecto.photogallery.domain.usecase.GetPhotosUseCase
import com.proyecto.photogallery.presentation.state.PhotoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val addPhotoUseCase: AddPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoUiState())
    val uiState: StateFlow<PhotoUiState> = _uiState.asStateFlow()

    // Mantener compatibilidad con código existente - usar stateIn para convertir Flow a StateFlow
    val photos: StateFlow<List<Photo>> = _uiState
        .map { it.photos }
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, emptyList())
    
    val errorMessage: StateFlow<String?> = _uiState
        .map { it.errorMessage }
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, null)

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val photos = getPhotosUseCase()
                _uiState.update { 
                    it.copy(
                        photos = photos,
                        isLoading = false,
                        maxPhotosReached = photos.size >= 3
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar fotos: ${e.message}"
                    )
                }
            }
        }
    }

    fun addPhoto(photo: Photo) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                addPhotoUseCase(photo)
                loadPhotos() // Recargar para obtener el estado actualizado
            } catch (e: MaxPhotosException) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message,
                        maxPhotosReached = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al agregar foto: ${e.message}"
                    )
                }
            }
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                deletePhotoUseCase(photo)
                loadPhotos()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al eliminar foto: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null, maxPhotosReached = false) }
    }
}
