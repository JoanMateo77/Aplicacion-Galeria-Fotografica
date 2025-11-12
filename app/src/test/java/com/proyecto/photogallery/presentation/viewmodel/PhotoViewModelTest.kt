package com.proyecto.photogallery.presentation.viewmodel

import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.domain.repository.FakePhotoRepository
import com.proyecto.photogallery.domain.usecase.AddPhotoUseCase
import com.proyecto.photogallery.domain.usecase.DeletePhotoUseCase
import com.proyecto.photogallery.domain.usecase.GetPhotoCountUseCase
import com.proyecto.photogallery.domain.usecase.GetPhotosUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PhotoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakePhotoRepository
    private lateinit var viewModel: PhotoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakePhotoRepository()
        viewModel = PhotoViewModel(
            addPhotoUseCase = AddPhotoUseCase(fakeRepository),
            deletePhotoUseCase = DeletePhotoUseCase(fakeRepository),
            getPhotosUseCase = GetPhotosUseCase(fakeRepository),
            getPhotoCountUseCase = GetPhotoCountUseCase(fakeRepository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addPhoto - cuando se añaden 3 fotos, el conteo es 3`() = runTest {
        // Given
        val photo1 = Photo(id = 1, uri = "uri1", dateTaken = 1L)
        val photo2 = Photo(id = 2, uri = "uri2", dateTaken = 2L)
        val photo3 = Photo(id = 3, uri = "uri3", dateTaken = 3L)

        // When
        viewModel.addPhoto(photo1)
        viewModel.addPhoto(photo2)
        viewModel.addPhoto(photo3)

        // Then
        testDispatcher.scheduler.advanceUntilIdle() // Avanza el dispatcher para que se completen las corrutinas
        assertEquals(3, viewModel.photos.value.size)
    }

    @Test
    fun `addPhoto - cuando se intenta añadir una cuarta foto, se muestra un error`() = runTest {
        // Given
        repeat(3) { i ->
            viewModel.addPhoto(Photo(id = i, uri = "uri$i", dateTaken = i.toLong()))
        }
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        val fourthPhoto = Photo(id = 4, uri = "uri4", dateTaken = 4L)
        viewModel.addPhoto(fourthPhoto)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(3, viewModel.photos.value.size) // El tamaño no debe cambiar
        assertNotNull(viewModel.errorMessage.value) // Se debe mostrar un mensaje de error
        assertEquals("No puedes agregar más de 3 fotos (RNF-04)", viewModel.errorMessage.value)
    }

    @Test
    fun `deletePhoto - borra una foto correctamente`() = runTest {
        // Given
        val photo = Photo(id = 1, uri = "uri1", dateTaken = 1L)
        viewModel.addPhoto(photo)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, viewModel.photos.value.size)

        // When
        viewModel.deletePhoto(photo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(0, viewModel.photos.value.size)
    }
}
