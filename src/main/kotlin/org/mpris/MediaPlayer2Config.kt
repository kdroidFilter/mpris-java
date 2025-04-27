package org.mpris

/**
 * Configuration for the MediaPlayer2 interface
 */
class MediaPlayer2Config {
    var canQuit: Boolean = false
    var fullscreen: Boolean = false
    var canSetFullscreen: Boolean = false
    var canRaise: Boolean = false
    var identity: String = ""
    var desktopEntry: String = ""
    var supportedUriSchemes: List<String> = emptyList()
    var supportedMimeTypes: List<String> = emptyList()
    var onRaise: () -> Unit = {}
    var onQuit: () -> Unit = {}

    /**
     * Convert to a Java builder
     */
    internal fun toBuilder(): MPRISMediaPlayer.MediaPlayer2Builder {
        return MPRISMediaPlayer.MediaPlayer2Builder()
            .setCanQuit(canQuit)
            .setFullscreen(fullscreen)
            .setCanSetFullscreen(canSetFullscreen)
            .setCanRaise(canRaise)
            .setIdentity(identity)
            .setDesktopEntry(desktopEntry)
            .setSupportedUriSchemes(*supportedUriSchemes.toTypedArray())
            .setSupportedMimeTypes(*supportedMimeTypes.toTypedArray())
            .setOnRaise { onRaise() }
            .setOnQuit { onQuit() }
    }
}