package com.proyecto.photogallery.infrastructure.mapper

import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.domain.model.PhotoSource
import com.proyecto.photogallery.infrastructure.datasource.local.entity.PhotoEntity

object PhotoMapper {
    fun toEntity(photo: Photo): PhotoEntity {
        return PhotoEntity(
            id = photo.id,
            uri = photo.uri,
            dateTaken = photo.dateTaken,
            source = photo.source.name
        )
    }

    fun toDomain(photoEntity: PhotoEntity): Photo {
        return Photo(
            id = photoEntity.id,
            uri = photoEntity.uri,
            dateTaken = photoEntity.dateTaken,
            source = try {
                PhotoSource.valueOf(photoEntity.source)
            } catch (e: IllegalArgumentException) {
                // Para datos antiguos que no tenían source, usar GALLERY como default
                PhotoSource.GALLERY
            }
        )
    }
}
