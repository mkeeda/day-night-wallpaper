package dev.mkeeda.day_night_wallpaper.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.putAll
import androidx.datastore.preferences.core.to
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
            lightImageUri = preferences[lightKey] ?: "",
            darkImageUri = ""
        )
    }

    suspend fun update(newWallpaperFile: WallpaperFile) {
        dataStore.edit { preferences ->
            preferences.putAll(
                lightKey to newWallpaperFile.lightImageUri,
                darkKey to newWallpaperFile.darkImageUri
            )
        }
    }
}
