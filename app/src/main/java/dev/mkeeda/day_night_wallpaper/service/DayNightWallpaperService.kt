package dev.mkeeda.day_night_wallpaper.service

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import dev.mkeeda.day_night_wallpaper.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DayNightWallpaperService : WallpaperService() {
    private val uiMode = MutableStateFlow(false)

    override fun onCreateEngine(): Engine {
        return DayNightWallpaperEngine()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        println("⭐ configure changed")
        uiMode.value = isDarkModeOn()
    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    inner class DayNightWallpaperEngine : Engine() {
        private val engineJob = Job()
        private val engineScope = CoroutineScope(Dispatchers.Main + engineJob)

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            holder ?: return
            drawImage(holder)
            println("⭐ surface created ${isDarkModeOn()}")
            engineScope.launch {
                uiMode.collect {
                    println("⭐ collect ${isDarkModeOn()}")
                }
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
            println("⭐ surface changed ${isDarkModeOn()}")
        }

        override fun onDestroy() {
            super.onDestroy()
            engineScope.cancel()
        }

        private fun isDarkModeOn(): Boolean {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return currentNightMode == Configuration.UI_MODE_NIGHT_YES
        }

        private fun drawImage(holder: SurfaceHolder) {
            val canvas = holder.lockCanvas()
            val srcBitmap = BitmapFactory.decodeResource(resources, R.drawable.sample_image)

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
            val scaledBitmap = Bitmap.createScaledBitmap(srcBitmap, scaledWidth, scaledHeight, true)

            val centerX = (canvas.width - scaledWidth) / 2.0f
            val centerY = (canvas.height - scaledHeight) / 2.0f

            canvas.drawColor(Color.BLACK)
            canvas.drawBitmap(scaledBitmap, centerX, centerY, Paint())

            holder.unlockCanvasAndPost(canvas)
        }
    }
}
