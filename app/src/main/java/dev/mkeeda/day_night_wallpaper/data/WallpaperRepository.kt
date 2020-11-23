package dev.mkeeda.day_night_wallpaper.data

import android.content.Context
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WallpaperRepository(
    context: Context
) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "wallpaper"
    )
    private val lightKey = preferencesKey<String>("light")
    private val darkKey = preferencesKey<String>("dark")

    val wallpaperFile: Flow<WallpaperFile?> = dataStore.data.map { preferences ->
        // TODO: if values is null, Flow emits null not empty string.
        WallpaperFile(
            lightImage = ThemeImage.Light((preferences[lightKey] ?: "").toUri()),
            darkImage = ThemeImage.Dark((preferences[darkKey] ?: "").toUri()),
        )
    }

    suspend fun update(newImage: ThemeImage) {
        dataStore.edit { preferences ->
            when(newImage) {
                is ThemeImage.Light -> preferences.set(key = lightKey, newImage.uri.toString())
                is ThemeImage.Dark -> preferences.set(key = darkKey, newImage.uri.toString())
            }
        }
    }
}
