package com.proyecto.photogallery.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyecto.photogallery.presentation.ui.screen.PhotoGalleryScreen

@Composable
fun NavGraph(startDestination: String = "gallery") {
    val navController = rememberNavController()

    NavHost(navController, startDestination = startDestination) {
        composable("gallery") {
            PhotoGalleryScreen()
        }
    }
}
