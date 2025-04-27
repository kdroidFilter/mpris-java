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
import org.mpris.*
import org.mpris.mpris.PlaybackStatus

/**
 * A simplified demo that showcases the enhanced DSL
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        alwaysOnTop = true,
        title = "MPRIS Simplified Demo"
    ) {
        SimplifiedApp()
    }
}

@Composable
@Preview
fun SimplifiedApp() {
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
                // Create a simple media player with sensible defaults
                val mediaPlayer = simpleMediaPlayer("mprisSimplifiedDemo", "MPRIS Simplified Demo") {
                    // Configure MediaPlayer2 interface with custom settings
                    mediaPlayer2 {
                        desktopEntry = "mpris-simplified-demo.desktop"
                        onRaise = { println("Player raised") }
                        onQuit = { exitProcess(0) }
                    }

                    // Configure Player interface with custom settings
                    player {
                        // Create metadata for the initial track
                        metadata {
                            trackId(org.freedesktop.dbus.DBusPath("/org/mpris/MediaPlayer2/Track/1"))
                            title("Initial Track")
                            artists("Demo Artist")
                            albumName("Demo Album")
                            trackNumber(1)
                            length(180000000) // 3 minutes in microseconds
                        }

                        // Set up basic event handlers
                        onNext = { println("Next track") }
                        onPrevious = { println("Previous track") }
                        onPlay = { println("Play") }
                        onPause = { println("Pause") }
                        onStop = { println("Stop") }
                        onPlayPause = { println("Play/Pause") }
                    }
                }

                // Initialize the player with the initial track
                mediaPlayer.updateTrack(
                    id = "/org/mpris/MediaPlayer2/Track/1",
                    title = "Initial Track",
                    artist = "Demo Artist",
                    album = "Demo Album",
                    artUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/Apple_Music_icon.svg/2048px-Apple_Music_icon.svg.png"
                )

                // Store reference and update UI
                mediaPlayerState.value = mediaPlayer
                currentTitle = "Initial Track"
                currentArtist = "Demo Artist"
                currentAlbum = "Demo Album"

            } catch (e: Exception) {
                println("Error initializing media player: ${e.message}")
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
                            val mediaPlayer = mediaPlayerState.value
                            if (mediaPlayer != null) {
                                if (playbackStatus == PlaybackStatus.PLAYING) {
                                    mediaPlayer.pause()
                                    playbackStatus = PlaybackStatus.PAUSED
                                } else {
                                    mediaPlayer.play()
                                    playbackStatus = PlaybackStatus.PLAYING
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
                                mediaPlayer.stop()
                                playbackStatus = PlaybackStatus.STOPPED
                            }
                        }
                    ) {
                        Text("Stop")
                    }

                    Button(
                        onClick = {
                            val mediaPlayer = mediaPlayerState.value
                            if (mediaPlayer != null) {
                                mediaPlayer.updateTrack(
                                    id = "/org/mpris/MediaPlayer2/Track/2",
                                    title = "Previous Track",
                                    artist = "Previous Artist",
                                    album = "Previous Album",
                                    artUrl = "https://images.vexels.com/media/users/3/131548/isolated/svg/9e36529b6e31cc4bae564fc2d14a8d0f.svg"
                                )
                                currentTitle = "Previous Track"
                                currentArtist = "Previous Artist"
                                currentAlbum = "Previous Album"
                            }
                        }
                    ) {
                        Text("Previous")
                    }

                    Button(
                        onClick = {
                            val mediaPlayer = mediaPlayerState.value
                            if (mediaPlayer != null) {
                                mediaPlayer.playTrack(
                                    id = "/org/mpris/MediaPlayer2/Track/3",
                                    title = "Next Track",
                                    artist = "Next Artist",
                                    album = "Next Album",
                                    artUrl = "https://png.pngtree.com/png-clipart/20190921/original/pngtree-music-icon-png-image_4694506.jpg"
                                )
                                currentTitle = "Next Track"
                                currentArtist = "Next Artist"
                                currentAlbum = "Next Album"
                                playbackStatus = PlaybackStatus.PLAYING
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
