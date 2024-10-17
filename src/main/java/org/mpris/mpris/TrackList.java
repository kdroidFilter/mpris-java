package com.spotifyxp.deps.de.werwolf2303.mpris.mpris;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.annotations.DBusProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;
import org.freedesktop.dbus.types.Variant;

import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://specifications.freedesktop.org/mpris-spec/latest/Track_List_Interface.html">Interface MediaPlayer2.TrackList</a>
 */

@DBusInterfaceName("org.mpris.MediaPlayer2.TrackList")
@DBusProperty(name = "Tracks", type = List.class, access = Access.READ) //List<DBusPath>
@DBusProperty(name = "CanEditTracks", type = Boolean.class, access = Access.READ)
public interface TrackList extends DBusInterface {
    /**
     * Gets all the metadata available for a set of tracks.
     * Each set of metadata must have a "mpris:trackid" entry at the very least,
     * which contains a string that uniquely identifies this track within the scope of the tracklist.
     * @param TrackIds The list of track ids for which metadata is requested.
     * @return Metadata of the set of tracks given as input.
     * See the type documentation for more details.
     */
    List<Map<String, Variant<?>>> GetTracksMetadata(List<DBusPath> TrackIds);

    /**
     * Adds a URI in the TrackList.
     * If the CanEditTracks property is false, this has no effect.
     * Note: Clients should not assume that the track has been added at the time when this method returns.
     * They should wait for a TrackAdded (or TrackListReplaced) signal.
     * @param Uri The uri of the item to add. Its uri scheme should be an element of the
     *            org.mpris.MediaPlayer2.SupportedUriSchemes property and the mime-type should match one of the
     *            elements of the org.mpris.MediaPlayer2.SupportedMimeTypes
     * @param AfterTrack The identifier of the track after which the new item should be inserted.
     *                   The path /org/mpris/MediaPlayer2/TrackList/NoTrack indicates that the
     *                   track should be inserted at the start of the track list.
     * @param SetAsCurrent Whether the newly inserted track should be considered as the current track.
     *                     Setting this to true has the same effect as calling GoTo afterwards.
     */
    void AddTrack(String Uri, DBusPath AfterTrack, boolean SetAsCurrent);

    /**
     * Removes an item from the TrackList.
     * If the track is not part of this tracklist, this has no effect.
     * If the CanEditTracks property is false, this has no effect.
     * Note: Clients should not assume that the track has been removed at the time when this method returns.
     * They should wait for a TrackRemoved (or TrackListReplaced) signal.
     * @param TrackId Identifier of the track to be removed. /org/mpris/MediaPlayer2/TrackList/NoTrack is not a valid value for this argument.
     */
    void RemoveTrack(DBusPath TrackId);

    /**
     * Skip to the specified TrackId.
     * If the track is not part of this tracklist, this has no effect.
     * If this object is not /org/mpris/MediaPlayer2, the current TrackList's tracks should be replaced with the contents of this TrackList,
     * and the TrackListReplaced signal should be fired from /org/mpris/MediaPlayer2.
     * @param TrackId Identifier of the track to skip to. /org/mpris/MediaPlayer2/TrackList/NoTrack is not a valid value for this argument.
     */
    void GoTo(DBusPath TrackId);


    /**
     * Indicates that the entire tracklist has been replaced.
     * It is left up to the implementation to decide when a change to the track list is invasive enough that this signal
     * should be emitted instead of a series of TrackAdded and TrackRemoved signals.
     */
    class TrackListReplaced extends DBusSignal {
        private final List<DBusPath> tracks;
        private final DBusPath currentTrack;

        /**
         * @param Tracks The new content of the tracklist.
         * @param CurrentTrack The identifier of the track to be considered as current.
         *                     /org/mpris/MediaPlayer2/TrackList/NoTrack indicates that there is no current track.
         *                     This should correspond to the mpris:trackid field of the Metadata property of the org.mpris.MediaPlayer2.Player interface.
         * @throws DBusException
         */
        public TrackListReplaced(String objectPath, List<DBusPath> Tracks, DBusPath CurrentTrack) throws DBusException {
            super(objectPath, Tracks, CurrentTrack);
            this.tracks = Tracks;
            this.currentTrack = CurrentTrack;
        }

        public List<DBusPath> getTracks() {
            return tracks;
        }

        public DBusPath getCurrentTrack() {
            return currentTrack;
        }
    }

    /**
     * Indicates that a track has been added to the track list.
     */
    class TrackAdded extends DBusSignal {
        private final Map<String, Variant<?>> metadata;
        private final DBusPath aftertrack;

        /**
         * @param Metadata The metadata of the newly added item.
         *                 This must include a mpris:trackid entry.
         *                 See the type documentation for more details.
         * @param AfterTrack The identifier of the track after which the new track was inserted.
         *                   The path /org/mpris/MediaPlayer2/TrackList/NoTrack indicates that the track was inserted at the start of the track list.
         * @throws DBusException
         */
        public TrackAdded(String objectPath, Map<String, Variant<?>> Metadata, DBusPath AfterTrack) throws DBusException {
            super(objectPath, Metadata, AfterTrack);
            this.metadata = Metadata;
            this.aftertrack = AfterTrack;
        }

        public Map<String, Variant<?>> getMetadata() {
            return metadata;
        }

        public DBusPath getAfterTrack() {
            return aftertrack;
        }
    }

    /**
     * Indicates that a track has been removed from the track list.
     */
    class TrackRemoved extends DBusSignal {
        private final DBusPath trackid;

        /**
         * @param TrackId The identifier of the track being removed.
         *                /org/mpris/MediaPlayer2/TrackList/NoTrack is not a valid value for this argument.
         * @throws DBusException
         */
        public TrackRemoved(String objectPath, DBusPath TrackId) throws DBusException {
            super(objectPath, TrackId);
            this.trackid = TrackId;
        }

        public DBusPath getTrackId() {
            return trackid;
        }
    }

    /**
     * Indicates that the metadata of a track in the tracklist has changed.
     * This may indicate that a track has been replaced, in which case the mpris:trackid metadata entry is different from the TrackId argument.
     */
    class TrackMetadataChanged extends DBusSignal {
        private final DBusPath trackid;
        private final Map<String, Variant<?>> metadata;

        /**
         * @param TrackId The id of the track which metadata has changed.
         *                If the track id has changed, this will be the old value.
         *                /org/mpris/MediaPlayer2/TrackList/NoTrack is not a valid value for this argument.
         * @param Metadata The new track metadata.
         *                 This must include a mpris:trackid entry. If the track id has changed, this will be the new value.
         *                 See the type documentation for more details.
         * @throws DBusException
         */
        public TrackMetadataChanged(String objectPath, DBusPath TrackId, Map<String, Variant<?>> Metadata) throws DBusException {
            super(objectPath, TrackId, Metadata);
            this.trackid = TrackId;
            this.metadata = Metadata;
        }

        public DBusPath getTrackId() {
            return trackid;
        }

        public Map<String, Variant<?>> getMetadata() {
            return metadata;
        }
    }
}