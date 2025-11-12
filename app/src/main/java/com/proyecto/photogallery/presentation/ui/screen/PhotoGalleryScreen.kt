package com.proyecto.photogallery.presentation.ui.screen

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.proyecto.photogallery.domain.model.Photo
import com.proyecto.photogallery.domain.model.PhotoSource
import com.proyecto.photogallery.presentation.ui.components.CameraButton
import com.proyecto.photogallery.presentation.ui.components.EmptyState
import com.proyecto.photogallery.presentation.ui.components.GalleryButton
import com.proyecto.photogallery.presentation.ui.components.PhotoGrid
import com.proyecto.photogallery.presentation.utils.createImageFile
import com.proyecto.photogallery.presentation.utils.getUri
import com.proyecto.photogallery.presentation.viewmodel.PhotoViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhotoGalleryScreen(
    viewModel: PhotoViewModel = hiltViewModel()
) {
    val photos by viewModel.photos.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val newPhoto = Photo(
                    uri = it.toString(),
                    dateTaken = System.currentTimeMillis(),
                    source = PhotoSource.GALLERY
                )
                viewModel.addPhoto(newPhoto)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempImageUri?.let {
                    val newPhoto = Photo(
                        uri = it.toString(),
                        dateTaken = System.currentTimeMillis(),
                        source = PhotoSource.CAMERA
                    )
                    viewModel.addPhoto(newPhoto)
                }
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val imageFile = createImageFile(context)
                val imageUri = imageFile.getUri(context)
                tempImageUri = imageUri
                cameraLauncher.launch(imageUri)
            } else {
                // Opcional: Mostrar un mensaje al usuario de que el permiso es necesario
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Galería (${photos.size}/3 fotos)",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        if (photos.isEmpty()) {
            EmptyState(modifier = Modifier.weight(1f))
        } else {
            PhotoGrid(
                photos = photos,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            CameraButton(
                onClick = {
                    if (cameraPermissionState.status.isGranted) {
                        val imageFile = createImageFile(context)
                        val imageUri = imageFile.getUri(context)
                        tempImageUri = imageUri
                        cameraLauncher.launch(imageUri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                enabled = photos.size < 3
            )
            GalleryButton(
                onClick = { galleryLauncher.launch("image/*") },
                enabled = photos.size < 3
            )
            Button(onClick = {
                if (photos.isNotEmpty()) viewModel.deletePhoto(photos.last())
            }) {
                Text("Eliminar Última")
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
