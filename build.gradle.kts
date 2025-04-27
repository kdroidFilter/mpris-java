plugins {
    java
}

group = "org.mpris"
version = project.property("mprisJavaVersion") as String

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("com.github.hypfvieh:dbus-java:${project.property("dbusJavaVersion")}")
    implementation("org.jetbrains:annotations:${project.property("jetbrainsAnnotationsVersion")}")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Create a JAR with the library
tasks.register<Jar>("libraryJar") {
    archiveFileName.set("mpris-java.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
}

tasks.build {
    dependsOn("libraryJar")
}
