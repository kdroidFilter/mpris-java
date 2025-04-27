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
    private MPRISMediaPlayer mediaPlayer;
    private DBusConnection connection;
    private PlaybackStatus currentPlaybackStatus = PlaybackStatus.STOPPED;

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
        mediaPlayer = new MPRISMediaPlayer(connection, "mprisSwingDemo");

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
                .setOnNext(o -> {
                    System.out.println("Next track");
                    try {
                        Metadata nextMetadata = createNextTrackMetadata();
                        playerBuilder.setMetadata(nextMetadata);
                        mediaPlayer.getMPRISMediaPlayer2None().setMetadata(nextMetadata);
                        updateTrackInfo(nextMetadata);
                    } catch (Exception ex) {
                        System.err.println("Error updating metadata: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                })
                .setOnPrevious(o -> {
                    System.out.println("Previous track");
                    try {
                        Metadata prevMetadata = createPreviousTrackMetadata();
                        playerBuilder.setMetadata(prevMetadata);
                        mediaPlayer.getMPRISMediaPlayer2None().setMetadata(prevMetadata);
                        updateTrackInfo(prevMetadata);
                    } catch (Exception ex) {
                        System.err.println("Error updating metadata: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                })
                .setOnPause(o -> {
                    System.out.println("Pause");
                    try {
                        currentPlaybackStatus = PlaybackStatus.PAUSED;
                        playerBuilder.setPlaybackStatus(PlaybackStatus.PAUSED);
                        mediaPlayer.getMPRISMediaPlayer2None().setPlaybackStatus(PlaybackStatus.PAUSED);
                        SwingUtilities.invokeLater(() -> {
                            if (statusLabel != null) {
                                statusLabel.setText("Status: PAUSED");
                            }
                        });
                        updatePlayPauseButtonText("Play");
                    } catch (Exception ex) {
                        System.err.println("Error updating playback status: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                })
                .setOnPlayPause(o -> {
                    System.out.println("Play/Pause");
                    try {
                        if (currentPlaybackStatus == PlaybackStatus.PLAYING) {
                            // If currently playing, change to paused
                            currentPlaybackStatus = PlaybackStatus.PAUSED;
                            playerBuilder.setPlaybackStatus(PlaybackStatus.PAUSED);
                            mediaPlayer.getMPRISMediaPlayer2None().setPlaybackStatus(PlaybackStatus.PAUSED);
                            SwingUtilities.invokeLater(() -> {
                                if (statusLabel != null) {
                                    statusLabel.setText("Status: PAUSED");
                                }
                            });
                            updatePlayPauseButtonText("Play");
                        } else {
                            // If paused or stopped, change to playing
                            currentPlaybackStatus = PlaybackStatus.PLAYING;
                            playerBuilder.setPlaybackStatus(PlaybackStatus.PLAYING);
                            mediaPlayer.getMPRISMediaPlayer2None().setPlaybackStatus(PlaybackStatus.PLAYING);
                            SwingUtilities.invokeLater(() -> {
                                if (statusLabel != null) {
                                    statusLabel.setText("Status: PLAYING");
                                }
                            });
                            updatePlayPauseButtonText("Pause");
                        }
                    } catch (Exception ex) {
                        System.err.println("Error updating playback status: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                })
                .setOnStop(o -> {
                    System.out.println("Stop");
                    try {
                        currentPlaybackStatus = PlaybackStatus.STOPPED;
                        playerBuilder.setPlaybackStatus(PlaybackStatus.STOPPED);
                        mediaPlayer.getMPRISMediaPlayer2None().setPlaybackStatus(PlaybackStatus.STOPPED);
                        SwingUtilities.invokeLater(() -> {
                            if (statusLabel != null) {
                                statusLabel.setText("Status: STOPPED");
                            }
                        });
                        updatePlayPauseButtonText("Play");
                    } catch (Exception ex) {
                        System.err.println("Error updating playback status: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                })
                .setOnPlay(o -> {
                    System.out.println("Play");
                    try {
                        currentPlaybackStatus = PlaybackStatus.PLAYING;
                        playerBuilder.setPlaybackStatus(PlaybackStatus.PLAYING);
                        mediaPlayer.getMPRISMediaPlayer2None().setPlaybackStatus(PlaybackStatus.PLAYING);
                        SwingUtilities.invokeLater(() -> {
                            if (statusLabel != null) {
                                statusLabel.setText("Status: PLAYING");
                            }
                        });
                        updatePlayPauseButtonText("Pause");
                    } catch (Exception ex) {
                        System.err.println("Error updating playback status: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                })
                .setOnSeek(position -> System.out.println("Seek to " + position))
                .setOnOpenURI(uri -> System.out.println("Open URI: " + uri));

        // Initialize the current playback status
        currentPlaybackStatus = PlaybackStatus.STOPPED;

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

        JButton playPauseButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JButton quitButton = new JButton("Quit");

        // Add action listeners to buttons
        playPauseButton.addActionListener(e -> {
            try {
                if (currentPlaybackStatus == PlaybackStatus.PLAYING) {
                    // If currently playing, change to paused
                    currentPlaybackStatus = PlaybackStatus.PAUSED;
                    playerBuilder.setPlaybackStatus(PlaybackStatus.PAUSED);
                    mediaPlayer.getMPRISMediaPlayer2None().setPlaybackStatus(PlaybackStatus.PAUSED);
                    playPauseButton.setText("Play");
                    statusLabel.setText("Status: PAUSED");
                    System.out.println("Playback paused");
                } else {
                    // If paused or stopped, change to playing
                    currentPlaybackStatus = PlaybackStatus.PLAYING;
                    playerBuilder.setPlaybackStatus(PlaybackStatus.PLAYING);
                    mediaPlayer.getMPRISMediaPlayer2None().setPlaybackStatus(PlaybackStatus.PLAYING);
                    playPauseButton.setText("Pause");
                    statusLabel.setText("Status: PLAYING");
                    System.out.println("Playback started");
                }
            } catch (Exception ex) {
                System.err.println("Error updating playback status: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        stopButton.addActionListener(e -> {
            try {
                currentPlaybackStatus = PlaybackStatus.STOPPED;
                playerBuilder.setPlaybackStatus(PlaybackStatus.STOPPED);
                mediaPlayer.getMPRISMediaPlayer2None().setPlaybackStatus(PlaybackStatus.STOPPED);
                playPauseButton.setText("Play");
                statusLabel.setText("Status: STOPPED");
                System.out.println("Playback stopped");
            } catch (Exception ex) {
                System.err.println("Error updating playback status: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        nextButton.addActionListener(e -> {
            try {
                Metadata nextMetadata = createNextTrackMetadata();
                playerBuilder.setMetadata(nextMetadata);
                mediaPlayer.getMPRISMediaPlayer2None().setMetadata(nextMetadata);
                updateTrackInfo(nextMetadata);
                System.out.println("Changed to next track");
            } catch (Exception ex) {
                System.err.println("Error updating metadata: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        prevButton.addActionListener(e -> {
            try {
                Metadata prevMetadata = createPreviousTrackMetadata();
                playerBuilder.setMetadata(prevMetadata);
                mediaPlayer.getMPRISMediaPlayer2None().setMetadata(prevMetadata);
                updateTrackInfo(prevMetadata);
                System.out.println("Changed to previous track");
            } catch (Exception ex) {
                System.err.println("Error updating metadata: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        quitButton.addActionListener(e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });

        // Add buttons to control panel
        controlPanel.add(playPauseButton);
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
     * Helper method to find and update the play/pause button text
     * @param newText The new text to set on the button
     */
    private void updatePlayPauseButtonText(String newText) {
        if (frame == null) return;

        SwingUtilities.invokeLater(() -> {
            // Search through all components to find the play/pause button
            for (Component comp : frame.getContentPane().getComponents()) {
                if (comp instanceof JPanel) {
                    JPanel panel = (JPanel) comp;
                    findAndUpdateButton(panel, newText);
                }
            }
        });
    }

    /**
     * Recursively search for the play/pause button in a container
     * @param container The container to search in
     * @param newText The new text to set on the button
     */
    private void findAndUpdateButton(Container container, String newText) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String buttonText = button.getText();
                if ("Play".equals(buttonText) || "Pause".equals(buttonText)) {
                    button.setText(newText);
                    return; // Found and updated the button, so return
                }
            } else if (comp instanceof Container) {
                // Recursively search in nested containers
                findAndUpdateButton((Container) comp, newText);
            }
        }
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
                .setArtURL(new URI("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/Apple_Music_icon.svg/2048px-Apple_Music_icon.svg.png"))
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
        currentTitle = "Next Track 2";
        currentArtist = "Sample Artist";
        currentAlbum = "Sample Album";

        return new Metadata.Builder()
                .setTrackID(new DBusPath("/org/mpris/MediaPlayer2/Track/2"))
                .setLength(240000000) // 4 minutes in microseconds
                .setArtURL(new URI("https://png.pngtree.com/png-clipart/20190921/original/pngtree-music-icon-png-image_4694506.jpg"))
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
                .setArtURL(new URI("https://images.vexels.com/media/users/3/131548/isolated/svg/9e36529b6e31cc4bae564fc2d14a8d0f.svg"))
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
