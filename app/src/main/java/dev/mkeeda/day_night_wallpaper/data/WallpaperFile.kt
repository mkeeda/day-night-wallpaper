package dev.mkeeda.day_night_wallpaper.data

import android.net.Uri

data class WallpaperFile(
    val lightImage: ThemeImage.Light,
    val darkImage: ThemeImage.Dark
)

sealed class ThemeImage {
    data class Light(val uri: Uri): ThemeImage()
    data class Dark(val uri: Uri): ThemeImage()
}
