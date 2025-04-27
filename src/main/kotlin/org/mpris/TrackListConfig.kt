package org.mpris

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.types.Variant

/**
 * Configuration for the TrackList interface
 */
class TrackListConfig {
    var tracks: List<DBusPath> = emptyList()
    var canEditTracks: Boolean = false
    var onGetTracksMetadata: (List<DBusPath>) -> List<Map<String, Variant<*>>> = { emptyList() }
    var onAddTrack: (List<Any>) -> Unit = {}
    var onRemoveTrack: (DBusPath) -> Unit = {}
    var onGoTo: (DBusPath) -> Unit = {}
    var onSignalTrackListReplaced: (org.mpris.mpris.TrackList.TrackListReplaced) -> Unit = {}
    var onSignalTrackAdded: (org.mpris.mpris.TrackList.TrackAdded) -> org.mpris.mpris.TrackList.TrackAdded = { it }
    var onSignalTrackRemoved: (org.mpris.mpris.TrackList.TrackRemoved) -> Unit = {}
    var onSignalTrackMetadataChanged: (org.mpris.mpris.TrackList.TrackMetadataChanged) -> Unit = {}

    /**
     * Convert to a Java builder
     */
    internal fun toBuilder(): MPRISMediaPlayer.TrackListBuilder {
        return MPRISMediaPlayer.TrackListBuilder()
            .setTracks(*tracks.toTypedArray())
            .setCanEditTracks(canEditTracks)
            .setOnGetTracksMetadata { onGetTracksMetadata(it) }
            .setOnAddTrack { onAddTrack(it) }
            .setOnRemoveTrack { onRemoveTrack(it) }
            .setOnGoTo { onGoTo(it) }
            .setOnSignalTrackListReplaced { onSignalTrackListReplaced(it) }
            .setOnSignalTrackAdded { onSignalTrackAdded(it) }
            .setOnSignalTrackRemoved { onSignalTrackRemoved(it) }
            .setOnSignalTrackMetadataChanged { onSignalTrackMetadataChanged(it) }
    }
}