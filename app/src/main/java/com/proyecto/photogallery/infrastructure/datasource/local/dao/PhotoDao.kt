package com.proyecto.photogallery.infrastructure.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.proyecto.photogallery.infrastructure.datasource.local.entity.PhotoEntity

@Dao
interface PhotoDao {

    @Insert
    suspend fun insertPhoto(photo: PhotoEntity)

    @Query("SELECT * FROM photos ORDER BY dateTaken DESC")
    suspend fun getAllPhotos(): List<PhotoEntity>
}