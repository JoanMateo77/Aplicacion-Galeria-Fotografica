package com.proyecto.photogallery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.domain.usecase.AddPhotoUseCase
import com.proyecto.photogallery.domain.usecase.DeletePhotoUseCase
import com.proyecto.photogallery.domain.usecase.GetPhotoCountUseCase
import com.proyecto.photogallery.domain.usecase.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val addPhotoUseCase: AddPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val getPhotosUseCase: GetPhotosUseCase,
    private val getPhotoCountUseCase: GetPhotoCountUseCase
) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _photos.value = getPhotosUseCase()
        }
    }

    fun addPhoto(photo: Photo) {
        viewModelScope.launch {
            try {
                val count = getPhotoCountUseCase()
                if (count >= 3) {
                    _errorMessage.value = "No puedes agregar más de 3 fotos (RNF-04)"
                } else {
                    addPhotoUseCase(photo)
                    loadPhotos()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            deletePhotoUseCase(photo)
            loadPhotos()
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
