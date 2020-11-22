package dev.mkeeda.day_night_wallpaper.ui.editor

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.mkeeda.day_night_wallpaper.data.Wallpaper
import dev.mkeeda.day_night_wallpaper.data.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditorViewModel(
    private val wallpaperRepository: WallpaperRepository
) : ViewModel() {
    val selectedImageUri: Flow<Uri?> = wallpaperRepository.wallpaperFlow
        .map { wallpaper ->
            wallpaper?.lightImageUri?.toUri()
        }

    fun selectImageUri(uri: Uri) {
        viewModelScope.launch {
            wallpaperRepository.update(
                newWallpaper = Wallpaper(
                    lightImageUri = uri.toString(),
                    darkImageUri = ""
                )
            )
        }
    }

    class Factory(
        private val wallpaperRepository: WallpaperRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditorViewModel(wallpaperRepository) as T
        }
    }
}
