package org.mpris;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.types.Variant;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 @see <a href="https://www.freedesktop.org/wiki/Specifications/mpris-spec/metadata/">MPRIS v2 metadata guidelines</a>
 */
public class Metadata {
    private Map<String, Variant<?>> internalMap;

    private Metadata() {
    }

    Metadata(final Map<String, Variant<?>> internalMap) {
        this.internalMap = internalMap;
    }

    Map<String, Variant<?>> getInternalMap() {
        return internalMap;
    }

    public static class Builder {
        private final Map<String, Variant<?>> internalMap = new HashMap<>();

        /**
         * @param trackID D-Bus path: A unique identity for this track within the context of an MPRIS object (eg: tracklist).
         */
        public Builder setTrackID(DBusPath trackID) {
            internalMap.put("mpris:trackid", new Variant<>(trackID, "o"));
            return this;
        }

        /**
         * @param duration 64-bit integer: The duration of the track in microseconds.
         */
        public Builder setLength(int duration) {
            internalMap.put("mpris:length", new Variant<>(duration, "x"));
            return this;
        }

        /**
         * @param artURL URI: The location of an image representing the track or album.
         *               Clients should not assume this will continue to exist when the media player stops giving out the URL.
         */
        public Builder setArtURL(URI artURL) {
            internalMap.put("mpris:artUrl", new Variant<>(artURL.toString(), "s"));
            return this;
        }

        /**
         * @param albumName String: The album name.
         */
        public Builder setAlbumName(String albumName) {
            internalMap.put("xesam:album", new Variant<>(albumName, "s"));
            return this;
        }

        /**
         * @param albumArtists List of Strings: The album artist(s).
         */
        public Builder setAlbumArtists(List<String> albumArtists) {
            internalMap.put("xesam:albumArtist", new Variant<>(albumArtists, "as"));
            return this;
        }

        /**
         * @param artists List of Strings: The track artist(s).
         */
        public Builder setArtists(List<String> artists) {
            internalMap.put("xesam:artist", new Variant<>(artists, "as"));
            return this;
        }

        /**
         * @param text String: The track lyrics.
         */
        public Builder setAsText(String text) {
            internalMap.put("xesam:asText", new Variant<>(text, "s"));
            return this;
        }

        /**
         * @param bpm Integer: The speed of the music, in beats per minute.
         */
        public Builder setAudioBPM(int bpm) {
            internalMap.put("xesam:audioBPM", new Variant<>(bpm, "x"));
            return this;
        }

        /**
         * @param autoRating Float: An automatically-generated rating, based on things such as how often it has been played. This should be in the range 0.0 to 1.0.
         */
        public Builder setAutoRating(double autoRating) throws IllegalArgumentException {
            if (autoRating < 0.0 || autoRating > 1.0) {
                throw new IllegalArgumentException("autoRating must be between 0.0f and 1.0f");
            }
            internalMap.put("xesam:autoRating", new Variant<>(autoRating, "d"));
            return this;
        }

        /**
         * @param comments List of Strings: A (list of) freeform comment(s).
         */
        public Builder setComments(List<String> comments) {
            internalMap.put("xesam:comment", new Variant<>(comments, "as"));
            return this;
        }

        /**
         * @param composers List of Strings: The composer(s) of the track.
         */
        public Builder setComposers(List<String> composers) {
            internalMap.put("xesam:composer", new Variant<>(composers, "as"));
            return this;
        }

        /**
         * @param date Date/Time: When the track was created. Usually only the year component will be useful.
         * @param offset The offset of utc (e.g. Germany: (Winter)+01:00 / (Summer)+02:00)
         */
        public Builder setContentCreated(Date date, ZoneOffset offset) {
            Instant instant = date.toInstant();
            OffsetDateTime offsetDateTime = instant.atOffset(offset);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            internalMap.put("xesam:contentCreated", new Variant<>(offsetDateTime.format(formatter), "s"));
            return this;
        }

        /**
         * @param discNumber Integer: The disc number on the album that this track is from.
         */
        public Builder setDiscNumber(int discNumber) {
            internalMap.put("xesam:discNumber", new Variant<>(discNumber, "x"));
            return this;
        }

        /**
         * @param date Date/Time: When the track was first played.
         * @param offset The offset of utc (e.g. Germany: (Winter)+01:00 / (Summer)+02:00)
         */
        public Builder setFirstUsed(Date date, ZoneOffset offset) {
            Instant instant = date.toInstant();
            OffsetDateTime offsetDateTime = instant.atOffset(offset);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            internalMap.put("xesam:firstUsed", new Variant<>(offsetDateTime.format(formatter), "s"));
            return this;
        }

        /**
         * @param genres List of Strings: The genre(s) of the track.
         */
        public Builder setGenres(List<String> genres) {
            internalMap.put("xesam:genre", new Variant<>(genres, "as"));
            return this;
        }

        /**
         * @param date Date/Time: When the track was last played.
         * @param offset The offset of utc (e.g. Germany: (Winter)+01:00 / (Summer)+02:00)
         */
        public Builder setLastUsed(Date date, ZoneOffset offset) {
            Instant instant = date.toInstant();
            OffsetDateTime offsetDateTime = instant.atOffset(offset);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            internalMap.put("xesam:lastUsed", new Variant<>(offsetDateTime.format(formatter), "s"));
            return this;
        }

        /**
         * @param lyricists List of Strings: The lyricist(s) of the track.
         */
        public Builder setLyricists(List<String> lyricists) {
            internalMap.put("xesam:lyricist", new Variant<>(lyricists, "as"));
            return this;
        }

        /**
         * @param title String: The track title.
         */
        public Builder setTitle(String title) {
            internalMap.put("xesam:title", new Variant<>(title, "s"));
            return this;
        }

        /**
         * @param trackNumber Integer: The track number on the album disc.
         */
        public Builder setTrackNumber(int trackNumber) {
            internalMap.put("xesam:trackNumber", new Variant<>(trackNumber, "x"));
            return this;
        }

        /**
         * @param url URI: The location of the media file.
         */
        public Builder setURL(URI url) {
            internalMap.put("xesam:url", new Variant<>(url.toString(), "s"));
            return this;
        }

        /**
         * @param useCount Integer: The number of times the track has been played.
         */
        public Builder setUseCount(int useCount) {
            internalMap.put("xesam:useCount", new Variant<>(useCount, "x"));
            return this;
        }

        /**
         * @param userRating Float: A user-specified rating. This should be in the range 0.0 to 1.0.
         */
        public Builder setUserRating(double userRating) throws IllegalArgumentException {
            if(userRating < 0.0 || userRating > 1.0) {
                throw new IllegalArgumentException("userRating must be between 0.0f and 1.0f");
            }
            internalMap.put("xesam:userRating", new Variant<>(userRating, "d"));
            return this;
        }

        public Metadata build() throws IllegalArgumentException {
            if(!internalMap.containsKey("mpris:trackid")) {
                throw new IllegalArgumentException("mpris:trackid not set");
            }
            if(!internalMap.containsKey("mpris:length")) {
                throw new IllegalArgumentException("mpris:length not set");
            }
            return new Metadata(internalMap);
        }
    }
}
