import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    java
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "org.mpris"
version = libs.versions.mprisJava.get()

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    gradlePluginPortal()
    mavenCentral()
}


dependencies {
    implementation(libs.dbusJava)
    implementation(project(":"))  // Dependency on the main module
    implementation(compose.desktop.currentOs)
    runtimeOnly("org.slf4j:slf4j-simple:2.0.17")

}

compose.desktop {
    application {
        mainClass = "org.mpris.demo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "demo"
            packageVersion = "1.0.0"
        }
    }
}

