package org.mpris

import org.freedesktop.dbus.DBusPath
import java.net.URI

/**
 * Builder for creating metadata
 */
class MetadataBuilder {
    private val builder = Metadata.Builder()

    /**
     * Set the track ID
     */
    fun trackId(trackId: DBusPath) {
        builder.setTrackID(trackId)
    }

    /**
     * Set the track length in microseconds
     */
    fun length(duration: Int) {
        builder.setLength(duration)
    }

    /**
     * Set the art URL
     */
    fun artUrl(artUrl: URI) {
        builder.setArtURL(artUrl)
    }

    /**
     * Set the album name
     */
    fun albumName(albumName: String) {
        builder.setAlbumName(albumName)
    }

    /**
     * Set the album artists
     */
    fun albumArtists(vararg artists: String) {
        builder.setAlbumArtists(artists.toList())
    }

    /**
     * Set the track artists
     */
    fun artists(vararg artists: String) {
        builder.setArtists(artists.toList())
    }

    /**
     * Set the track title
     */
    fun title(title: String) {
        builder.setTitle(title)
    }

    /**
     * Set the track number
     */
    fun trackNumber(trackNumber: Int) {
        builder.setTrackNumber(trackNumber)
    }

    /**
     * Set the disc number
     */
    fun discNumber(discNumber: Int) {
        builder.setDiscNumber(discNumber)
    }

    /**
     * Set the genres
     */
    fun genres(vararg genres: String) {
        builder.setGenres(genres.toList())
    }

    /**
     * Build the metadata
     */
    internal fun build(): Metadata {
        return builder.build()
    }
}