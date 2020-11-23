package dev.mkeeda.day_night_wallpaper.util

import android.content.Intent
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ServiceLifecycleDispatcher

/**
 * A WallpaperService implements LifecycleOwner.
 * To see
 *   - LifecycleService document: https://developer.android.com/reference/androidx/lifecycle/LifecycleService
 *   - LifecycleService implementation: https://cs.android.com/androidx/platform/frameworks/support/+/androidx-master-dev:lifecycle/lifecycle-service/src/main/java/androidx/lifecycle/LifecycleService.java
 */
abstract class LifecycleWallpaperService : WallpaperService(), LifecycleOwner {
    private val serviceLifecycleDispatcher by lazy {
        ServiceLifecycleDispatcher(this)
    }

    @CallSuper
    override fun onCreate() {
        serviceLifecycleDispatcher.onServicePreSuperOnCreate()
        super.onCreate()
    }

    @Suppress("deprecation")
    @CallSuper
    override fun onStart(intent: Intent?, startId: Int) {
        serviceLifecycleDispatcher.onServicePreSuperOnStart()
        super.onStart(intent, startId)
    }

    // this method is added only to annotate it with @CallSuper.
    // In usual service super.onStartCommand is no-op, but in LifecycleService
    // it results in mDispatcher.onServicePreSuperOnStart() call, because
    // super.onStartCommand calls onStart().
    @CallSuper
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    @CallSuper
    override fun onDestroy() {
        serviceLifecycleDispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }

    override fun getLifecycle(): Lifecycle {
        return serviceLifecycleDispatcher.lifecycle
    }

    final override fun onCreateEngine(): Engine {
        // Workaround:
        // WallpaperService#onBind is final method,
        // so dispatcher cannot be received a ON_START event on bind.
        serviceLifecycleDispatcher.onServicePreSuperOnBind()
        return onCreateEngineWithLifecycle()
    }

    abstract fun onCreateEngineWithLifecycle(): LifecycleEngine

    open inner class LifecycleEngine : Engine(), LifecycleOwner {
        private val lifecycleRegistry by lazy {
            LifecycleRegistry(this)
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            super.onCreate(surfaceHolder)
        }

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            super.onSurfaceCreated(holder)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            super.onSurfaceDestroyed(holder)
        }

        override fun onDestroy() {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            super.onDestroy()
        }
    }
}
