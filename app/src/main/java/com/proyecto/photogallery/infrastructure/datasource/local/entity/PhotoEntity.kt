package com.proyecto.photogallery.infrastructure.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uri: String,
    val dateTaken: Long,
    val source: String // CAMERA o GALLERY
)
