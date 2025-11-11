package com.proyecto.photogallery.infrastructure.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.proyecto.photogallery.infrastructure.datasource.local.entity.PhotoEntity

@Dao
interface PhotoDao {

    @Insert
    suspend fun insert(photoEntity: PhotoEntity)

    @Update
    suspend fun update(photoEntity: PhotoEntity)

    @Query("SELECT * FROM photos ORDER BY dateTaken DESC")
    suspend fun getAll(): List<PhotoEntity>

    @Query("DELETE FROM photos")
    suspend fun clearAll()
}
