package org.mpris.demo;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.mpris.MPRISMediaPlayer;
import org.mpris.Metadata;
import org.mpris.mpris.LoopStatus;
import org.mpris.mpris.PlaybackStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * Demo application that shows how to use the mpris-java library to create a media player
 * with a minimal Swing GUI interface.
 */
public class MPRISSwingDemo {
    private JFrame frame;
    private JLabel statusLabel;
    private JLabel trackLabel;
    private JLabel artistLabel;
    private JLabel albumLabel;
    private MPRISMediaPlayer.PlayerBuilder playerBuilder;
    private DBusConnection connection;

    // Track information
    private String currentTitle = "Unknown";
    private String currentArtist = "Unknown";
    private String currentAlbum = "Unknown";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MPRISSwingDemo().initialize();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), 
                        "MPRIS Swing Demo Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private void initialize() throws DBusException, URISyntaxException, IOException {
        // Create a DBus connection (SESSION bus is used for desktop applications)
        connection = DBusConnection.newConnection(DBusConnection.DBusBusType.SESSION);

        // Create a media player with a unique name
        MPRISMediaPlayer mediaPlayer = new MPRISMediaPlayer(connection, "mprisSwingDemo");

        // Create metadata for a track and update instance variables
        Metadata metadata = createSampleTrackMetadata();

        // Create a media player builder with basic properties
        MPRISMediaPlayer.MediaPlayer2Builder mediaPlayer2Builder = new MPRISMediaPlayer.MediaPlayer2Builder()
                .setCanQuit(true)
                .setCanRaise(true)
                .setIdentity("MPRIS Swing Demo")
                .setDesktopEntry("mpris-swing-demo.desktop")
                .setSupportedUriSchemes("file", "http", "https")
                .setSupportedMimeTypes("audio/mpeg", "audio/flac", "audio/ogg")
                .setOnRaise(o -> System.out.println("Player raised"))
                .setOnQuit(o -> System.exit(0));

        // Create a player builder with track metadata and playback controls
        playerBuilder = new MPRISMediaPlayer.PlayerBuilder()
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

        // Create and setup the GUI
        createGUI();

        // Update track info display
        updateTrackInfo(metadata);
    }

    private void createGUI() {
        // Create the main frame
        frame = new JFrame("MPRIS Swing Demo");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    connection.close();
                    System.out.println("Demo application closed.");
                    frame.dispose();
                    System.exit(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Create panels
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Track info panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        statusLabel = new JLabel("Status: STOPPED");
        trackLabel = new JLabel("Track: ");
        artistLabel = new JLabel("Artist: ");
        albumLabel = new JLabel("Album: ");

        infoPanel.add(statusLabel);
        infoPanel.add(trackLabel);
        infoPanel.add(artistLabel);
        infoPanel.add(albumLabel);

        // Control buttons panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        JButton stopButton = new JButton("Stop");
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JButton quitButton = new JButton("Quit");

        // Add action listeners to buttons
        playButton.addActionListener(e -> {
            playerBuilder.setPlaybackStatus(PlaybackStatus.PLAYING);
            statusLabel.setText("Status: PLAYING");
            System.out.println("Playback started");
        });

        pauseButton.addActionListener(e -> {
            playerBuilder.setPlaybackStatus(PlaybackStatus.PAUSED);
            statusLabel.setText("Status: PAUSED");
            System.out.println("Playback paused");
        });

        stopButton.addActionListener(e -> {
            playerBuilder.setPlaybackStatus(PlaybackStatus.STOPPED);
            statusLabel.setText("Status: STOPPED");
            System.out.println("Playback stopped");
        });

        nextButton.addActionListener(e -> {
            try {
                Metadata nextMetadata = createNextTrackMetadata();
                playerBuilder.setMetadata(nextMetadata);
                updateTrackInfo(nextMetadata);
                System.out.println("Changed to next track");
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        });

        prevButton.addActionListener(e -> {
            try {
                Metadata prevMetadata = createPreviousTrackMetadata();
                playerBuilder.setMetadata(prevMetadata);
                updateTrackInfo(prevMetadata);
                System.out.println("Changed to previous track");
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        });

        quitButton.addActionListener(e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });

        // Add buttons to control panel
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(prevButton);
        controlPanel.add(nextButton);
        controlPanel.add(quitButton);

        // Add panels to main panel
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);

        // Set up the frame
        frame.add(mainPanel);
        frame.setSize(500, 250);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void updateTrackInfo(Metadata metadata) {
        // Update UI with current track information
        trackLabel.setText("Track: " + currentTitle);
        artistLabel.setText("Artist: " + currentArtist);
        albumLabel.setText("Album: " + currentAlbum);
    }

    /**
     * Creates sample metadata for a track
     */
    private Metadata createSampleTrackMetadata() throws URISyntaxException {
        // Update instance variables
        currentTitle = "Sample Track";
        currentArtist = "Sample Artist";
        currentAlbum = "Sample Album";

        return new Metadata.Builder()
                .setTrackID(new DBusPath("/org/mpris/MediaPlayer2/Track/1"))
                .setLength(180000000) // 3 minutes in microseconds
                .setArtURL(new URI("https://ca.slack-edge.com/T09229ZC6-U070RR9Q6BE-b8b2da805ea7-512"))
                .setAlbumName(currentAlbum)
                .setAlbumArtists(Arrays.asList(currentArtist))
                .setArtists(Arrays.asList(currentArtist))
                .setTitle(currentTitle)
                .setTrackNumber(1)
                .setDiscNumber(1)
                .setGenres(Arrays.asList("Rock", "Pop"))
                .build();
    }

    /**
     * Creates metadata for the "next" track
     */
    private Metadata createNextTrackMetadata() throws URISyntaxException {
        // Update instance variables
        currentTitle = "Next Track";
        currentArtist = "Sample Artist";
        currentAlbum = "Sample Album";

        return new Metadata.Builder()
                .setTrackID(new DBusPath("/org/mpris/MediaPlayer2/Track/2"))
                .setLength(240000000) // 4 minutes in microseconds
                .setArtURL(new URI("https://ca.slack-edge.com/T8493FQ76-U01A2EW3XBQ-ee10e32f10e5-512"))
                .setAlbumName(currentAlbum)
                .setAlbumArtists(Arrays.asList(currentArtist))
                .setArtists(Arrays.asList(currentArtist))
                .setTitle(currentTitle)
                .setTrackNumber(2)
                .setDiscNumber(1)
                .setGenres(Arrays.asList("Rock", "Pop"))
                .build();
    }

    /**
     * Creates metadata for the "previous" track
     */
    private Metadata createPreviousTrackMetadata() throws URISyntaxException {
        // Update instance variables
        currentTitle = "Previous Track";
        currentArtist = "Sample Artist";
        currentAlbum = "Sample Album";

        return new Metadata.Builder()
                .setTrackID(new DBusPath("/org/mpris/MediaPlayer2/Track/3"))
                .setLength(200000000) // 3.33 minutes in microseconds
                .setArtURL(new URI("https://example.com/album-art3.jpg"))
                .setAlbumName(currentAlbum)
                .setAlbumArtists(Arrays.asList(currentArtist))
                .setArtists(Arrays.asList(currentArtist))
                .setTitle(currentTitle)
                .setTrackNumber(3)
                .setDiscNumber(1)
                .setGenres(Arrays.asList("Rock", "Pop"))
                .build();
    }
}
