package com.proyecto.photogallery.domain.usecase

import com.proyecto.photogallery.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotoCountUseCase @Inject constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(): Int = repository.getAllPhotos().size
}
