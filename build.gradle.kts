plugins {
    java
    kotlin("jvm")
}

group = "org.mpris"
version = libs.versions.mprisJava.get()

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.dbusJava)
    implementation(libs.jetbrainsAnnotations)
}
