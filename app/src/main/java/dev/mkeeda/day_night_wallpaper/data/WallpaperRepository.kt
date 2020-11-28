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

    /*
    * This flow emits a WallpaperFile instance if there is a URI in either light or dark.
    * If both light and dark URIs are null, the flow emits null.
    * */
    val wallpaperFile: Flow<WallpaperFile?> = dataStore.data.map { preferences ->
        val lightImageUriString = preferences[lightKey]
        val darkImageUriString = preferences[darkKey]

        if (lightImageUriString == null && darkImageUriString == null) {
            null
        } else {
            WallpaperFile(
                lightImage = lightImageUriString?.let {
                    ThemeImage.Light(it.toUri())
                },
                darkImage = darkImageUriString?.let {
                    ThemeImage.Dark(it.toUri())
                },
            )
        }
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
