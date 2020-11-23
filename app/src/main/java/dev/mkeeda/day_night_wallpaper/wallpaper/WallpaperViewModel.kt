package dev.mkeeda.day_night_wallpaper.wallpaper

import dev.mkeeda.day_night_wallpaper.data.UiMode
import dev.mkeeda.day_night_wallpaper.domain.ShowWallpaperUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * This ViewModel is for WallpaperService, is not subclass Jetpack ViewModel.
*/
@ExperimentalCoroutinesApi
class WallpaperViewModel(
    private val showWallpaperUseCase: ShowWallpaperUseCase,
    coroutineScope: CoroutineScope,
    initialUiMode: UiMode
) {
    private val uiModeState = MutableStateFlow(value = initialUiMode)

    private val _renderingWallpaper = MutableStateFlow(value = "")
    val renderingWallpaper: StateFlow<String> = _renderingWallpaper

    init {
        coroutineScope.launch {
            showWallpaperUseCase.execute(uiMode = uiModeState).collect { filePath ->
                _renderingWallpaper.value = filePath
            }
        }
    }

    fun notifyUiMode(newMode: UiMode) {
        uiModeState.value = newMode
    }
}
