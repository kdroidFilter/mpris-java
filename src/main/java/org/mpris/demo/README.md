# MPRIS Notification Demo

This is a simple demo application that shows how to use the mpris-java library to create a media player that implements the MPRIS (Media Player Remote Interfacing Specification) D-Bus interface. When run, this demo will register a media player on the D-Bus, which can then be controlled by other applications and will display notifications when track information or playback status changes.

## What is MPRIS?

MPRIS (Media Player Remote Interfacing Specification) is a standard D-Bus interface that provides a common way for applications to control media players. It allows desktop environments and other applications to:

- Display notifications when tracks change
- Show media controls in the system tray or notification area
- Control playback (play, pause, stop, next, previous)
- Display track information (title, artist, album, artwork)

## How to Run the Demo

1. Make sure you have Java 8 or later installed
2. Build the project with Maven:
   ```
   mvn package
   ```
3. Run the demo application:
   ```
   java -cp target/mpris-java.jar org.mpris.demo.MPRISNotificationDemo
   ```

## Using the Demo

Once the demo is running, you'll see a command prompt where you can enter commands to control the media player:

- `play` - Start playback (changes playback status to PLAYING)
- `pause` - Pause playback (changes playback status to PAUSED)
- `stop` - Stop playback (changes playback status to STOPPED)
- `next` - Simulate changing to the next track
- `prev` - Simulate changing to the previous track
- `quit` - Exit the application

When you change the playback status or track, you should see notifications appear on your desktop (if your desktop environment supports MPRIS notifications).

## How It Works

The demo application:

1. Creates a D-Bus connection to the session bus
2. Creates a media player with a unique name ("mprisNotificationDemo")
3. Sets up metadata for a sample track
4. Configures the media player with various properties and callbacks
5. Registers the media player on the D-Bus
6. Provides a simple command-line interface to control the player

When you use the commands to change playback status or tracks, the demo updates the appropriate properties on the D-Bus, which triggers notifications from your desktop environment.

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