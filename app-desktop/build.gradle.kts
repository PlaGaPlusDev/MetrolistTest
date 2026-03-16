plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation(libs.ktor.client.cio)
                implementation(project(":innertube"))
                implementation(project(":lrclib"))
                implementation(project(":lastfm"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.metrolist.music.MainKt"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "Metrolist"
            packageVersion = "1.0.0"
        }
    }
}
