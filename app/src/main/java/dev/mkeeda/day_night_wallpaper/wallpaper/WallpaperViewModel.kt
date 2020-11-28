package dev.mkeeda.day_night_wallpaper.wallpaper

import android.net.Uri
import dev.mkeeda.day_night_wallpaper.data.UiMode
import dev.mkeeda.day_night_wallpaper.domain.ShowWallpaperUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

/**
 * This ViewModel is for WallpaperService, is not subclass Jetpack ViewModel.
 */
class WallpaperViewModel(
    showWallpaperUseCase: ShowWallpaperUseCase,
    initialUiMode: UiMode
) {
    private val uiModeState = MutableStateFlow(value = initialUiMode)

    val renderedWallpaper: Flow<Uri> = showWallpaperUseCase.execute(uiMode = uiModeState).filterNotNull()

    fun notifyUiMode(newMode: UiMode) {
        uiModeState.value = newMode
    }
}
