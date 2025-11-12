package com.proyecto.photogallery.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun PhotoCard(
    photoUri: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(4.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(photoUri),
            contentDescription = "Photo",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}
