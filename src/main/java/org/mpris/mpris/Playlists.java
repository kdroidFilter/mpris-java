package org.mpris.mpris;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.annotations.DBusProperty;
import org.freedesktop.dbus.annotations.Position;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;

import java.util.List;

/**
 * @see <a href="https://specifications.freedesktop.org/mpris-spec/latest/Playlists_Interface.html">Interface MediaPlayer2.Playlists</a>
 * @since 2.1
 */

@DBusInterfaceName("org.mpris.MediaPlayer2.Playlists")
@DBusProperty(name = "PlaylistCount", type = Integer.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "Orderings", type = List.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "ActivePlaylist", type = Playlists.Maybe_Playlist.class, access = DBusProperty.Access.READ)
public interface Playlists extends DBusInterface {
    /**
     * Starts playing the given playlist.
     * Note that this must be implemented. If the media player does not allow clients to change the playlist, it should not implement this interface at all.
     * It is up to the media player whether this completely replaces the current tracklist,
     * or whether it is merely inserted into the tracklist and the first track starts. For example,
     * if the media player is operating in a "jukebox" mode, it may just append the playlist to the list of upcoming tracks, and skip to the first track in the playlist.
     * @param PlaylistId The id of the playlist to activate.
     */
    void ActivatePlaylist(DBusPath PlaylistId);

    /**
     * Gets a set of playlists.
     * @param Index The index of the first playlist to be fetched (according to the ordering).
     * @param MaxCount The maximum number of playlists to fetch.
     * @param Order The ordering that should be used.
     * @param ReverseOrder Whether the order should be reversed.
     * @return A list of (at most MaxCount) playlists.
     */
    List<Playlist> GetPlaylists(int Index, int MaxCount, String Order, boolean ReverseOrder);

    /**
     * Indicates that either the Name or Icon attribute of a playlist has changed.
     * Client implementations should be aware that this signal may not be implemented.
     */
    class PlaylistChanged extends DBusSignal {
        private final Playlist playlist;

        /**
         * @param playlist The playlist which details have changed.
         * @throws DBusException
         */
        public PlaylistChanged(String objectpath, Playlist playlist) throws DBusException {
            super(objectpath, playlist);
            this.playlist = playlist;
            if(playlist == null) throw new IllegalArgumentException("Playlist is null");
        }

        public Playlist getPlaylist() {
            return playlist;
        }
    }

    /**
     * A data structure describing a playlist, or nothing.
     */
    class Maybe_Playlist extends Struct {
        @Position(0)
        private final boolean hasPlaylist;

        @Position(1)
        private final Playlist playlist;

        /**
         * @param hasPlaylist Whether this structure refers to a valid playlist.
         * @param playlist The playlist, providing Valid is true, otherwise undefined.
         *                 When constructing this type, it should be noted that the playlist ID must be a valid object path,
         *                 or D-Bus implementations may reject it. This is true even when Valid is false.
         *                 It is suggested that "/" is used as the playlist ID in this case.
         */
        public Maybe_Playlist(boolean hasPlaylist, Playlist playlist) {
            this.hasPlaylist = hasPlaylist;
            this.playlist = playlist;
            if(playlist == null) throw new IllegalArgumentException("Playlist is null");
        }

        public boolean hasPlaylist() {
            return hasPlaylist;
        }

        public Playlist getPlaylist() {
            return playlist;
        }
    }

    /**
     * A data structure describing a playlist.
     */
    class Playlist extends Struct {
        @Position(0)
        private DBusPath id;
        @Position(1)
        private String name;
        @Position(2)
        private String icon;

        /**
         * @param id A unique identifier for the playlist. This should remain the same if the playlist is renamed.
         * @param name The name of the playlist, typically given by the user.
         * @param icon The URI of an (optional) icon.
         */
        public Playlist(DBusPath id, String name, String icon) {
            this.id = id;
            this.name = name;
            this.icon = icon;
        }

        public DBusPath getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }
    }
}
