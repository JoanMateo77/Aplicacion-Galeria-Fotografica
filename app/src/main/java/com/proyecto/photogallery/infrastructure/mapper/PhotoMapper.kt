package com.proyecto.photogallery.infrastructure.mapper

import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.infrastructure.datasource.local.entity.PhotoEntity

object PhotoMapper {
    fun toEntity(photo: Photo): PhotoEntity {
        return PhotoEntity(
            id = photo.id,
            uri = photo.uri,
            dateTaken = photo.dateTaken,
            source = "" // El modelo de dominio no tiene 'source', se deja vacío por ahora
        )
    }

    fun toDomain(photoEntity: PhotoEntity): Photo {
        return Photo(
            id = photoEntity.id,
            uri = photoEntity.uri,
            dateTaken = photoEntity.dateTaken
        )
    }
}
