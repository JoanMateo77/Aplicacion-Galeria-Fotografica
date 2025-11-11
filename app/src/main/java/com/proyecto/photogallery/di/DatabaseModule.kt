package com.proyecto.photogallery.di

import android.content.Context
import androidx.room.Room
import com.proyecto.photogallery.infrastructure.datasource.local.PhotoDatabase
import com.proyecto.photogallery.infrastructure.datasource.local.dao.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): PhotoDatabase {
        return Room.databaseBuilder(
            appContext,
            PhotoDatabase::class.java,
            "photo_database"
        ).build()
    }

    @Provides
    fun providePhotoDao(database: PhotoDatabase): PhotoDao = database.photoDao()
}