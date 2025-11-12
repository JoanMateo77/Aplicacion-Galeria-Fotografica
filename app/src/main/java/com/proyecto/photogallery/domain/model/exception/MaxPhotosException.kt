package com.proyecto.photogallery.domain.model.exception

class MaxPhotosException(
    message: String = "No puedes agregar más de 3 fotos (RNF-04)"
) : Exception(message)
