package dev.mkeeda.day_night_wallpaper.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import dev.mkeeda.day_night_wallpaper.DayNightWallpaperApp
import dev.mkeeda.day_night_wallpaper.data.ThemeImage
import dev.mkeeda.day_night_wallpaper.data.UiMode
import dev.mkeeda.day_night_wallpaper.ui.editor.EditorScreen
import dev.mkeeda.day_night_wallpaper.ui.editor.EditorViewModel
import dev.mkeeda.day_night_wallpaper.ui.theme.DayNightWallpaperTheme

class MainActivity : AppCompatActivity() {
    private val viewModel: EditorViewModel by viewModels {
        val wallpaperRepository = (application as DayNightWallpaperApp).wallpaperRepository
        EditorViewModel.Factory(wallpaperRepository)
    }

    private val openLightImage = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { lightImageUri: Uri? ->
        lightImageUri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            viewModel.selectImageUri(ThemeImage.Light(uri = it))
        }
    }

    private val openDarkImage = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { darkImageUri: Uri? ->
        darkImageUri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            viewModel.selectImageUri(ThemeImage.Dark(uri = it))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DayNightWallpaperTheme {
                EditorScreen(
                    onSelectImage = { uiMode ->
                        when (uiMode) {
                            UiMode.Light -> openLightImage.launch(arrayOf("image/*"))
                            UiMode.Dark -> openDarkImage.launch(arrayOf("image/*"))
                        }
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}
