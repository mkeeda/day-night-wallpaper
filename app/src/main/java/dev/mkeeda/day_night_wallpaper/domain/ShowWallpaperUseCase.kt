package dev.mkeeda.day_night_wallpaper.domain

import android.net.Uri
import dev.mkeeda.day_night_wallpaper.data.UiMode
import dev.mkeeda.day_night_wallpaper.data.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

class ShowWallpaperUseCase(
    private val wallpaperRepository: WallpaperRepository
) {
    fun execute(uiMode: Flow<UiMode>): Flow<Uri?> {
        return combine(
            wallpaperRepository.wallpaperFile.filterNotNull(),
            uiMode
        ) { wallpaperFile, mode ->
            when (mode) {
                UiMode.Light -> wallpaperFile.lightImage?.uri
                UiMode.Dark -> wallpaperFile.darkImage?.uri
            }
        }
    }
}
