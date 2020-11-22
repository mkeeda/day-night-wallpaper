package dev.mkeeda.day_night_wallpaper.buildSrc

object Libs {
    object Kotlin {
        const val version = "1.4.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:4.2.0-alpha16"

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.3.2"
        const val appCompat = "androidx.appcompat:appcompat:1.2.0"

        object Compose {
            const val version = "1.0.0-alpha06"
            const val ui = "androidx.compose.ui:ui:$version"
            const val material = "androidx.compose.material:material:$version"
            const val uiTooling = "androidx.ui:ui-tooling:$version"
        }

        const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.0-alpha06"
        const val activityKtx = "androidx.activity:activity-ktx:1.2.0-beta01"
        const val dataStorePreferences = "androidx.datastore:datastore-preferences:1.0.0-alpha04"

        object Test {
            const val junit = "androidx.test.ext:junit:1.1.2"
            const val espresso = "androidx.test.espresso:espresso-core:3.3.0"
        }
    }

    const val material = "com.google.android.material:material:1.2.1"
    const val junit = "junit:junit:4.+"
}