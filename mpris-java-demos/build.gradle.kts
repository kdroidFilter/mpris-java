plugins {
    java
    application
}

group = "org.mpris"
version = rootProject.property("mprisJavaVersion") as String

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":"))  // Dependency on the main module
    implementation("com.github.hypfvieh:dbus-java:${rootProject.property("dbusJavaVersion")}")
    implementation("org.jetbrains:annotations:${rootProject.property("jetbrainsAnnotationsVersion")}")
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

// Create a fat JAR with all dependencies
tasks.register<Jar>("fatJar") {
    archiveFileName.set("mpris-java-demos.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "org.mpris.demo.MPRISNotificationDemo"
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get())
}

tasks.build {
    dependsOn("fatJar")
}
