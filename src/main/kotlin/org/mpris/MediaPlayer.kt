package org.mpris

import org.freedesktop.dbus.connections.impl.DBusConnection

/**
 * Creates a new MPRIS media player with the given name and configuration.
 *
 * @param playerName The name of the player (e.g. "myplayer" or "myplayer.instance1234")
 * @param connection Optional DBus connection (defaults to SESSION bus)
 * @param block Configuration block for the media player
 * @return The created MPRISMediaPlayer instance
 */
fun createMediaPlayer(
    playerName: String,
    connection: DBusConnection = DBusConnection.newConnection(DBusConnection.DBusBusType.SESSION),
    block: MediaPlayerConfig.() -> Unit
): MPRISMediaPlayer {
    val config = MediaPlayerConfig().apply(block)
    val mediaPlayer = MPRISMediaPlayer(connection, playerName)

    // Build the media player with the configuration
    val mediaPlayer2Builder = config.mediaPlayer2Config.toBuilder()
    val playerBuilder = config.playerConfig.toBuilder()

    // Build the appropriate type based on the configuration
    when {
        config.trackListConfig != null && config.playlistsConfig != null -> {
            val trackListBuilder = config.trackListConfig!!.toBuilder()
            val playlistsBuilder = config.playlistsConfig!!.toBuilder()
            mediaPlayer.buildMPRISMediaPlayer2All(mediaPlayer2Builder, playerBuilder, trackListBuilder, playlistsBuilder)
        }
        config.trackListConfig != null -> {
            val trackListBuilder = config.trackListConfig!!.toBuilder()
            mediaPlayer.buildMPRISMediaPlayer2WTL(mediaPlayer2Builder, playerBuilder, trackListBuilder)
        }
        config.playlistsConfig != null -> {
            val playlistsBuilder = config.playlistsConfig!!.toBuilder()
            mediaPlayer.buildMPRISMediaPlayer2WPL(mediaPlayer2Builder, playerBuilder, playlistsBuilder)
        }
        else -> {
            mediaPlayer.buildMPRISMediaPlayer2None(mediaPlayer2Builder, playerBuilder)
        }
    }

    // Create the media player on the D-Bus
    mediaPlayer.create()

    return mediaPlayer
}