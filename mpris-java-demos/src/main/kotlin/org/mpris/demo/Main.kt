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
import org.mpris.*
import org.mpris.mpris.LoopStatus
import org.mpris.mpris.PlaybackStatus
import java.net.URI

// Helper function to execute code with error handling
private inline fun withErrorHandling(
    errorMessage: String,
    action: () -> Unit
) {
    try {
        action()
    } catch (ex: Exception) {
        println("$errorMessage: ${ex.message}")
        ex.printStackTrace()
    }
}

// Helper function to update playback status
private fun updatePlaybackStatus(
    mediaPlayer: MPRISMediaPlayer?,
    newStatus: PlaybackStatus,
    updateState: (PlaybackStatus) -> Unit
) {
    withErrorHandling("Error updating playback status") {
        mediaPlayer?.setPlaybackStatus(newStatus)
        updateState(newStatus)
        println("Playback status changed to $newStatus")
    }
}

// Helper function to update track metadata
private fun updateTrackMetadata(
    mediaPlayer: MPRISMediaPlayer?,
    metadata: Metadata,
    title: String,
    artist: String = "Sample Artist",
    album: String = "Sample Album",
    updateTitle: (String) -> Unit,
    updateArtist: (String) -> Unit,
    updateAlbum: (String) -> Unit
) {
    withErrorHandling("Error updating metadata") {
        mediaPlayer?.setMetadata(metadata)
        updateTitle(title)
        updateArtist(artist)
        updateAlbum(album)
        println("Changed track to $title")
    }
}

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

    // Media player reference
    val mediaPlayerState = remember { mutableStateOf<MPRISMediaPlayer?>(null) }

    // Initialize the media player
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                // Create metadata for a track and update state variables
                currentTitle = "Sample Track"
                currentArtist = "Sample Artist"
                currentAlbum = "Sample Album"

                // Create a media player with the new Kotlin API
                val mediaPlayer = createMediaPlayer("mprisComposeDemo") {
                    // Configure MediaPlayer2 interface
                    mediaPlayer2 {
                        canQuit = true
                        canRaise = true
                        identity = "MPRIS Compose Demo"
                        desktopEntry = "mpris-compose-demo.desktop"
                        supportedUriSchemes = listOf("file", "http", "https")
                        supportedMimeTypes = listOf("audio/mpeg", "audio/flac", "audio/ogg")
                        onRaise = { println("Player raised") }
                        onQuit = { exitProcess(0) }
                    }

                    // Configure Player interface
                    player {
                        playbackStatus = PlaybackStatus.STOPPED
                        loopStatus = LoopStatus.NONE
                        shuffle = true
                        volume = 1.0
                        position = 0
                        canGoNext = true
                        canGoPrevious = true
                        canPlay = true
                        canPause = true
                        canSeek = true
                        canControl = true

                        // Create metadata for the track
                        metadata {
                            trackId(DBusPath("/org/mpris/MediaPlayer2/Track/1"))
                            length(180000000) // 3 minutes in microseconds
                            artUrl(URI("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/Apple_Music_icon.svg/2048px-Apple_Music_icon.svg.png"))
                            albumName("Sample Album")
                            albumArtists("Sample Artist")
                            artists("Sample Artist")
                            title("Sample Track")
                            trackNumber(1)
                            discNumber(1)
                            genres("Rock", "Pop")
                        }

                        // Event handlers
                        onNext = {
                            println("Next track")
                            val nextMetadata = createNextTrackMetadata()
                            updateTrackMetadata(
                                mediaPlayer = mediaPlayerState.value,
                                metadata = nextMetadata,
                                title = "Next Track 2",
                                updateTitle = { currentTitle = it },
                                updateArtist = { currentArtist = it },
                                updateAlbum = { currentAlbum = it }
                            )
                        }

                        onPrevious = {
                            println("Previous track")
                            val prevMetadata = createPreviousTrackMetadata()
                            updateTrackMetadata(
                                mediaPlayer = mediaPlayerState.value,
                                metadata = prevMetadata,
                                title = "Previous Track",
                                updateTitle = { currentTitle = it },
                                updateArtist = { currentArtist = it },
                                updateAlbum = { currentAlbum = it }
                            )
                        }

                        onPause = {
                            println("Pause")
                            updatePlaybackStatus(
                                mediaPlayer = mediaPlayerState.value,
                                newStatus = PlaybackStatus.PAUSED
                            ) { playbackStatus = it }
                        }

                        onPlayPause = {
                            println("Play/Pause")
                            val newStatus = if (playbackStatus == PlaybackStatus.PLAYING) {
                                PlaybackStatus.PAUSED
                            } else {
                                PlaybackStatus.PLAYING
                            }
                            updatePlaybackStatus(
                                mediaPlayer = mediaPlayerState.value,
                                newStatus = newStatus
                            ) { playbackStatus = it }
                        }

                        onStop = {
                            println("Stop")
                            updatePlaybackStatus(
                                mediaPlayer = mediaPlayerState.value,
                                newStatus = PlaybackStatus.STOPPED
                            ) { playbackStatus = it }
                        }

                        onPlay = {
                            println("Play")
                            updatePlaybackStatus(
                                mediaPlayer = mediaPlayerState.value,
                                newStatus = PlaybackStatus.PLAYING
                            ) { playbackStatus = it }
                        }

                        onSeek = { position -> println("Seek to $position") }
                        onOpenURI = { uri -> println("Open URI: $uri") }
                    }
                }

                // Store reference for later use
                mediaPlayerState.value = mediaPlayer

            } catch (e: Exception) {
                println("Error initializing media player: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Cleanup when the window is closed
    DisposableEffect(Unit) {
        onDispose {
            println("Demo application closed.")
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
                            val mediaPlayer = mediaPlayerState.value
                            if (mediaPlayer != null) {
                                if (playbackStatus == PlaybackStatus.PLAYING) {
                                    // If currently playing, change to paused
                                    updatePlaybackStatus(mediaPlayer, PlaybackStatus.PAUSED) { playbackStatus = it }
                                } else {
                                    // If paused or stopped, change to playing
                                    updatePlaybackStatus(mediaPlayer, PlaybackStatus.PLAYING) { playbackStatus = it }
                                }
                            }
                        }
                    ) {
                        Text(if (playbackStatus == PlaybackStatus.PLAYING) "Pause" else "Play")
                    }

                    Button(
                        onClick = {
                            val mediaPlayer = mediaPlayerState.value
                            if (mediaPlayer != null) {
                                updatePlaybackStatus(mediaPlayer, PlaybackStatus.STOPPED) { playbackStatus = it }
                            }
                        }
                    ) {
                        Text("Stop")
                    }

                    Button(
                        onClick = {
                            val mediaPlayer = mediaPlayerState.value
                            if (mediaPlayer != null) {
                                val prevMetadata = createPreviousTrackMetadata()
                                updateTrackMetadata(
                                    mediaPlayer = mediaPlayer,
                                    metadata = prevMetadata,
                                    title = "Previous Track",
                                    updateTitle = { currentTitle = it },
                                    updateArtist = { currentArtist = it },
                                    updateAlbum = { currentAlbum = it }
                                )
                            }
                        }
                    ) {
                        Text("Previous")
                    }

                    Button(
                        onClick = {
                            val mediaPlayer = mediaPlayerState.value
                            if (mediaPlayer != null) {
                                val nextMetadata = createNextTrackMetadata()
                                updateTrackMetadata(
                                    mediaPlayer = mediaPlayer,
                                    metadata = nextMetadata,
                                    title = "Next Track 2",
                                    updateTitle = { currentTitle = it },
                                    updateArtist = { currentArtist = it },
                                    updateAlbum = { currentAlbum = it }
                                )
                            }
                        }
                    ) {
                        Text("Next")
                    }

                    Button(
                        onClick = { exitProcess(0) }
                    ) {
                        Text("Quit")
                    }
                }
            }
        }
    }
}

