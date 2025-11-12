package com.proyecto.photogallery.domain.repository

import com.proyecto.photogallery.domain.model.Photo

class FakePhotoRepository : PhotoRepository {

    private val photos = mutableListOf<Photo>()

    override suspend fun getAllPhotos(): List<Photo> {
        return photos
    }

    override suspend fun insertPhoto(photo: Photo) {
        photos.add(photo)
    }

    override suspend fun updatePhoto(photo: Photo) {
        val index = photos.indexOfFirst { it.id == photo.id }
        if (index != -1) {
            photos[index] = photo
        }
    }

    override suspend fun deletePhoto(photo: Photo) {
        photos.remove(photo)
    }

    override suspend fun deleteAllPhotos() {
        photos.clear()
    }
}
