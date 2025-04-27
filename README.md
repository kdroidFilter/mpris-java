# MPRIS Java

A Java/Kotlin library for implementing the [MPRIS D-Bus Interface Specification](https://specifications.freedesktop.org/mpris-spec/latest/) in media players.

## Features

- Full implementation of the MPRIS D-Bus Interface Specification
- Support for all MPRIS interfaces: MediaPlayer2, Player, TrackList, and Playlists
- Kotlin DSL for easy configuration
- Java API for use in Java applications
- Demo applications showcasing usage

## Getting Started

### Kotlin (Recommended)

The library provides a Kotlin DSL for easy configuration of MPRIS media players.

#### Basic Usage

```kotlin
// Create a simple media player with sensible defaults
val mediaPlayer = simpleMediaPlayer("myPlayerName", "My Media Player") {
    // Optional custom configuration
    mediaPlayer2 {
        desktopEntry = "my-player.desktop"
        onRaise = { println("Player raised") }
        onQuit = { exitProcess(0) }
    }
    
    player {
        // Configure initial track
        metadata {
            trackId(DBusPath("/org/mpris/MediaPlayer2/Track/1"))
            title("My Track")
            artists("My Artist")
            albumName("My Album")
        }
    }
}

// Update track metadata
mediaPlayer.updateTrack(
    id = "/org/mpris/MediaPlayer2/Track/2",
    title = "New Track",
    artist = "New Artist",
    album = "New Album"
)

// Control playback
mediaPlayer.play()
mediaPlayer.pause()
mediaPlayer.stop()
mediaPlayer.next()
mediaPlayer.previous()
```

#### Advanced Usage

For more advanced use cases, you can use the full DSL:

```kotlin
val mediaPlayer = createMediaPlayer("myPlayerName") {
    // Configure MediaPlayer2 interface
    mediaPlayer2 {
        canQuit = true
        canRaise = true
        identity = "My Media Player"
        desktopEntry = "my-player.desktop"
        supportedUriSchemes = listOf("file", "http", "https")
        supportedMimeTypes = listOf("audio/mpeg", "audio/flac")
        onRaise = { println("Player raised") }
        onQuit = { exitProcess(0) }
    }

    // Configure Player interface
    player {
        playbackStatus = PlaybackStatus.STOPPED
        loopStatus = LoopStatus.NONE
        shuffle = false
        volume = 1.0
        canGoNext = true
        canGoPrevious = true
        canPlay = true
        canPause = true
        canSeek = true
        canControl = true
        
        // Configure metadata
        metadata {
            trackId(DBusPath("/org/mpris/MediaPlayer2/Track/1"))
            length(180000000) // 3 minutes in microseconds
            artUrl(URI("https://example.com/album-art.jpg"))
            albumName("My Album")
            albumArtists("My Artist")
            artists("My Artist")
            title("My Track")
            trackNumber(1)
            discNumber(1)
            genres("Rock", "Pop")
        }
        
        // Configure event handlers
        onNext = { /* Handle next track */ }
        onPrevious = { /* Handle previous track */ }
        onPause = { /* Handle pause */ }
        onPlay = { /* Handle play */ }
        onStop = { /* Handle stop */ }
        onPlayPause = { /* Handle play/pause toggle */ }
        onSeek = { position -> /* Handle seek */ }
        onOpenURI = { uri -> /* Handle open URI */ }
    }
    
    // Optional: Configure TrackList interface
    trackList {
        tracks = listOf(DBusPath("/org/mpris/MediaPlayer2/Track/1"))
        canEditTracks = false
        onGetTracksMetadata = { tracks -> /* Return metadata for tracks */ emptyList() }
        onAddTrack = { /* Handle add track */ }
        onRemoveTrack = { /* Handle remove track */ }
        onGoTo = { /* Handle go to track */ }
    }
    
    // Optional: Configure Playlists interface
    playlists {
        playlistsCount = 0
        orderings = listOf(PlaylistOrdering.ALPHABETICAL)
        // activePlaylist must be set
        activePlaylist = Maybe_Playlist(false, Playlist(DBusPath("/"), "", ""))
        onActivatePlaylist = { /* Handle activate playlist */ }
        onGetPlaylists = { /* Return playlists */ emptyList() }
    }
}
```

### Java

The library also provides a Java API for use in Java applications.

```java
// Create a media player
MPRISMediaPlayer mediaPlayer = new MPRISMediaPlayer(
    DBusConnection.newConnection(DBusConnection.DBusBusType.SESSION),
    "myPlayerName"
);

// Configure the media player
MPRISMediaPlayer.MediaPlayer2Builder mediaPlayer2Builder = new MPRISMediaPlayer.MediaPlayer2Builder()
    .setCanQuit(true)
    .setCanRaise(true)
    .setIdentity("My Media Player")
    .setDesktopEntry("my-player.desktop")
    .setSupportedUriSchemes("file", "http", "https")
    .setSupportedMimeTypes("audio/mpeg", "audio/flac")
    .setOnRaise(() -> System.out.println("Player raised"))
    .setOnQuit(() -> System.exit(0));

// Configure the player
Metadata metadata = new Metadata.Builder()
    .setTrackID(new DBusPath("/org/mpris/MediaPlayer2/Track/1"))
    .setLength(180000000) // 3 minutes in microseconds
    .setArtURL(URI.create("https://example.com/album-art.jpg"))
    .setAlbumName("My Album")
    .setAlbumArtists(List.of("My Artist"))
    .setArtists(List.of("My Artist"))
    .setTitle("My Track")
    .setTrackNumber(1)
    .setDiscNumber(1)
    .setGenres(List.of("Rock", "Pop"))
    .build();

MPRISMediaPlayer.PlayerBuilder playerBuilder = new MPRISMediaPlayer.PlayerBuilder()
    .setPlaybackStatus(PlaybackStatus.STOPPED)
    .setLoopStatus(LoopStatus.NONE)
    .setShuffle(false)
    .setVolume(1.0)
    .setMetadata(metadata)
    .setCanGoNext(true)
    .setCanGoPrevious(true)
    .setCanPlay(true)
    .setCanPause(true)
    .setCanSeek(true)
    .setCanControl(true)
    .setOnNext(() -> { /* Handle next track */ })
    .setOnPrevious(() -> { /* Handle previous track */ })
    .setOnPause(() -> { /* Handle pause */ })
    .setOnPlay(() -> { /* Handle play */ })
    .setOnStop(() -> { /* Handle stop */ })
    .setOnPlayPause(() -> { /* Handle play/pause toggle */ });

// Build the media player
mediaPlayer.buildMPRISMediaPlayer2None(mediaPlayer2Builder, playerBuilder);

// Create the media player on the D-Bus
mediaPlayer.create();
```

## Examples

See the `mpris-java-demos` directory for example applications.

## License

This project is licensed under the MIT License - see the LICENSE file for details.