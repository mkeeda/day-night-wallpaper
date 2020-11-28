package dev.mkeeda.day_night_wallpaper.ui.editor

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.mkeeda.day_night_wallpaper.R
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
        ScrollableColumn(Modifier.padding(8.dp)) {
            WallpaperSelector(
                title = "select light theme image",
                onSelectImage = { onSelectImage(UiMode.Light) },
                selectedImageUri = lightImageUri
            )
            Spacer(modifier = Modifier.padding(8.dp))
            WallpaperSelector(
                title = "select dark theme image",
                onSelectImage = { onSelectImage(UiMode.Dark) },
                selectedImageUri = darkImageUri
            )
        }
    }
}

@Composable
fun WallpaperSelector(
    title: String,
    onSelectImage: () -> Unit,
    selectedImageUri: Uri?
) {
    Column {
        Button(onClick = onSelectImage){
            Text(text = title)
        }
        selectedImageUri?.let {
            WallpaperPreview(selectedImageUri = it)
        }
    }
}

@Composable
fun WallpaperPreview(
    selectedImageUri: Uri
) {
    CoilImage(
        data = selectedImageUri,
        loading = {
            Icon(
                asset = Icons.Default.InsertPhoto,
                tint = MaterialTheme.colors.onSurface
            )
        },
        error = {
            Icon(
                asset = Icons.Default.BrokenImage,
                tint = MaterialTheme.colors.error
            )
        }
    )
}

@Preview
@Composable
fun EditorScreenPreview() {
    val sampleUri = convertUri(context = ContextAmbient.current, drawableResId = R.drawable.sample_image)
    DayNightWallpaperTheme {
        EditorContent(
            onSelectImage = {},
            lightImageUri = sampleUri,
            darkImageUri = sampleUri,
        )
    }
}

fun convertUri(context: Context, @DrawableRes drawableResId: Int): Uri {
    val packageName = context.resources.getResourcePackageName(drawableResId)
    val resourceType = context.resources.getResourceTypeName(drawableResId)
    val entryName = context.resources.getResourceEntryName(drawableResId)
    return "${ContentResolver.SCHEME_ANDROID_RESOURCE}://$packageName/$resourceType/$entryName".toUri()
}
