package dev.mkeeda.day_night_wallpaper.extention

import android.content.ContextWrapper
import android.content.res.Configuration

fun ContextWrapper.isDarkTheme(): Boolean {
    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}

/**
 * For API 29+
 * This is the same implementation as Configuration#isNightModeActive.
 * isNightModeActive method requires API 30 but dark theme supports API 29+.
 */
fun Configuration.isDarkTheme(): Boolean {
    val currentNightMode = uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}
