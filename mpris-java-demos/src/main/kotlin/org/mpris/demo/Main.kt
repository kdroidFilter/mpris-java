package org.mpris.demo

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.system.exitProcess
import kotlinx.coroutines.launch
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.mpris.MPRISMediaPlayer
import org.mpris.Metadata
import org.mpris.mpris.LoopStatus
import org.mpris.mpris.PlaybackStatus
import java.net.URI
import kotlin.system.exitProcess

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        alwaysOnTop = true,
        title = "MPRIS Compose Demo"
    ) {
        App()
    }
}

@Composable
@Preview
fun App() {
    val scope = rememberCoroutineScope()

    // State variables
    var playbackStatus by remember { mutableStateOf(PlaybackStatus.STOPPED) }
    var currentTitle by remember { mutableStateOf("Unknown") }
    var currentArtist by remember { mutableStateOf("Unknown") }
    var currentAlbum by remember { mutableStateOf("Unknown") }

    // Media player references
    val mediaPlayerState = remember { mutableStateOf<MPRISMediaPlayer?>(null) }
    val playerBuilderState = remember { mutableStateOf<MPRISMediaPlayer.PlayerBuilder?>(null) }
    val connectionState = remember { mutableStateOf<DBusConnection?>(null) }

    // Initialize the media player
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                // Create a DBus connection (SESSION bus is used for desktop applications)
                val connection = DBusConnection.newConnection(DBusConnection.DBusBusType.SESSION)
                connectionState.value = connection

                // Create a media player with a unique name
                val mediaPlayer = MPRISMediaPlayer(connection, "mprisComposeDemo")

                // Create metadata for a track and update state variables
                val metadata = createSampleTrackMetadata()
                currentTitle = "Sample Track"
                currentArtist = "Sample Artist"
                currentAlbum = "Sample Album"

                // Create a media player builder with basic properties
                val mediaPlayer2Builder = MPRISMediaPlayer.MediaPlayer2Builder()
                    .setCanQuit(true)
                    .setCanRaise(true)
                    .setIdentity("MPRIS Compose Demo")
                    .setDesktopEntry("mpris-compose-demo.desktop")
                    .setSupportedUriSchemes("file", "http", "https")
                    .setSupportedMimeTypes("audio/mpeg", "audio/flac", "audio/ogg")
                    .setOnRaise { println("Player raised") }
                    .setOnQuit { exitProcess(0) }

                // Create a player builder with track metadata and playback controls
                val playerBuilder = MPRISMediaPlayer.PlayerBuilder()
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
                    .setOnNext {
                        println("Next track")
                        try {
                            val nextMetadata = createNextTrackMetadata()
                            playerBuilderState.value?.setMetadata(nextMetadata)
                            mediaPlayer.mprisMediaPlayer2None.setMetadata(nextMetadata)
                            currentTitle = "Next Track 2"
                            currentArtist = "Sample Artist"
                            currentAlbum = "Sample Album"
                        } catch (ex: Exception) {
                            println("Error updating metadata: ${ex.message}")
                            ex.printStackTrace()
                        }
                    }
                    .setOnPrevious {
                        println("Previous track")
                        try {
                            val prevMetadata = createPreviousTrackMetadata()
                            playerBuilderState.value?.setMetadata(prevMetadata)
                            mediaPlayer.mprisMediaPlayer2None.setMetadata(prevMetadata)
                            currentTitle = "Previous Track"
                            currentArtist = "Sample Artist"
                            currentAlbum = "Sample Album"
                        } catch (ex: Exception) {
                            println("Error updating metadata: ${ex.message}")
                            ex.printStackTrace()
                        }
                    }
                    .setOnPause {
                        println("Pause")
                        try {
                            playbackStatus = PlaybackStatus.PAUSED
                            playerBuilderState.value?.setPlaybackStatus(PlaybackStatus.PAUSED)
                            mediaPlayer.mprisMediaPlayer2None.setPlaybackStatus(PlaybackStatus.PAUSED)
                        } catch (ex: Exception) {
                            println("Error updating playback status: ${ex.message}")
                            ex.printStackTrace()
                        }
                    }
                    .setOnPlayPause {
                        println("Play/Pause")
                        try {
                            if (playbackStatus == PlaybackStatus.PLAYING) {
                                // If currently playing, change to paused
                                playbackStatus = PlaybackStatus.PAUSED
                                playerBuilderState.value?.setPlaybackStatus(PlaybackStatus.PAUSED)
                                mediaPlayer.mprisMediaPlayer2None.setPlaybackStatus(PlaybackStatus.PAUSED)
                            } else {
                                // If paused or stopped, change to playing
                                playbackStatus = PlaybackStatus.PLAYING
                                playerBuilderState.value?.setPlaybackStatus(PlaybackStatus.PLAYING)
                                mediaPlayer.mprisMediaPlayer2None.setPlaybackStatus(PlaybackStatus.PLAYING)
                            }
                        } catch (ex: Exception) {
                            println("Error updating playback status: ${ex.message}")
                            ex.printStackTrace()
                        }
                    }
                    .setOnStop {
                        println("Stop")
                        try {
                            playbackStatus = PlaybackStatus.STOPPED
                            playerBuilderState.value?.setPlaybackStatus(PlaybackStatus.STOPPED)
                            mediaPlayer.mprisMediaPlayer2None.setPlaybackStatus(PlaybackStatus.STOPPED)
                        } catch (ex: Exception) {
                            println("Error updating playback status: ${ex.message}")
                            ex.printStackTrace()
                        }
                    }
                    .setOnPlay {
                        println("Play")
                        try {
                            playbackStatus = PlaybackStatus.PLAYING
                            playerBuilderState.value?.setPlaybackStatus(PlaybackStatus.PLAYING)
                            mediaPlayer.mprisMediaPlayer2None.setPlaybackStatus(PlaybackStatus.PLAYING)
                        } catch (ex: Exception) {
                            println("Error updating playback status: ${ex.message}")
                            ex.printStackTrace()
                        }
                    }
                    .setOnSeek { position -> println("Seek to $position") }
                    .setOnOpenURI { uri -> println("Open URI: $uri") }

                // Build the media player with the minimum required interfaces (MediaPlayer2 and Player)
                mediaPlayer.buildMPRISMediaPlayer2None(mediaPlayer2Builder, playerBuilder)

                // Create the media player on the D-Bus
                mediaPlayer.create()

                // Store references for later use
                mediaPlayerState.value = mediaPlayer
                playerBuilderState.value = playerBuilder

            } catch (e: Exception) {
                println("Error initializing media player: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Cleanup when the window is closed
    DisposableEffect(Unit) {
        onDispose {
            try {
                connectionState.value?.close()
                println("Demo application closed.")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Track info panel
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Status: $playbackStatus")
                        Spacer(Modifier.height(8.dp))
                        Text("Track: $currentTitle")
                        Spacer(Modifier.height(8.dp))
                        Text("Artist: $currentArtist")
                        Spacer(Modifier.height(8.dp))
                        Text("Album: $currentAlbum")
                    }
                }

                // Control buttons panel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            try {
                                val mediaPlayer = mediaPlayerState.value
                                val playerBuilder = playerBuilderState.value

                                if (mediaPlayer != null && playerBuilder != null) {
                                    if (playbackStatus == PlaybackStatus.PLAYING) {
                                        // If currently playing, change to paused
                                        playbackStatus = PlaybackStatus.PAUSED
                                        playerBuilder.setPlaybackStatus(PlaybackStatus.PAUSED)
                                        mediaPlayer.mprisMediaPlayer2None.setPlaybackStatus(PlaybackStatus.PAUSED)
                                        println("Playback paused")
                                    } else {
                                        // If paused or stopped, change to playing
                                        playbackStatus = PlaybackStatus.PLAYING
                                        playerBuilder.setPlaybackStatus(PlaybackStatus.PLAYING)
                                        mediaPlayer.mprisMediaPlayer2None.setPlaybackStatus(PlaybackStatus.PLAYING)
                                        println("Playback started")
                                    }
                                }
                            } catch (ex: Exception) {
                                println("Error updating playback status: ${ex.message}")
                                ex.printStackTrace()
                            }
                        }
                    ) {
                        Text(if (playbackStatus == PlaybackStatus.PLAYING) "Pause" else "Play")
                    }

                    Button(
                        onClick = {
                            try {
                                val mediaPlayer = mediaPlayerState.value
                                val playerBuilder = playerBuilderState.value

                                if (mediaPlayer != null && playerBuilder != null) {
                                    playbackStatus = PlaybackStatus.STOPPED
                                    playerBuilder.setPlaybackStatus(PlaybackStatus.STOPPED)
                                    mediaPlayer.mprisMediaPlayer2None.setPlaybackStatus(PlaybackStatus.STOPPED)
                                    println("Playback stopped")
                                }
                            } catch (ex: Exception) {
                                println("Error updating playback status: ${ex.message}")
                                ex.printStackTrace()
                            }
                        }
                    ) {
                        Text("Stop")
                    }

                    Button(
                        onClick = {
                            try {
                                val mediaPlayer = mediaPlayerState.value
                                val playerBuilder = playerBuilderState.value

                                if (mediaPlayer != null && playerBuilder != null) {
                                    val prevMetadata = createPreviousTrackMetadata()
                                    playerBuilder.setMetadata(prevMetadata)
                                    mediaPlayer.mprisMediaPlayer2None.setMetadata(prevMetadata)
                                    currentTitle = "Previous Track"
                                    currentArtist = "Sample Artist"
                                    currentAlbum = "Sample Album"
                                    println("Changed to previous track")
                                }
                            } catch (ex: Exception) {
                                println("Error updating metadata: ${ex.message}")
                                ex.printStackTrace()
                            }
                        }
                    ) {
                        Text("Previous")
                    }

                    Button(
                        onClick = {
                            try {
                                val mediaPlayer = mediaPlayerState.value
                                val playerBuilder = playerBuilderState.value

                                if (mediaPlayer != null && playerBuilder != null) {
                                    val nextMetadata = createNextTrackMetadata()
                                    playerBuilder.setMetadata(nextMetadata)
                                    mediaPlayer.mprisMediaPlayer2None.setMetadata(nextMetadata)
                                    currentTitle = "Next Track 2"
                                    currentArtist = "Sample Artist"
                                    currentAlbum = "Sample Album"
                                    println("Changed to next track")
                                }
                            } catch (ex: Exception) {
                                println("Error updating metadata: ${ex.message}")
                                ex.printStackTrace()
                            }
                        }
                    ) {
                        Text("Next")
                    }

                    Button(
                        onClick = {
                            exitProcess(0)
                        }
                    ) {
                        Text("Quit")
                    }
                }
            }
        }
    }
}

