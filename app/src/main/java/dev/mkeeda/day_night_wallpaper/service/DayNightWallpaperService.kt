package dev.mkeeda.day_night_wallpaper.service

import android.graphics.Color
import android.graphics.Paint
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class DayNightWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine {
        return DayNightWallpaperEngine()
    }

    inner class DayNightWallpaperEngine : Engine() {
        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            doDraw()
        }

        private fun doDraw() {
            val canvas = surfaceHolder.lockCanvas()
            val x = (canvas.width / 2).toFloat()
            val y = (canvas.height / 2).toFloat()
            val paint = Paint()
            canvas.drawColor(Color.BLACK)
            paint.textSize = 56F
            paint.color = Color.WHITE
            canvas.drawText("Hello wallpaper!", x, y, paint)
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }
}
