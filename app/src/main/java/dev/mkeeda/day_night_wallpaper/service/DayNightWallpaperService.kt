package dev.mkeeda.day_night_wallpaper.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import dev.mkeeda.day_night_wallpaper.R

class DayNightWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine {
        return DayNightWallpaperEngine()
    }

    inner class DayNightWallpaperEngine : Engine() {
        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            holder ?: return
            drawImage(holder)
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
