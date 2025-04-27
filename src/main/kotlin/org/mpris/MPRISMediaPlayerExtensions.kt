package org.mpris

import org.freedesktop.dbus.exceptions.DBusException
import org.mpris.mpris.PlaybackStatus

/**
 * Extension functions for MPRISMediaPlayer
 */

/**
 * Update the playback status
 */
fun MPRISMediaPlayer.setPlaybackStatus(status: PlaybackStatus) {
    try {
        // Currently only NONE mode is supported via public API
        getMPRISMediaPlayer2None()?.setPlaybackStatus(status)
    } catch (e: DBusException) {
        throw RuntimeException("Failed to update playback status", e)
    }
}

/**
 * Update the metadata
 */
fun MPRISMediaPlayer.setMetadata(metadata: Metadata) {
    try {
        // Currently only NONE mode is supported via public API
        getMPRISMediaPlayer2None()?.setMetadata(metadata)
    } catch (e: DBusException) {
        throw RuntimeException("Failed to update metadata", e)
    }
}

/**
 * Update the metadata using a builder
 */
fun MPRISMediaPlayer.setMetadata(block: MetadataBuilder.() -> Unit) {
    val metadata = MetadataBuilder().apply(block).build()
    setMetadata(metadata)
}

/**
 * Play the media
 */
fun MPRISMediaPlayer.play() {
    // Currently only NONE mode is supported via public API
    getMPRISMediaPlayer2None()?.Play()
}

/**
 * Pause the media
 */
fun MPRISMediaPlayer.pause() {
    // Currently only NONE mode is supported via public API
    getMPRISMediaPlayer2None()?.Pause()
}

/**
 * Toggle play/pause
 */
fun MPRISMediaPlayer.playPause() {
    // Currently only NONE mode is supported via public API
    getMPRISMediaPlayer2None()?.PlayPause()
}

/**
 * Stop the media
 */
fun MPRISMediaPlayer.stop() {
    // Currently only NONE mode is supported via public API
    getMPRISMediaPlayer2None()?.Stop()
}

/**
 * Go to the next track
 */
fun MPRISMediaPlayer.next() {
    // Currently only NONE mode is supported via public API
    getMPRISMediaPlayer2None()?.Next()
}

/**
 * Go to the previous track
 */
fun MPRISMediaPlayer.previous() {
    // Currently only NONE mode is supported via public API
    getMPRISMediaPlayer2None()?.Previous()
}