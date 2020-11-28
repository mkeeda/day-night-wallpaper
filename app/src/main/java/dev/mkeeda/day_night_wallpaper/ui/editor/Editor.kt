package dev.mkeeda.day_night_wallpaper.ui.editor

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.ui.tooling.preview.Preview
import dev.mkeeda.day_night_wallpaper.data.UiMode
import dev.mkeeda.day_night_wallpaper.ui.theme.DayNightWallpaperTheme

@Composable
fun EditorScreen(
    onSelectImage: (UiMode) -> Unit,
    viewModel: EditorViewModel
) {
    val state by viewModel.wallpaperFile.collectAsState(initial = null)
    EditorContent(
        onSelectImage = onSelectImage,
        lightImageUri = state?.lightImage?.uri,
        darkImageUri = state?.darkImage?.uri
    )
}

@Composable
fun EditorContent(
    onSelectImage: (UiMode) -> Unit,
    lightImageUri: Uri?,
    darkImageUri: Uri?
) {
    Surface(color = MaterialTheme.colors.background) {
        Column(Modifier.padding(8.dp)) {
            ImageSelector(
                title = "select light theme image",
                onSelectImage = { onSelectImage(UiMode.Light) },
                selectedImageUri = lightImageUri
            )
            Spacer(modifier = Modifier.padding(8.dp))
            ImageSelector(
                title = "select dark theme image",
                onSelectImage = { onSelectImage(UiMode.Dark) },
                selectedImageUri = darkImageUri
            )
        }
    }
}

@Composable
fun ImageSelector(
    title: String,
    onSelectImage: () -> Unit,
    selectedImageUri: Uri?
) {
    Column {
        Button(onClick = onSelectImage){
            Text(text = title)
        }
        selectedImageUri?.let {
            Text(text = it.toString())
        }
    }
}

@Preview
@Composable
fun EditorScreenPreview() {
    DayNightWallpaperTheme {
        EditorContent(
            onSelectImage = {},
            lightImageUri = "content://path/to/light/image".toUri(),
            darkImageUri = "content://path/to/dark/image".toUri(),
        )
    }
}
