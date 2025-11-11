package com.proyecto.photogallery.infrastructure.mapper

import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.infrastructure.datasource.local.entity.PhotoEntity

fun PhotoEntity.toDomain(): Photo {
    return Photo(
        id = this.id,
        uri = this.uri,
        dateTaken = this.dateTaken
    )
}

fun Photo.toEntity(): PhotoEntity {
    return PhotoEntity(
        id = this.id,
        uri = this.uri,
        dateTaken = this.dateTaken
    )
}