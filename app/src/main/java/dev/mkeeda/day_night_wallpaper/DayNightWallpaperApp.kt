package dev.mkeeda.day_night_wallpaper

import android.app.Application
import dev.mkeeda.day_night_wallpaper.data.WallpaperRepository

class DayNightWallpaperApp : Application() {
    val wallpaperRepository = WallpaperRepository(context = this)
}
