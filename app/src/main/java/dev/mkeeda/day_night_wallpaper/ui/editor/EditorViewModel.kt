package dev.mkeeda.day_night_wallpaper.ui.editor

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EditorViewModel : ViewModel() {
    var selectedImageUri: Uri? by mutableStateOf(null)
        private set

    fun selectImageUri(uri: Uri) {
        selectedImageUri = uri
        println("⭐: $uri")
    }
}
