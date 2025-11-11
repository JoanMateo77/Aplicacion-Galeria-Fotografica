package com.proyecto.photogallery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.proyecto.photogallery.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {
}
