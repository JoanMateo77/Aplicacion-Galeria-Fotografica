package com.proyecto.photogallery.infrastructure.repository

import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.domain.repository.PhotoRepository
import com.proyecto.photogallery.infrastructure.datasource.local.dao.PhotoDao
import com.proyecto.photogallery.infrastructure.mapper.PhotoMapper
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao
) : PhotoRepository {

    override suspend fun insertPhoto(photo: Photo) {
        val photoEntity = PhotoMapper.toEntity(photo)
        photoDao.insert(photoEntity)
    }

    override suspend fun updatePhoto(photo: Photo) {
        val photoEntity = PhotoMapper.toEntity(photo)
        photoDao.update(photoEntity)
    }

    override suspend fun deletePhoto(photo: Photo) {
        val photoEntity = PhotoMapper.toEntity(photo)
        photoDao.delete(photoEntity)
    }

    override suspend fun getAllPhotos(): List<Photo> {
        return photoDao.getAll().map { photoEntity -> PhotoMapper.toDomain(photoEntity) }
    }

    override suspend fun deleteAllPhotos() {
        photoDao.clearAll()
    }
}
