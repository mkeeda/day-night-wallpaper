package dev.mkeeda.day_night_wallpaper.ui.editor

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import dev.mkeeda.day_night_wallpaper.ui.theme.DayNightWallpaperTheme

@Composable
fun EditorScreen(
    onSelectImage: () -> Unit,
    viewModel: EditorViewModel = viewModel()
) {
    Surface(color = MaterialTheme.colors.background) {
        Column {
            Button(onClick = onSelectImage){
                Text(text = "select")
            }
            viewModel.selectedImageUri?.let {
                Text(text = it.toString())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditorScreenPreview() {
    DayNightWallpaperTheme {
        EditorScreen(onSelectImage = {})
    }
}
