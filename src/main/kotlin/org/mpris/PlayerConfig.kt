package org.mpris

import org.freedesktop.dbus.DBusPath
import org.mpris.mpris.LoopStatus
import org.mpris.mpris.PlaybackStatus

/**
 * Configuration for the Player interface
 */
class PlayerConfig {
    var playbackStatus: PlaybackStatus = PlaybackStatus.STOPPED
    var loopStatus: LoopStatus = LoopStatus.NONE
    var rate: Double = 1.0
    var shuffle: Boolean = false
    var metadata: Metadata? = null
    var volume: Double = 1.0
    var position: Int = 0
    var minimumRate: Double = 0.1
    var maximumRate: Double = 2.0
    var canGoNext: Boolean = false
    var canGoPrevious: Boolean = false
    var canPlay: Boolean = false
    var canPause: Boolean = false
    var canSeek: Boolean = false
    var canControl: Boolean = false
    var onNext: () -> Unit = {}
    var onPrevious: () -> Unit = {}
    var onPause: () -> Unit = {}
    var onPlayPause: () -> Unit = {}
    var onStop: () -> Unit = {}
    var onPlay: () -> Unit = {}
    var onSeek: (Int) -> Unit = {}
    var onSetPosition: (Map<DBusPath, Int>) -> Unit = {}
    var onOpenURI: (String) -> Unit = {}
    var onSignalSeeked: (Long) -> Unit = {}

    /**
     * Create metadata for the player
     */
    fun metadata(block: MetadataBuilder.() -> Unit) {
        metadata = MetadataBuilder().apply(block).build()
    }

    /**
     * Convert to a Java builder
     */
    internal fun toBuilder(): MPRISMediaPlayer.PlayerBuilder {
        if (metadata == null) {
            throw IllegalStateException("Metadata must be set")
        }

        return MPRISMediaPlayer.PlayerBuilder()
            .setPlaybackStatus(playbackStatus)
            .setLoopStatus(loopStatus)
            .setRate(rate)
            .setShuffle(shuffle)
            .setMetadata(metadata!!)
            .setVolume(volume)
            .setPosition(position)
            .setMinimumRate(minimumRate)
            .setMaximumRate(maximumRate)
            .setCanGoNext(canGoNext)
            .setCanGoPrevious(canGoPrevious)
            .setCanPlay(canPlay)
            .setCanPause(canPause)
            .setCanSeek(canSeek)
            .setCanControl(canControl)
            .setOnNext { onNext() }
            .setOnPrevious { onPrevious() }
            .setOnPause { onPause() }
            .setOnPlayPause { onPlayPause() }
            .setOnStop { onStop() }
            .setOnPlay { onPlay() }
            .setOnSeek { onSeek(it) }
            .setOnSetPosition { onSetPosition(it) }
            .setOnOpenURI { onOpenURI(it) }
            .setOnSignalSeeked { onSignalSeeked(it) }
    }
}