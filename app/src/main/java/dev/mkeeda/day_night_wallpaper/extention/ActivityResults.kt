package dev.mkeeda.day_night_wallpaper.extention

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import dev.mkeeda.day_night_wallpaper.data.ThemeImage
import dev.mkeeda.day_night_wallpaper.data.UiMode

fun ComponentActivity.registerForLightAndDarkImageActivityResult(
    callback: (ThemeImage) -> Unit
): LightAndDarkImageResultLauncher {
    val lightLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { lightImageUri: Uri? ->
        lightImageUri?.let {
            callback.invoke(ThemeImage.Light(uri = it))
        }
    }

    val darkLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { darkImageUri: Uri? ->
        darkImageUri?.let {
            callback.invoke(ThemeImage.Dark(uri = it))
        }
    }

    return LightAndDarkImageResultLauncher(lightLauncher, darkLauncher)
}

class LightAndDarkImageResultLauncher(
    private val lightLauncher: ActivityResultLauncher<Array<out String>>,
    private val darkLauncher: ActivityResultLauncher<Array<out String>>
) {
    fun launchWith(uiMode: UiMode) {
        when (uiMode) {
            UiMode.Light -> lightLauncher.launch(arrayOf("image/*"))
            UiMode.Dark -> darkLauncher.launch(arrayOf("image/*"))
        }
    }
}
