package dev.mkeeda.day_night_wallpaper.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import dev.mkeeda.day_night_wallpaper.DayNightWallpaperApp
import dev.mkeeda.day_night_wallpaper.extention.registerForLightAndDarkImageActivityResult
import dev.mkeeda.day_night_wallpaper.ui.editor.EditorScreen
import dev.mkeeda.day_night_wallpaper.ui.editor.EditorViewModel
import dev.mkeeda.day_night_wallpaper.ui.theme.DayNightWallpaperTheme

class MainActivity : AppCompatActivity() {
    private val viewModel: EditorViewModel by viewModels {
        val wallpaperRepository = (application as DayNightWallpaperApp).wallpaperRepository
        EditorViewModel.Factory(wallpaperRepository)
    }

    private val openDocument = registerForLightAndDarkImageActivityResult { themeImage ->
        contentResolver.takePersistableUriPermission(themeImage.uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        viewModel.selectImageUri(themeImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DayNightWallpaperTheme {
                EditorScreen(
                    onSelectImage = { uiMode ->
                        openDocument.launchWith(uiMode)
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}
