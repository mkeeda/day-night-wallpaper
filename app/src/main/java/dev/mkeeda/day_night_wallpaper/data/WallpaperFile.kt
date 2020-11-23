package dev.mkeeda.day_night_wallpaper.data

import android.net.Uri

data class WallpaperFile(
    val lightImage: ThemeImage.Light,
    val darkImage: ThemeImage.Dark
)

interface FileLocationRepresentable {
    val uri: Uri
}

sealed class ThemeImage : FileLocationRepresentable {
    data class Light(override val uri: Uri): ThemeImage()
    data class Dark(override val uri: Uri): ThemeImage()
}
