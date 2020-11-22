package dev.mkeeda.day_night_wallpaper.ui.editor

import android.net.Uri
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.core.net.toUri
import androidx.ui.tooling.preview.Preview
import dev.mkeeda.day_night_wallpaper.ui.theme.DayNightWallpaperTheme

@Composable
fun EditorScreen(
    onSelectImage: () -> Unit,
    viewModel: EditorViewModel
) {
    val selectedImageUri = viewModel.selectedImageUri.collectAsState(initial = null)
    EditorContent(
        onSelectImage = onSelectImage,
        selectedImageUri = selectedImageUri.value
    )
}

@Composable
fun EditorContent(
    onSelectImage: () -> Unit,
    selectedImageUri: Uri?
) {
    Surface(color = MaterialTheme.colors.background) {
        Column {
            Button(onClick = onSelectImage){
                Text(text = "select")
            }
            selectedImageUri?.let {
                Text(text = it.toString())
            }
        }
    }
}

@Preview
@Composable
fun EditorScreenPreview() {
    DayNightWallpaperTheme {
        EditorContent(
            onSelectImage = {},
            selectedImageUri = "content://path/to/image".toUri()
        )
    }
}
