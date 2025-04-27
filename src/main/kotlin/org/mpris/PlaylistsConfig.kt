package org.mpris

import org.freedesktop.dbus.DBusPath
import org.mpris.mpris.PlaylistOrdering

/**
 * Configuration for the Playlists interface
 */
class PlaylistsConfig {
    var playlistsCount: Int = 0
    var orderings: List<PlaylistOrdering> = emptyList()
    var activePlaylist: org.mpris.mpris.Playlists.Maybe_Playlist? = null
    var onActivatePlaylist: (DBusPath) -> Unit = {}
    var onGetPlaylists: (List<Any>) -> List<org.mpris.mpris.Playlists.Playlist> = { emptyList() }
    var onSignalPlaylistChanged: (org.mpris.mpris.Playlists.PlaylistChanged) -> Unit = {}

    /**
     * Convert to a Java builder
     */
    internal fun toBuilder(): MPRISMediaPlayer.PlaylistsBuilder {
        if (activePlaylist == null) {
            throw IllegalStateException("activePlaylist must be set")
        }

        return MPRISMediaPlayer.PlaylistsBuilder()
            .setPlaylistsCount(playlistsCount)
            .setOrderings(orderings)
            .setActivePlaylist(activePlaylist!!)
            .setOnActivatePlaylist { onActivatePlaylist(it) }
            .setOnGetPlaylists { onGetPlaylists(it) }
            .setOnSignalPlaylistChanged { onSignalPlaylistChanged(it) }
    }
}