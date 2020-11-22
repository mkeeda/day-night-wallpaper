package dev.mkeeda.day_night_wallpaper.service

import android.content.Intent
import android.service.wallpaper.WallpaperService
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher

/**
 * A WallpaperService implements LifecycleOwner.
 * To see
 *   - LifecycleService document: https://developer.android.com/reference/androidx/lifecycle/LifecycleService
 *   - LifecycleService implementation: https://cs.android.com/androidx/platform/frameworks/support/+/androidx-master-dev:lifecycle/lifecycle-service/src/main/java/androidx/lifecycle/LifecycleService.java
 */
abstract class LifecycleWallpaperService : WallpaperService(), LifecycleOwner {
    @Suppress("LeakingThis")
    // FIXME: Leaking 'this' in constructor of non-final
    private val mDispatcher = ServiceLifecycleDispatcher(this)

    @CallSuper
    override fun onCreate() {
        mDispatcher.onServicePreSuperOnCreate()
        super.onCreate()
    }

    @Suppress("deprecation")
    @CallSuper
    override fun onStart(intent: Intent?, startId: Int) {
        mDispatcher.onServicePreSuperOnStart()
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
        mDispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }

    override fun getLifecycle(): Lifecycle {
        return mDispatcher.lifecycle
    }

    final override fun onCreateEngine(): Engine {
        // Workaround:
        // WallpaperService#onBind is final method,
        // so dispatcher cannot be received a ON_START event on bind.
        mDispatcher.onServicePreSuperOnBind()
        return onCreateEngineWithLifecycle()
    }

    abstract fun onCreateEngineWithLifecycle(): Engine
}
