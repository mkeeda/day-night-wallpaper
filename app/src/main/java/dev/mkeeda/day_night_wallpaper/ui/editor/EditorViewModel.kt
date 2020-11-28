package dev.mkeeda.day_night_wallpaper.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.mkeeda.day_night_wallpaper.data.ThemeImage
import dev.mkeeda.day_night_wallpaper.data.WallpaperFile
import dev.mkeeda.day_night_wallpaper.data.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EditorViewModel(
    private val wallpaperRepository: WallpaperRepository
) : ViewModel() {
    val wallpaperFile: Flow<WallpaperFile?> = wallpaperRepository.wallpaperFile

    fun selectImageUri(newImage: ThemeImage) {
        viewModelScope.launch {
            wallpaperRepository.update(newImage)
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
