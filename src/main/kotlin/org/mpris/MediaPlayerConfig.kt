package org.mpris

/**
 * Configuration class for the media player
 */
class MediaPlayerConfig {
    var mediaPlayer2Config = MediaPlayer2Config()
    var playerConfig = PlayerConfig()
    var trackListConfig: TrackListConfig? = null
    var playlistsConfig: PlaylistsConfig? = null

    /**
     * Configure the MediaPlayer2 interface
     */
    fun mediaPlayer2(block: MediaPlayer2Config.() -> Unit) {
        mediaPlayer2Config.apply(block)
    }

    /**
     * Configure the Player interface
     */
    fun player(block: PlayerConfig.() -> Unit) {
        playerConfig.apply(block)
    }

    /**
     * Configure the TrackList interface
     */
    fun trackList(block: TrackListConfig.() -> Unit) {
        trackListConfig = TrackListConfig().apply(block)
    }

    /**
     * Configure the Playlists interface
     */
    fun playlists(block: PlaylistsConfig.() -> Unit) {
        playlistsConfig = PlaylistsConfig().apply(block)
    }
}