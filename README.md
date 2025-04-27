# mpris-java

100% Pure java implementation of MPRIS (MPRIS D-Bus Interface Specification)

Taken from the freedesktop.org site:

The Media Player Remote Interfacing Specification is a standard D-Bus interface which aims to provide a common programmatic API for controlling media players.

It provides a mechanism for discovery, querying and basic playback control of compliant media players, as well as a tracklist interface which is used to add context to the active media item.

## Building the Project

This project uses Gradle with Kotlin DSL (build.gradle.kts) as its build system.

### First-time setup

Before building the project for the first time, you need to generate the Gradle wrapper files:

```bash
gradle wrapper
```

This will create the necessary wrapper files, including the gradle-wrapper.jar file.

### Version Management

This project uses a centralized version management approach with a `gradle.properties` file in the root directory. This file contains version information for the project and its dependencies, making it easy to update versions in a single place.

The version information is defined in the following properties:
- `mprisJavaVersion`: The version of the mpris-java library
- `dbusJavaVersion`: The version of the dbus-java dependency
- `jetbrainsAnnotationsVersion`: The version of the Jetbrains annotations dependency

### Building

To build the project:

```bash
./gradlew build
```

This will compile the code and create a JAR file in the `build/libs` directory.

## Demo Applications

Demo applications are included in a separate module (`mpris-java-demos`) to show how to use this library to create media players that display notifications. The demos include:

1. A command-line interface demo (MPRISNotificationDemo)
2. A graphical user interface demo using Java Swing (MPRISSwingDemo)

See the [demo README](mpris-java-demos/src/main/java/org/mpris/demo/README.md) for more information on how to run and use the demos.

### Running the Demos

To build and run the demos:

```bash
# Build the entire project including demos
./gradlew build

# Run the command-line demo
./gradlew :mpris-java-demos:runNotificationDemo

# Run the Swing GUI demo
./gradlew :mpris-java-demos:runSwingDemo
```
