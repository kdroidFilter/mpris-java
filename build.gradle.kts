plugins {
    java
}

group = "org.mpris"
version = libs.versions.mprisJava.get()

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.dbusJava)
    implementation(libs.jetbrainsAnnotations)
}
