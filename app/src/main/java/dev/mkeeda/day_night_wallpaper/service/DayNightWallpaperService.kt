package dev.mkeeda.day_night_wallpaper.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.view.SurfaceHolder
import androidx.core.graphics.scale
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import dev.mkeeda.day_night_wallpaper.DayNightWallpaperApp
import dev.mkeeda.day_night_wallpaper.data.WallpaperRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext

class DayNightWallpaperService : LifecycleWallpaperService() {
    override fun onCreateEngineWithLifecycle(): LifecycleEngine {
        val wallpaperRepository = (application as DayNightWallpaperApp).wallpaperRepository
        return DayNightWallpaperEngine(wallpaperRepository)
    }

    inner class DayNightWallpaperEngine(
        private val wallpaperRepository: WallpaperRepository
    ) : LifecycleEngine() {
        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)

            lifecycleScope.launchWhenStarted {
                wallpaperRepository.wallpaperFlow
                    .filterNotNull()
                    .collect { wallpaper ->
                        drawImage(uri = wallpaper.lightImageUri.toUri())
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
