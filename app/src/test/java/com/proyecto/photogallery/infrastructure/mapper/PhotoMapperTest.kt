package com.proyecto.photogallery.infrastructure.mapper

import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.domain.model.PhotoSource
import com.proyecto.photogallery.infrastructure.datasource.local.entity.PhotoEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class PhotoMapperTest {

    @Test
    fun `toDomain convierte correctamente PhotoEntity a Photo`() {
        // Dado: una entidad de la base de datos
        val entity = PhotoEntity(
            id = 1,
            uri = "content://photo1",
            dateTaken = 1678886400000L, // Un timestamp de ejemplo
            source = "CAMERA"
        )

        // Cuando: se convierte al modelo de dominio
        val photo = PhotoMapper.toDomain(entity)

        // Entonces: los datos deben coincidir
        assertEquals(entity.id, photo.id)
        assertEquals(entity.uri, photo.uri)
        assertEquals(entity.dateTaken, photo.dateTaken)
        assertEquals(PhotoSource.CAMERA, photo.source)
    }

    @Test
    fun `toDomain convierte correctamente PhotoEntity con source GALLERY`() {
        // Dado: una entidad de la base de datos con source GALLERY
        val entity = PhotoEntity(
            id = 2,
            uri = "content://photo2",
            dateTaken = 1678972800000L,
            source = "GALLERY"
        )

        // Cuando: se convierte al modelo de dominio
        val photo = PhotoMapper.toDomain(entity)

        // Entonces: el source debe ser GALLERY
        assertEquals(PhotoSource.GALLERY, photo.source)
    }

    @Test
    fun `toDomain maneja correctamente source inválido usando default GALLERY`() {
        // Dado: una entidad con source inválido (datos antiguos)
        val entity = PhotoEntity(
            id = 3,
            uri = "content://photo3",
            dateTaken = 1678972800000L,
            source = ""
        )

        // Cuando: se convierte al modelo de dominio
        val photo = PhotoMapper.toDomain(entity)

        // Entonces: debe usar GALLERY como default
        assertEquals(PhotoSource.GALLERY, photo.source)
    }

    @Test
    fun `toEntity convierte correctamente Photo a PhotoEntity`() {
        // Dado: un modelo de dominio
        val photo = Photo(
            id = 2,
            uri = "content://photo2",
            dateTaken = 1678972800000L, // Un timestamp de ejemplo
            source = PhotoSource.CAMERA
        )

        // Cuando: se convierte a la entidad de la base de datos
        val entity = PhotoMapper.toEntity(photo)

        // Entonces: los datos deben coincidir
        assertEquals(photo.id, entity.id)
        assertEquals(photo.uri, entity.uri)
        assertEquals(photo.dateTaken, entity.dateTaken)
        assertEquals("CAMERA", entity.source)
    }

    @Test
    fun `toEntity convierte correctamente Photo con source GALLERY`() {
        // Dado: un modelo de dominio con source GALLERY
        val photo = Photo(
            id = 3,
            uri = "content://photo3",
            dateTaken = 1678972800000L,
            source = PhotoSource.GALLERY
        )

        // Cuando: se convierte a la entidad de la base de datos
        val entity = PhotoMapper.toEntity(photo)

        // Entonces: el source debe ser "GALLERY"
        assertEquals("GALLERY", entity.source)
    }
}
