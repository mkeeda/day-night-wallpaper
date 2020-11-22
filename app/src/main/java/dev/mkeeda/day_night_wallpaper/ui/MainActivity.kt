package dev.mkeeda.day_night_wallpaper.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import dev.mkeeda.day_night_wallpaper.ui.editor.EditorScreen
import dev.mkeeda.day_night_wallpaper.ui.editor.EditorViewModel
import dev.mkeeda.day_night_wallpaper.ui.theme.DayNightWallpaperTheme

class MainActivity : AppCompatActivity() {
    private val viewModel: EditorViewModel by viewModels {
        EditorViewModel.Factory(context = this)
    }

    private val openDocument = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { imageUri: Uri? ->
        imageUri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            viewModel.selectImageUri(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DayNightWallpaperTheme {
                EditorScreen(
                    onSelectImage = {
                        openDocument.launch(arrayOf("image/*"))
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}