/**
 * Creates metadata for a track using the builder pattern
 */
private fun createTrackMetadata(
    trackId: String,
    title: String,
    trackNumber: Int,
    length: Int = 180000000, // Default: 3 minutes in microseconds
    artUrl: String = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/Apple_Music_icon.svg/2048px-Apple_Music_icon.svg.png",
    album: String = "Sample Album",
    artist: String = "Sample Artist",
    discNumber: Int = 1,
    genres: List<String> = listOf("Rock", "Pop")
): Metadata {
    return Metadata.Builder().apply {
        setTrackID(DBusPath(trackId))
        setLength(length)
        setArtURL(URI(artUrl))
        setAlbumName(album)
        setAlbumArtists(listOf(artist))
        setArtists(listOf(artist))
        setTitle(title)
        setTrackNumber(trackNumber)
        setDiscNumber(discNumber)
        setGenres(genres)
    }.build()
}

/**
 * Creates sample metadata for a track
 */
private fun createSampleTrackMetadata(): Metadata {
    return createTrackMetadata(
        trackId = "/org/mpris/MediaPlayer2/Track/1",
        title = "Sample Track",
        trackNumber = 1
    )
}

/**
 * Creates metadata for the "next" track
 */
private fun createNextTrackMetadata(): Metadata {
    return createTrackMetadata(
        trackId = "/org/mpris/MediaPlayer2/Track/2",
        title = "Next Track 2",
        trackNumber = 2,
        length = 240000000, // 4 minutes in microseconds
        artUrl = "https://png.pngtree.com/png-clipart/20190921/original/pngtree-music-icon-png-image_4694506.jpg"
    )
}

/**
 * Creates metadata for the "previous" track
 */
private fun createPreviousTrackMetadata(): Metadata {
    return createTrackMetadata(
        trackId = "/org/mpris/MediaPlayer2/Track/3",
        title = "Previous Track",
        trackNumber = 3,
        length = 200000000, // 3.33 minutes in microseconds
        artUrl = "https://images.vexels.com/media/users/3/131548/isolated/svg/9e36529b6e31cc4bae564fc2d14a8d0f.svg"
    )
}
