package com.proyecto.photogallery.presentation.state

import com.proyecto.photogallery.domain.model.Photo

data class PhotoUiState(
    val photos: List<Photo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val maxPhotosReached: Boolean = false
) {
    val hasPhotos: Boolean get() = photos.isNotEmpty()
    val photoCount: Int get() = photos.size
    val canAddMore: Boolean get() = photoCount < 3
    val isError: Boolean get() = errorMessage != null
}
