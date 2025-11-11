package com.proyecto.photogallery

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.proyecto.photogallery.infrastructure.datasource.local.dao.PhotoDao
import com.proyecto.photogallery.infrastructure.datasource.local.entity.PhotoEntity
import com.proyecto.photogallery.infrastructure.datasource.local.PhotoDatabase
import com.proyecto.photogallery.infrastructure.mapper.PhotoMapper
import com.proyecto.photogallery.infrastructure.repository.PhotoRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoRepositoryTest {

    private lateinit var db: PhotoDatabase
    private lateinit var dao: PhotoDao
    private lateinit var repository: PhotoRepositoryImpl

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PhotoDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.photoDao()
        repository = PhotoRepositoryImpl(dao)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndGetPhotos() = runBlocking {
        // Dado: una foto a insertar
        val photoEntity = PhotoEntity(
            id = 1,
            uri = "file://test_photo.jpg",
            dateTaken = System.currentTimeMillis(),
            source = "camera"
        )

        // Cuando: se inserta y se consulta
        dao.insert(photoEntity)
        val photos = dao.getAll()

        // Entonces: debería haber exactamente una foto y coincidir el URI
        assertEquals(1, photos.size)
        assertEquals("file://test_photo.jpg", photos.first().uri)
    }

    @Test
    fun clearAllRemovesData() = runBlocking {
        val photoEntity = PhotoEntity(
            id = 1,
            uri = "file://delete_me.jpg",
            dateTaken = System.currentTimeMillis(),
            source = "gallery"
        )
        dao.insert(photoEntity)
        dao.clearAll()
        val photos = dao.getAll()
        assertTrue(photos.isEmpty())
    }
}
