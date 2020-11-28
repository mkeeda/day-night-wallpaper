package dev.mkeeda.day_night_wallpaper.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.mkeeda.day_night_wallpaper.data.ThemeImage
import dev.mkeeda.day_night_wallpaper.data.UiMode
import dev.mkeeda.day_night_wallpaper.data.WallpaperFile
import dev.mkeeda.day_night_wallpaper.data.WallpaperRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class EditorViewModel(
    private val wallpaperRepository: WallpaperRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow(EditorViewState())
    private val selectedTheme = MutableStateFlow(UiMode.Light)

    val viewState: StateFlow<EditorViewState> = _viewState

    init {
        viewModelScope.launch {
            combine(
                wallpaperRepository.wallpaperFile,
                selectedTheme
            ) { wallpaperFile, uiMode ->
                EditorViewState(
                    wallpaperFile = wallpaperFile,
                    selectedTheme = uiMode
                )
            }.collect {
                _viewState.value = it
            }
        }
    }

    fun onSelectImageUri(newImage: ThemeImage) {
        viewModelScope.launch {
            wallpaperRepository.update(newImage)
        }
    }

    fun onSelectTheme(theme: UiMode) {
        selectedTheme.value = theme
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

data class EditorViewState(
    val wallpaperFile: WallpaperFile? = null,
    val wallpaperThemes: List<UiMode> = listOf(UiMode.Light, UiMode.Dark),
    val selectedTheme: UiMode = UiMode.Light,
)
