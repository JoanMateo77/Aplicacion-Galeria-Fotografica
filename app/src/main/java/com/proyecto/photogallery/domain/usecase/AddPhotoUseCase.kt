package com.proyecto.photogallery.domain.usecase

import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.domain.model.exception.MaxPhotosException
import com.proyecto.photogallery.domain.repository.PhotoRepository
import javax.inject.Inject

class AddPhotoUseCase @Inject constructor(
    private val repository: PhotoRepository,
    private val getPhotoCountUseCase: GetPhotoCountUseCase
) {
    suspend operator fun invoke(photo: Photo) {
        val count = getPhotoCountUseCase()
        if (count >= 3) {
            throw MaxPhotosException()
        }
        repository.insertPhoto(photo)
    }
}