/**
 * Creates sample metadata for a track
 */
private fun createSampleTrackMetadata(): Metadata {
    return Metadata.Builder()
        .setTrackID(DBusPath("/org/mpris/MediaPlayer2/Track/1"))
        .setLength(180000000) // 3 minutes in microseconds
        .setArtURL(URI("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/Apple_Music_icon.svg/2048px-Apple_Music_icon.svg.png"))
        .setAlbumName("Sample Album")
        .setAlbumArtists(listOf("Sample Artist"))
        .setArtists(listOf("Sample Artist"))
        .setTitle("Sample Track")
        .setTrackNumber(1)
        .setDiscNumber(1)
        .setGenres(listOf("Rock", "Pop"))
        .build()
}

/**
 * Creates metadata for the "next" track
 */
private fun createNextTrackMetadata(): Metadata {
    return Metadata.Builder()
        .setTrackID(DBusPath("/org/mpris/MediaPlayer2/Track/2"))
        .setLength(240000000) // 4 minutes in microseconds
        .setArtURL(URI("https://png.pngtree.com/png-clipart/20190921/original/pngtree-music-icon-png-image_4694506.jpg"))
        .setAlbumName("Sample Album")
        .setAlbumArtists(listOf("Sample Artist"))
        .setArtists(listOf("Sample Artist"))
        .setTitle("Next Track 2")
        .setTrackNumber(2)
        .setDiscNumber(1)
        .setGenres(listOf("Rock", "Pop"))
        .build()
}

/**
 * Creates metadata for the "previous" track
 */
private fun createPreviousTrackMetadata(): Metadata {
    return Metadata.Builder()
        .setTrackID(DBusPath("/org/mpris/MediaPlayer2/Track/3"))
        .setLength(200000000) // 3.33 minutes in microseconds
        .setArtURL(URI("https://images.vexels.com/media/users/3/131548/isolated/svg/9e36529b6e31cc4bae564fc2d14a8d0f.svg"))
        .setAlbumName("Sample Album")
        .setAlbumArtists(listOf("Sample Artist"))
        .setArtists(listOf("Sample Artist"))
        .setTitle("Previous Track")
        .setTrackNumber(3)
        .setDiscNumber(1)
        .setGenres(listOf("Rock", "Pop"))
        .build()
}
