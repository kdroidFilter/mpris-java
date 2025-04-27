# MPRIS Demo Applications

This package contains demo applications that show how to use the mpris-java library to create a media player that implements the MPRIS (Media Player Remote Interfacing Specification) D-Bus interface. When run, these demos will register a media player on the D-Bus, which can then be controlled by other applications and will display notifications when track information or playback status changes.

There are two demo applications available:
1. **MPRISNotificationDemo** - A command-line interface demo
2. **MPRISSwingDemo** - A graphical user interface demo using Java Swing

## What is MPRIS?

MPRIS (Media Player Remote Interfacing Specification) is a standard D-Bus interface that provides a common way for applications to control media players. It allows desktop environments and other applications to:

- Display notifications when tracks change
- Show media controls in the system tray or notification area
- Control playback (play, pause, stop, next, previous)
- Display track information (title, artist, album, artwork)

## How to Run the Demos

### Command-Line Demo (MPRISNotificationDemo)

1. Make sure you have Java 8 or later installed
2. Run the demo using the Gradle task:
   ```
   ./gradlew runDemo
   ```

Alternatively, you can build the project and run the JAR file directly:
1. Build the project with Gradle:
   ```
   ./gradlew build
   ```
2. Run the command-line demo application:
   ```
   java -cp build/libs/mpris-java.jar org.mpris.demo.MPRISNotificationDemo
   ```

### Swing GUI Demo (MPRISSwingDemo)

1. Make sure you have Java 8 or later installed
2. Build the project with Gradle:
   ```
   ./gradlew build
   ```
3. Run the Swing GUI demo application:
   ```
   java -cp build/libs/mpris-java.jar org.mpris.demo.MPRISSwingDemo
   ```

## Using the Demos

### Command-Line Demo

Once the command-line demo is running, you'll see a prompt where you can enter commands to control the media player:

- `play` - Start playback (changes playback status to PLAYING)
- `pause` - Pause playback (changes playback status to PAUSED)
- `stop` - Stop playback (changes playback status to STOPPED)
- `next` - Simulate changing to the next track
- `prev` - Simulate changing to the previous track
- `quit` - Exit the application

### Swing GUI Demo

The Swing GUI demo provides a graphical interface with buttons for controlling the media player:

- **Play** - Start playback (changes playback status to PLAYING)
- **Pause** - Pause playback (changes playback status to PAUSED)
- **Stop** - Stop playback (changes playback status to STOPPED)
- **Previous** - Simulate changing to the previous track
- **Next** - Simulate changing to the next track
- **Quit** - Exit the application

The GUI also displays the current track information (title, artist, album) and playback status.

When you change the playback status or track in either demo, you should see notifications appear on your desktop (if your desktop environment supports MPRIS notifications).

## How It Works

Both demo applications follow the same basic workflow:

1. Create a D-Bus connection to the session bus
2. Create a media player with a unique name
3. Set up metadata for a sample track
4. Configure the media player with various properties and callbacks
5. Register the media player on the D-Bus
6. Provide an interface to control the player (command-line or GUI)

The main difference is in the user interface:
- The command-line demo uses a text-based interface with a Scanner to read user input
- The Swing GUI demo uses buttons and labels to provide a graphical interface

When you use either interface to change playback status or tracks, the demo updates the appropriate properties on the D-Bus, which triggers notifications from your desktop environment.

## Requirements

- Java 8 or later
- A Linux desktop environment that supports MPRIS (GNOME, KDE, XFCE, etc.)
- D-Bus

## Troubleshooting

If you don't see notifications when changing tracks or playback status:

1. Make sure your desktop environment supports MPRIS notifications
2. Check that D-Bus is running (`ps aux | grep dbus`)
3. Try using a D-Bus monitoring tool to verify that the media player is registered:
   ```
   dbus-monitor "interface='org.mpris.MediaPlayer2.Player'"
   ```
