package com.proyecto.photogallery.domain.usecase

import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.domain.repository.PhotoRepository
import javax.inject.Inject

class AddPhotoUseCase @Inject constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(photo: Photo) = repository.insertPhoto(photo)
}
