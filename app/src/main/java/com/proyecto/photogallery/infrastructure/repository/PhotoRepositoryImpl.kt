package com.proyecto.photogallery.infrastructure.repository

import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.domain.repository.PhotoRepository
import com.proyecto.photogallery.infrastructure.datasource.local.dao.PhotoDao
import com.proyecto.photogallery.infrastructure.mapper.toDomain
import com.proyecto.photogallery.infrastructure.mapper.toEntity
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao
) : PhotoRepository {

    override suspend fun getAllPhotos(): List<Photo> {
        return photoDao.getAllPhotos().map { it.toDomain() }
    }

    override suspend fun insertPhoto(photo: Photo) {
        photoDao.insertPhoto(photo.toEntity())
    }
}