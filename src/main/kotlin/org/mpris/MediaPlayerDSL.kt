package org.mpris

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.mpris.mpris.LoopStatus
import org.mpris.mpris.PlaybackStatus
import java.net.URI

/**
 * DSL for creating and configuring a media player
 */

/**
 * Creates a simple media player with sensible defaults
 *
 * @param name The name of the player
 * @param identity The identity of the player (defaults to name)
 * @param block Optional configuration block
 * @return The created MPRISMediaPlayer instance
 */
fun simpleMediaPlayer(
    name: String,
    identity: String = name,
    block: (MediaPlayerConfig.() -> Unit)? = null
): MPRISMediaPlayer {
    return createMediaPlayer(name) {
        // Configure MediaPlayer2 interface with sensible defaults
        mediaPlayer2 {
            canQuit = true
            canRaise = true
            this.identity = identity
            supportedUriSchemes = listOf("file", "http", "https")
            supportedMimeTypes = listOf("audio/mpeg", "audio/flac", "audio/ogg")
        }

        // Configure Player interface with sensible defaults
        player {
            playbackStatus = PlaybackStatus.STOPPED
            loopStatus = LoopStatus.NONE
            shuffle = false
            volume = 1.0
            position = 0
            canGoNext = true
            canGoPrevious = true
            canPlay = true
            canPause = true
            canSeek = true
            canControl = true
        }

        // Apply custom configuration if provided
        block?.invoke(this)
    }
}

/**
 * Creates a track with the given parameters
 *
 * @param id The track ID
 * @param title The track title
 * @param artist The track artist
 * @param album The track album
 * @param trackNumber The track number
 * @param length The track length in microseconds
 * @param artUrl The track art URL
 * @param block Optional additional configuration
 * @return The created Metadata
 */
fun track(
    id: String,
    title: String,
    artist: String,
    album: String = "",
    trackNumber: Int = 1,
    length: Int = 180000000, // 3 minutes in microseconds
    artUrl: String? = null,
    block: (MetadataBuilder.() -> Unit)? = null
): Metadata {
    return MetadataBuilder().apply {
        trackId(DBusPath(id))
        this.title(title)
        artists(artist)
        if (album.isNotEmpty()) {
            albumName(album)
            albumArtists(artist)
        }
        this.trackNumber(trackNumber)
        length(length)
        if (artUrl != null) {
            artUrl(URI(artUrl))
        }
        
        // Apply additional configuration if provided
        block?.invoke(this)
    }.build()
}

/**
 * Extension function to update the current track
 */
fun MPRISMediaPlayer.updateTrack(
    id: String,
    title: String,
    artist: String,
    album: String = "",
    trackNumber: Int = 1,
    length: Int = 180000000,
    artUrl: String? = null,
    block: (MetadataBuilder.() -> Unit)? = null
) {
    val metadata = track(id, title, artist, album, trackNumber, length, artUrl, block)
    setMetadata(metadata)
}

/**
 * Extension function to play a track
 */
fun MPRISMediaPlayer.playTrack(
    id: String,
    title: String,
    artist: String,
    album: String = "",
    trackNumber: Int = 1,
    length: Int = 180000000,
    artUrl: String? = null,
    block: (MetadataBuilder.() -> Unit)? = null
) {
    updateTrack(id, title, artist, album, trackNumber, length, artUrl, block)
    setPlaybackStatus(PlaybackStatus.PLAYING)
}

/**
 * Extension function to configure common event handlers
 */
fun PlayerConfig.setupCommonHandlers(
    mediaPlayer: MPRISMediaPlayer,
    onTrackChange: ((String, String, String) -> Unit)? = null
) {
    onNext = {
        mediaPlayer.next()
        onTrackChange?.invoke("Next Track", "Artist", "Album")
    }
    
    onPrevious = {
        mediaPlayer.previous()
        onTrackChange?.invoke("Previous Track", "Artist", "Album")
    }
    
    onPlay = {
        mediaPlayer.setPlaybackStatus(PlaybackStatus.PLAYING)
    }
    
    onPause = {
        mediaPlayer.setPlaybackStatus(PlaybackStatus.PAUSED)
    }
    
    onStop = {
        mediaPlayer.setPlaybackStatus(PlaybackStatus.STOPPED)
    }
    
    onPlayPause = {
        if (playbackStatus == PlaybackStatus.PLAYING) {
            mediaPlayer.setPlaybackStatus(PlaybackStatus.PAUSED)
        } else {
            mediaPlayer.setPlaybackStatus(PlaybackStatus.PLAYING)
        }
    }
}