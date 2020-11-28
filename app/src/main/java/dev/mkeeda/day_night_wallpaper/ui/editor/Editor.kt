package dev.mkeeda.day_night_wallpaper.ui.editor

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
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
    val state by viewModel.viewState.collectAsState()
    EditorContent(
        wallpaperThemes = state.wallpaperThemes,
        selectedTheme = state.selectedTheme,
        onSelectTheme = viewModel::onSelectTheme,
        onSelectImage = onSelectImage,
        lightImageUri = state.wallpaperFile?.lightImage?.uri,
        darkImageUri = state.wallpaperFile?.darkImage?.uri
    )
}

@Composable
fun EditorContent(
    wallpaperThemes: List<UiMode>,
    selectedTheme: UiMode,
    onSelectTheme: (UiMode) -> Unit,
    onSelectImage: (UiMode) -> Unit,
    lightImageUri: Uri?,
    darkImageUri: Uri?
) {
    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            EditorTabs(
                wallpaperThemes = wallpaperThemes,
                selectedTheme = selectedTheme,
                onTabSelected = onSelectTheme
            )
            when(selectedTheme) {
                UiMode.Light -> WallpaperSelector(
                    onSelectImage = { onSelectImage(UiMode.Light) },
                    selectedImageUri = lightImageUri
                )
                UiMode.Dark -> WallpaperSelector(
                    onSelectImage = { onSelectImage(UiMode.Dark) },
                    selectedImageUri = darkImageUri
                )
            }
        }
    }
}

@Composable
fun EditorTabs(
    wallpaperThemes: List<UiMode>,
    selectedTheme: UiMode,
    onTabSelected: (UiMode) -> Unit
) {
    val selectedCategoryIndex = wallpaperThemes.indexOfFirst { it == selectedTheme }
    TabRow(selectedTabIndex = selectedCategoryIndex) {
        wallpaperThemes.forEachIndexed { index, uiMode ->
            Tab(
                selected = index == selectedCategoryIndex,
                onClick = { onTabSelected(uiMode) },
                text = {
                    Text(
                        text = when (uiMode) {
                            UiMode.Light -> "Light"
                            UiMode.Dark -> "Dark"
                        }
                    )
                }
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
    var selectedTheme by remember { mutableStateOf(UiMode.Dark) }
    DayNightWallpaperTheme {
        EditorContent(
            wallpaperThemes = listOf(UiMode.Light, UiMode.Dark),
            selectedTheme = selectedTheme,
            onSelectTheme = { selectedTheme = it},
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
