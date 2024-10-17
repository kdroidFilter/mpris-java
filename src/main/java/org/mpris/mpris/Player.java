package com.spotifyxp.deps.de.werwolf2303.mpris.mpris;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.annotations.DBusProperty;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;

import java.util.Map;

/**
 * @see <a href="https://specifications.freedesktop.org/mpris-spec/latest/Player_Interface.html">Interface MediaPlayer2.Player</a>
 */

@DBusInterfaceName("org.mpris.MediaPlayer2.Player")
@DBusProperty(name = "PlaybackStatus", type = String.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "LoopStatus", type = String.class, access = DBusProperty.Access.READ_WRITE)
@DBusProperty(name = "Rate", type = Double.class, access = DBusProperty.Access.READ_WRITE)
@DBusProperty(name = "Shuffle", type = Boolean.class, access = DBusProperty.Access.READ_WRITE)
@DBusProperty(name = "Metadata", type = Map.class, access = DBusProperty.Access.READ) //Map<String, Variant<?>>
@DBusProperty(name = "Volume", type = Double.class, access = DBusProperty.Access.READ_WRITE)
@DBusProperty(name = "Position", type = Integer.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "MinimumRate", type = Double.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "MaximumRate", type = Double.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "CanGoNext", type = Boolean.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "CanGoPrevious", type = Boolean.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "CanPlay", type = Boolean.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "CanPause", type = Boolean.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "CanSeek", type = Boolean.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "CanControl", type = Boolean.class, access = DBusProperty.Access.READ)
public interface Player extends DBusInterface {
    /**
     * Skips to the next track in the tracklist.
     * If there is no next track (and endless playback and track repeat are both off), stop playback.
     * If playback is paused or stopped, it remains that way.
     * If CanGoNext is false, attempting to call this method should have no effect.
     */
    void Next();

    /**
     * Skips to the previous track in the tracklist.
     * If there is no previous track (and endless playback and track repeat are both off), stop playback.
     * If playback is paused or stopped, it remains that way.
     * If CanGoPrevious is false, attempting to call this method should have no effect.
     */
    void Previous();

    /**
     * Pauses playback.
     * If playback is already paused, this has no effect.
     * Calling Play after this should cause playback to start again from the same position.
     * If CanPause is false, attempting to call this method should have no effect.
     */
    void Pause();

    /**
     * Pauses playback.
     * If playback is already paused, resumes playback.
     * If playback is stopped, starts playback.
     * If CanPause is false, attempting to call this method should have no effect and raise an error.
     */
    void PlayPause();

    /**
     * Stops playback.
     * If playback is already stopped, this has no effect.
     * Calling Play after this should cause playback to start again from the beginning of the track.
     * If CanControl is false, attempting to call this method should have no effect and raise an error.
     */
    void Stop();

    /**
     * Starts or resumes playback.
     * If already playing, this has no effect.
     * If paused, playback resumes from the current position.
     * If there is no track to play, this has no effect.
     * If CanPlay is false, attempting to call this method should have no effect.
     */
    void Play();

    /**
     * Seeks forward in the current track by the specified number of microseconds.
     * A negative value seeks back. If this would mean seeking back further than the start of the track, the position is set to 0.
     * If the value passed in would mean seeking beyond the end of the track, acts like a call to Next.
     * If the CanSeek property is false, this has no effect.
     * @param x The number of microseconds to seek forward.
     */
    void Seek(int x);

    /**
     * Sets the current track position in microseconds.
     * If the Position argument is less than 0, do nothing.
     * If the Position argument is greater than the track length, do nothing.
     * If the CanSeek property is false, this has no effect.
     * @param Track_Id
     * The currently playing track's identifier.
     * If this does not match the id of the currently-playing track, the call is ignored as "stale".
     * /org/mpris/MediaPlayer2/TrackList/NoTrack is not a valid value for this argument.
     * @param x Track position in microseconds. Between 0 and track length
     */
    void SetPosition(DBusPath Track_Id, int x);

    /**
     * Opens the Uri given as an argument
     * If the playback is stopped, starts playing
     * If the uri scheme or the mime-type of the uri to open is not supported,
     * this method does nothing and may raise an error. In particular,
     * if the list of available uri schemes is empty, this method may not be implemented.
     * Clients should not assume that the Uri has been opened as soon as this method returns.
     * They should wait until the mpris:trackid field in the Metadata property changes.
     * If the media player implements the TrackList interface,
     * then the opened track should be made part of the tracklist,
     * the org.mpris.MediaPlayer2.TrackList.TrackAdded or org.mpris.MediaPlayer2.TrackList.TrackListReplaced
     * signal should be fired, as well as the org.freedesktop.DBus.Properties.PropertiesChanged
     * signal on the tracklist interface.
     * @param Uri Uri of the track to load.
     *            Its uri scheme should be an element of the org.mpris.MediaPlayer2.SupportedUriSchemes
     *            property and the mime-type should match one of the elements of the
     *            org.mpris.MediaPlayer2.SupportedMimeTypes.
     */
    void OpenURI(String Uri);

    /**
     * Indicates that the track position has changed in a way that is inconsistant with the current playing state.
     * When this signal is not received, clients should assume that:
     * When playing, the position progresses according to the rate property.
     * When paused, it remains constant.
     * This signal does not need to be emitted when playback starts or when the track changes, unless the track is starting at an unexpected position.
     * An expected position would be the last known one when going from Paused to Playing, and 0 when going from Stopped to Playing.
     */
    class Seeked extends DBusSignal {
        private final long timeInUs;

        /**
         * @param _path The path where the signal should be emitted
         * @param _timeInUs The new position, in microseconds.
         * @throws DBusException
         */
        public Seeked(String _path, long _timeInUs) throws DBusException {
            super(_path);
            timeInUs = _timeInUs;
        }

        public long getTimeInUs() {
            return timeInUs;
        }
    }
}
