package com.spotifyxp.deps.de.werwolf2303.mpris.mpris;

import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.annotations.DBusProperty;
import org.freedesktop.dbus.interfaces.DBusInterface;

import java.util.List;

/**
 * @see <a href="https://specifications.freedesktop.org/mpris-spec/latest/Media_Player.html">Interface MediaPlayer2</a>
 */

@DBusInterfaceName("org.mpris.MediaPlayer2")
@DBusProperty(name = "CanQuit", type = Boolean.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "Fullscreen", type = Boolean.class, access = DBusProperty.Access.READ_WRITE)
@DBusProperty(name = "CanSetFullscreen", type = Boolean.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "CanRaise", type = Boolean.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "HasTrackList", type = Boolean.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "Identity", type = String.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "DesktopEntry", type = String.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "SupportedUriSchemes", type = List.class, access = DBusProperty.Access.READ)
@DBusProperty(name = "SupportedMimeTypes", type = List.class, access = DBusProperty.Access.READ)
public interface MediaPlayer2 extends DBusInterface {

    /**
     * Brings the media player's user interface to the front using any appropriate mechanism available.
     * The media player may be unable to control how its user interface is displayed, or it may not have a
     * graphical user interface at all. In this case, the CanRaise property is false and this method does nothing.
     */
    void Raise();

    /**
     * Causes the media player to stop running.
     * The media player may refuse to allow clients to shut it down. In this case, the CanQuit property is false and this method does nothing.
     * Note: Media players which can be D-Bus activated, or for which there is no sensibly easy way to terminate a running instance (via the main interface or a notification area icon for example)
     * should allow clients to use this method. Otherwise, it should not be needed.
     * If the media player does not have a UI, this should be implemented.
     */
    void Quit();
}
