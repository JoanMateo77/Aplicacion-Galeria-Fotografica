package com.proyecto.photogallery.infrastructure.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.proyecto.photogallery.infrastructure.datasource.local.dao.PhotoDao
import com.proyecto.photogallery.infrastructure.datasource.local.entity.PhotoEntity

@Database(
    entities = [PhotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}