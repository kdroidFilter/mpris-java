package org.mpris.demo;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.mpris.MPRISMediaPlayer;
import org.mpris.Metadata;
import org.mpris.mpris.LoopStatus;
import org.mpris.mpris.PlaybackStatus;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Demo application that shows how to use the mpris-java library to create a media player
 * and display notifications when track information or playback status changes.
 */
public class MPRISNotificationDemo {

    public static void main(String[] args) {
        try {
            // Create a DBus connection (SESSION bus is used for desktop applications)
            DBusConnection connection = DBusConnection.newConnection(DBusConnection.DBusBusType.SESSION);

            // Create a media player with a unique name
            MPRISMediaPlayer mediaPlayer = new MPRISMediaPlayer(connection, "mprisNotificationDemo");

            // Create metadata for a track
            Metadata metadata = createSampleTrackMetadata();

            // Create a media player builder with basic properties
            MPRISMediaPlayer.MediaPlayer2Builder mediaPlayer2Builder = new MPRISMediaPlayer.MediaPlayer2Builder()
                    .setCanQuit(true)
                    .setCanRaise(true)
                    .setIdentity("MPRIS Notification Demo")
                    .setDesktopEntry("mpris-notification-demo.desktop")
                    .setSupportedUriSchemes("file", "http", "https")
                    .setSupportedMimeTypes("audio/mpeg", "audio/flac", "audio/ogg")
                    .setOnRaise(o -> System.out.println("Player raised"))
                    .setOnQuit(o -> System.exit(0));

            // Create a player builder with track metadata and playback controls
            MPRISMediaPlayer.PlayerBuilder playerBuilder = new MPRISMediaPlayer.PlayerBuilder()
                    .setPlaybackStatus(PlaybackStatus.STOPPED)
                    .setLoopStatus(LoopStatus.NONE)
                    .setRate(1.0)
                    .setShuffle(false)
                    .setMetadata(metadata)
                    .setVolume(1.0)
                    .setPosition(0)
                    .setMinimumRate(0.1)
                    .setMaximumRate(2.0)
                    .setCanGoNext(true)
                    .setCanGoPrevious(true)
                    .setCanPlay(true)
                    .setCanPause(true)
                    .setCanSeek(true)
                    .setCanControl(true)
                    .setOnNext(o -> System.out.println("Next track"))
                    .setOnPrevious(o -> System.out.println("Previous track"))
                    .setOnPause(o -> System.out.println("Pause"))
                    .setOnPlayPause(o -> System.out.println("Play/Pause"))
                    .setOnStop(o -> System.out.println("Stop"))
                    .setOnPlay(o -> System.out.println("Play"))
                    .setOnSeek(position -> System.out.println("Seek to " + position))
                    .setOnOpenURI(uri -> System.out.println("Open URI: " + uri));

            // Build the media player with the minimum required interfaces (MediaPlayer2 and Player)
            mediaPlayer.buildMPRISMediaPlayer2None(mediaPlayer2Builder, playerBuilder);

            // Create the media player on the D-Bus
            mediaPlayer.create();

            System.out.println("MPRIS Notification Demo is running.");
            System.out.println("The media player is now available on the D-Bus.");
            System.out.println("You should see notifications when playback status changes.");
            System.out.println("\nCommands:");
            System.out.println("play - Start playback");
            System.out.println("pause - Pause playback");
            System.out.println("stop - Stop playback");
            System.out.println("next - Simulate next track");
            System.out.println("prev - Simulate previous track");
            System.out.println("quit - Exit the application");

            // Simple command-line interface to control the demo
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.print("> ");
                String command = scanner.nextLine().trim().toLowerCase();

                switch (command) {
                    case "play":
                        playerBuilder.setPlaybackStatus(PlaybackStatus.PLAYING);
                        System.out.println("Playback started");
                        break;
                    case "pause":
                        playerBuilder.setPlaybackStatus(PlaybackStatus.PAUSED);
                        System.out.println("Playback paused");
                        break;
                    case "stop":
                        playerBuilder.setPlaybackStatus(PlaybackStatus.STOPPED);
                        System.out.println("Playback stopped");
                        break;
                    case "next":
                        // Simulate changing to the next track by updating metadata
                        playerBuilder.setMetadata(createNextTrackMetadata());
                        System.out.println("Changed to next track");
                        break;
                    case "prev":
                        // Simulate changing to the previous track by updating metadata
                        playerBuilder.setMetadata(createPreviousTrackMetadata());
                        System.out.println("Changed to previous track");
                        break;
                    case "quit":
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown command: " + command);
                        break;
                }
            }

            // Clean up
            connection.close();
            System.out.println("Demo application closed.");

        } catch (DBusException | URISyntaxException | IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates sample metadata for a track
     */
    private static Metadata createSampleTrackMetadata() throws URISyntaxException {
        return new Metadata.Builder()
                .setTrackID(new DBusPath("/org/mpris/MediaPlayer2/Track/1"))
                .setLength(180000000) // 3 minutes in microseconds
                .setArtURL(new URI("https://ca.slack-edge.com/T09229ZC6-U070RR9Q6BE-b8b2da805ea7-512"))
                .setAlbumName("Sample Album")
                .setAlbumArtists(Arrays.asList("Sample Artist"))
                .setArtists(Arrays.asList("Sample Artist"))
                .setTitle("Sample Track")
                .setTrackNumber(1)
                .setDiscNumber(1)
                .setGenres(Arrays.asList("Rock", "Pop"))
                .build();
    }

    /**
     * Creates metadata for the "next" track
     */
    private static Metadata createNextTrackMetadata() throws URISyntaxException {
        return new Metadata.Builder()
                .setTrackID(new DBusPath("/org/mpris/MediaPlayer2/Track/2"))
                .setLength(240000000) // 4 minutes in microseconds
                .setArtURL(new URI("https://ca.slack-edge.com/T8493FQ76-U01A2EW3XBQ-ee10e32f10e5-512"))
                .setAlbumName("Sample Album")
                .setAlbumArtists(Arrays.asList("Sample Artist"))
                .setArtists(Arrays.asList("Sample Artist"))
                .setTitle("Next Track")
                .setTrackNumber(2)
                .setDiscNumber(1)
                .setGenres(Arrays.asList("Rock", "Pop"))
                .build();
    }

    /**
     * Creates metadata for the "previous" track
     */
    private static Metadata createPreviousTrackMetadata() throws URISyntaxException {
        return new Metadata.Builder()
                .setTrackID(new DBusPath("/org/mpris/MediaPlayer2/Track/3"))
                .setLength(200000000) // 3.33 minutes in microseconds
                .setArtURL(new URI("https://example.com/album-art3.jpg"))
                .setAlbumName("Sample Album")
                .setAlbumArtists(Arrays.asList("Sample Artist"))
                .setArtists(Arrays.asList("Sample Artist"))
                .setTitle("Previous Track")
                .setTrackNumber(3)
                .setDiscNumber(1)
                .setGenres(Arrays.asList("Rock", "Pop"))
                .build();
    }
}
