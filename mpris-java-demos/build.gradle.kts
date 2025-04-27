plugins {
    java
    application
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
    implementation(project(":"))  // Dependency on the main module
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Configure the application plugin to use the notification demo as the main class
application {
    mainClass.set("org.mpris.demo.MPRISNotificationDemo")
}


// Task to run the Swing demo
tasks.register<JavaExec>("runSwingDemo") {
    description = "Run the MPRIS Swing Demo"
    group = "application"

    mainClass.set("org.mpris.demo.MPRISSwingDemo")
    classpath = sourceSets["main"].runtimeClasspath
}
