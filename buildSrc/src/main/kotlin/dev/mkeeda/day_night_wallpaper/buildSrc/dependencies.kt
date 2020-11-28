package dev.mkeeda.day_night_wallpaper.buildSrc

object Libs {
    object Kotlin {
        const val version = "1.4.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object KotlinX {
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:4.2.0-alpha16"

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.3.2"
        const val appCompat = "androidx.appcompat:appcompat:1.2.0"

        object Compose {
            const val version = "1.0.0-alpha07"
            const val ui = "androidx.compose.ui:ui:$version"
            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val uiTooling = "androidx.ui:ui-tooling:$version"
        }

        object Lifecycle {
            private const val version = "2.3.0-beta01"
            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val service = "androidx.lifecycle:lifecycle-service:$version"
        }

        const val activityKtx = "androidx.activity:activity-ktx:1.2.0-beta01"
        const val dataStorePreferences = "androidx.datastore:datastore-preferences:1.0.0-alpha04"

        object Test {
            const val junit = "androidx.test.ext:junit:1.1.2"
            const val espresso = "androidx.test.espresso:espresso-core:3.3.0"
        }
    }

    object Accompanist {
        const val coil = "dev.chrisbanes.accompanist:accompanist-coil:0.3.3.1"
    }

    const val material = "com.google.android.material:material:1.2.1"

    const val junit = "junit:junit:4.+"
}
