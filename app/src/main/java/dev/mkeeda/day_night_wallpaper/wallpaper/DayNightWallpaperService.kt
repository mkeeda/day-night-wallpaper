package dev.mkeeda.day_night_wallpaper.wallpaper

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.view.SurfaceHolder
import androidx.core.graphics.scale
import androidx.lifecycle.lifecycleScope
import dev.mkeeda.day_night_wallpaper.DayNightWallpaperApp
import dev.mkeeda.day_night_wallpaper.data.UiMode
import dev.mkeeda.day_night_wallpaper.domain.ShowWallpaperUseCase
import dev.mkeeda.day_night_wallpaper.extention.isDarkTheme
import dev.mkeeda.day_night_wallpaper.util.LifecycleWallpaperService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class DayNightWallpaperService : LifecycleWallpaperService() {
    private lateinit var viewModel: WallpaperViewModel

    override fun onCreate() {
        super.onCreate()

        // FIXME: Use DI container
        val wallpaperRepository = (application as DayNightWallpaperApp).wallpaperRepository
        val showWallpaperUseCase = ShowWallpaperUseCase(wallpaperRepository)
        viewModel = WallpaperViewModel(
            showWallpaperUseCase = showWallpaperUseCase,
            initialUiMode = if (isDarkTheme()) UiMode.Dark else UiMode.Light
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.notifyUiMode(newMode = if (newConfig.isDarkTheme()) UiMode.Dark else UiMode.Light)
    }

    override fun onCreateEngineWithLifecycle(): LifecycleWallpaperService.LifecycleEngine {
        return DayNightWallpaperEngine(viewModel)
    }

    inner class DayNightWallpaperEngine(
        private val viewModel: WallpaperViewModel
    ) : LifecycleWallpaperService.LifecycleEngine() {
        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)

            lifecycleScope.launchWhenStarted {
                viewModel.renderedWallpaper
                    .collect { filePath ->
                        drawImage(uri = filePath)
                    }
            }
        }

        private suspend fun drawImage(uri: Uri) {
            withContext(Dispatchers.IO) {
                val canvas = surfaceHolder.lockCanvas()
                val srcBitmap = uri.loadBitmap()

                // Scale to fit center of screen
                val originalWidth = srcBitmap.width
                val originalHeight = srcBitmap.height
                val (scaledWidth, scaledHeight) = when {
                    originalWidth > originalHeight -> {
                        val ratio = originalHeight.toFloat() / originalWidth.toFloat()
                        val scaledWidth = (canvas.width * ratio).toInt()
                        scaledWidth to canvas.height
                    }
                    originalWidth < originalHeight -> {
                        val ratio = originalWidth.toFloat() / originalHeight.toFloat()
                        val scaledHeight = (canvas.height * ratio).toInt()
                        canvas.width to scaledHeight
                    }
                    else -> {
                        canvas.width to canvas.height
                    }
                }
                val scaledBitmap = srcBitmap.scale(width = scaledWidth, height = scaledHeight)

                val centerX = (canvas.width - scaledWidth) / 2.0f
                val centerY = (canvas.height - scaledHeight) / 2.0f

                canvas.drawColor(Color.BLACK)
                canvas.drawBitmap(scaledBitmap, centerX, centerY, Paint())

                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }

    private fun Uri.loadBitmap(): Bitmap {
        return contentResolver.openFileDescriptor(this, "r").use { parcelFileDescriptor ->
            BitmapFactory.decodeFileDescriptor(parcelFileDescriptor?.fileDescriptor)
        }
    }
}
