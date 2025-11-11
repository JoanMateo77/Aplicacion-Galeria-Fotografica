package com.proyecto.photogallery.infrastructure.mapper

import com.proyecto.photogallery.domain.model.Photo
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
            source = "camera"
        )

        // Cuando: se convierte al modelo de dominio
        val photo = PhotoMapper.toDomain(entity)

        // Entonces: los datos deben coincidir (excepto 'source', que no existe en el dominio)
        assertEquals(entity.id, photo.id)
        assertEquals(entity.uri, photo.uri)
        assertEquals(entity.dateTaken, photo.dateTaken)
    }

    @Test
    fun `toEntity convierte correctamente Photo a PhotoEntity`() {
        // Dado: un modelo de dominio
        val photo = Photo(
            id = 2,
            uri = "content://photo2",
            dateTaken = 1678972800000L // Un timestamp de ejemplo
        )

        // Cuando: se convierte a la entidad de la base de datos
        val entity = PhotoMapper.toEntity(photo)

        // Entonces: los datos deben coincidir
        assertEquals(photo.id, entity.id)
        assertEquals(photo.uri, entity.uri)
        assertEquals(photo.dateTaken, entity.dateTaken)
        // El campo 'source' se establece como vacío por defecto en el mapper, lo cual es correcto.
        assertEquals("", entity.source)
    }
}
