package dev.mkeeda.day_night_wallpaper.domain

import dev.mkeeda.day_night_wallpaper.data.UiMode
import dev.mkeeda.day_night_wallpaper.data.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

class ShowWallpaperUseCase(
    private val wallpaperRepository: WallpaperRepository
) {
    suspend fun execute(uiMode: Flow<UiMode>): Flow<String> {
        return combine(
            wallpaperRepository.wallpaperFlow.filterNotNull(),
            uiMode
        ) { wallpaper, mode ->
            when (mode) {
                UiMode.Light -> wallpaper.lightImageUri
                UiMode.Dark -> wallpaper.darkImageUri
            }
        }
    }
}