package com.proyecto.photogallery.domain.repository

import com.proyecto.photogallery.domain.model.Photo

interface PhotoRepository {
    suspend fun getAllPhotos(): List<Photo>
    suspend fun insertPhoto(photo: Photo)
    suspend fun deleteAllPhotos()
}