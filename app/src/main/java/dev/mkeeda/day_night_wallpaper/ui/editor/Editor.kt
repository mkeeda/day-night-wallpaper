package dev.mkeeda.day_night_wallpaper.ui.editor

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
                onSelectImage = { onSelectImage(UiMode.Light) },
                selectedImageUri = lightImageUri
            )
            Spacer(modifier = Modifier.padding(8.dp))
            WallpaperSelector(
                onSelectImage = { onSelectImage(UiMode.Dark) },
                selectedImageUri = darkImageUri
            )
        }
    }
}

@Composable
fun WallpaperSelector(
    modifier: Modifier = Modifier,
    onSelectImage: () -> Unit,
    selectedImageUri: Uri?
) {
    Surface(
        modifier = modifier.clickable(onClick = onSelectImage)
    ) {
        if (selectedImageUri == null) {
            EmptyWallpaper()
        } else {
            WallpaperPreview(selectedImageUri = selectedImageUri)
        }
    }
}

@Composable
fun EmptyWallpaper() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(asset = Icons.Default.Wallpaper)
        Text(text = "Tap and select wallpaper")
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
            darkImageUri = null,
        )
    }
}

fun convertUri(context: Context, @DrawableRes drawableResId: Int): Uri {
    val packageName = context.resources.getResourcePackageName(drawableResId)
    val resourceType = context.resources.getResourceTypeName(drawableResId)
    val entryName = context.resources.getResourceEntryName(drawableResId)
    return "${ContentResolver.SCHEME_ANDROID_RESOURCE}://$packageName/$resourceType/$entryName".toUri()
}
