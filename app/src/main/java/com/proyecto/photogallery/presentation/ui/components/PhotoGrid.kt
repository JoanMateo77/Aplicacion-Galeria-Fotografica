package com.proyecto.photogallery.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.proyecto.photogallery.domain.model.Photo

@Composable
fun PhotoGrid(
    photos: List<Photo>,
    onPhotoClick: (Photo) -> Unit = {},
    modifier: Modifier = Modifier,
    columns: Int = 3
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize()
    ) {
        items(photos) { photo ->
            PhotoCard(
                photoUri = photo.uri,
                onClick = { onPhotoClick(photo) }
            )
        }
    }
}
