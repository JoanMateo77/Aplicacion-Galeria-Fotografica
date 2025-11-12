package com.proyecto.photogallery.domain.model

data class Photo(
    val id: Int = 0,
    val uri: String,
    val dateTaken: Long,
    val source: PhotoSource
)