package com.spotifyxp.deps.de.werwolf2303.mpris;

import com.spotifyxp.deps.de.werwolf2303.mpris.mpris.*;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.types.Variant;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unused")
public class MPRISMediaPlayer {
    private MPRISMP2All mprisMediaPlayer2All;
    private MPRISMP2None mprisMediaPlayer2None;
    private MPRISMP2WPL mprisMediaPlayer2WPL;
    private MPRISMP2WTL mprisMediaPlayer2WTL;
    private final String playerName;
    private final DBusConnection connection;
    private MediaPlayer2Mode buildMode = MediaPlayer2Mode.NONE;

    /**
     * @param connection DBusConnection (System, Session)
     * @param playerName The name of the player (e.g. spotifyxp or spotifyxp.instance1234).
     *                   See <a href="https://specifications.freedesktop.org/mpris-spec/latest/#Bus-Name-Policy">Specification</a>
     */
    public MPRISMediaPlayer(
            DBusConnection connection,
            @NotNull String playerName
    ) throws DBusException {
        this.playerName = playerName;
        if(connection == null) connection = DBusConnection.newConnection(DBusConnection.DBusBusType.SESSION);
        this.connection = connection;
    }

    /**
     * @return The name of the player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Types of players
     * <p>ALL  = MediaPlayer2, Player, TrackList, Playlists</p>
     * <p>WPL  = MediaPlayer2, Player, Playlists</p>
     * <p>WTL  = MediaPlayer2, Player TrackList</p>
     * <p>NONE = MediaPlayer2, Player</p>
     */
    enum MediaPlayer2Mode {
        ALL, // MediaPlayer2, Player, TrackList, Playlists
        WPL, // MediaPlayer2, Player, Playlists
        WTL, // MediaPlayer2, Player TrackList
        NONE // MediaPlayer2, Player (REQUIRED Fields)
    }

    /**
     * Builds an instance of MPRISMediaPlayer2All
     * Contains: MediaPlayer2, Player, TrackList, Playlists
     */
    public MPRISMP2All buildMPRISMediaPlayer2All(
            @NotNull MediaPlayer2Builder mediaPlayer2Builder,
            @NotNull PlayerBuilder playerBuilder,
            @NotNull TrackListBuilder trackListBuilder,
            @NotNull PlaylistsBuilder playlistsBuilder
    ) {
        mprisMediaPlayer2All = mediaPlayer2Builder.buildAll(
                connection,
                playerBuilder,
                trackListBuilder,
                playlistsBuilder
        );
        buildMode = MediaPlayer2Mode.ALL;
        return mprisMediaPlayer2All;
    }

    /**
     * Builds an instance of MPRISMediaPlayer2None
     * Contains: MediaPlayer2, Player
     */
    public MPRISMP2None buildMPRISMediaPlayer2None(
            @NotNull MediaPlayer2Builder mediaPlayer2Builder,
            @NotNull PlayerBuilder playerBuilder
    ) {
        mprisMediaPlayer2None = mediaPlayer2Builder.buildNone(
                connection,
                playerBuilder
        );
        buildMode = MediaPlayer2Mode.NONE;
        return mprisMediaPlayer2None;
    }

    /**
     * Builds an instance of MPRISMediaPlayer2All
     * Contains: MediaPlayer2, Player, TrackList, Playlists
     */
    public MPRISMP2WPL buildMPRISMediaPlayer2WPL(
            @NotNull MediaPlayer2Builder mediaPlayer2Builder,
            @NotNull PlayerBuilder playerBuilder,
            @NotNull PlaylistsBuilder playlistsBuilder
    ) {
        mprisMediaPlayer2WPL = mediaPlayer2Builder.buildWPL(
                connection,
                playerBuilder,
                playlistsBuilder
        );
        buildMode = MediaPlayer2Mode.WPL;
        return mprisMediaPlayer2WPL;
    }

    /**
     * Builds an instance of MPRISMediaPlayer2All
     * Contains: MediaPlayer2, Player, TrackList
     */
    public MPRISMP2WTL buildMPRISMediaPlayer2WTL(
            @NotNull MediaPlayer2Builder mediaPlayer2Builder,
            @NotNull PlayerBuilder playerBuilder,
            @NotNull TrackListBuilder trackListBuilder
    ) {
        mprisMediaPlayer2WTL = mediaPlayer2Builder.buildWTL(
                connection,
                playerBuilder,
                trackListBuilder
        );
        buildMode = MediaPlayer2Mode.WTL;
        return mprisMediaPlayer2WTL;
    }

    public static class MediaPlayer2Builder {
        boolean canQuit;
        boolean fullscreen;
        boolean canSetFullscreen;
        boolean canRaise;
        String identity;
        String desktopEntry;
        List<String> supportedUriSchemes;
        List<String> supportedMimeTypes;
        TypeRunnable<?> onRaise;
        TypeRunnable<?> onQuit;

        public MediaPlayer2Builder() {
            canQuit = false;
            fullscreen = false;
            canSetFullscreen = false;
            canRaise = false;
            identity = "";
            desktopEntry = "";
            supportedUriSchemes = Collections.emptyList();
            supportedMimeTypes = Collections.emptyList();
            onRaise = (T) -> {};
            onQuit = (T) -> {};
        }

        public MediaPlayer2Builder setCanQuit(boolean canQuit) {
            this.canQuit = canQuit;
            return this;
        }

        public MediaPlayer2Builder setFullscreen(boolean fullscreen) {
            this.fullscreen = fullscreen;
            return this;
        }

        public MediaPlayer2Builder setCanSetFullscreen(boolean canSetFullscreen) {
            this.canSetFullscreen = canSetFullscreen;
            return this;
        }

        public MediaPlayer2Builder setCanRaise(boolean canRaise) {
            this.canRaise = canRaise;
            return this;
        }

        public MediaPlayer2Builder setIdentity(@NotNull String identity) {
            this.identity = identity;
            return this;
        }

        public MediaPlayer2Builder setDesktopEntry(@NotNull String desktopEntry) {
            this.desktopEntry = desktopEntry;
            return this;
        }

        public MediaPlayer2Builder setSupportedUriSchemes(@NotNull String... supportedUriSchemes) {
            this.supportedUriSchemes = Arrays.asList(supportedUriSchemes);
            return this;
        }

        public MediaPlayer2Builder setSupportedMimeTypes(@NotNull String... supportedMimeTypes) {
            this.supportedMimeTypes = Arrays.asList(supportedMimeTypes);
            return this;
        }

        public MediaPlayer2Builder setOnRaise(@NotNull TypeRunnable<?> onRaise) {
            this.onRaise = onRaise;
            return this;
        }

        public MediaPlayer2Builder setOnQuit(@NotNull TypeRunnable<?> onQuit) {
            this.onQuit = onQuit;
            return this;
        }

        public MPRISMP2All buildAll(
                DBusConnection connection,
                PlayerBuilder playerBuilder,
                TrackListBuilder trackListBuilder,
                PlaylistsBuilder playlistsBuilder
        ) {
            if(!desktopEntry.isEmpty() && !desktopEntry.toLowerCase(Locale.ROOT).endsWith(".desktop")) {
                throw new IllegalArgumentException("Desktop entry must be an desktop entry");
            }
            if(identity.isEmpty()) {
                identity = desktopEntry.split("/")[desktopEntry.split("/").length]
                        .replace(".desktop", "");
            }
            playerBuilder.build();
            trackListBuilder.build();
            playlistsBuilder.build();
            return new MPRISMP2All(
                    connection,
                    playerBuilder,
                    trackListBuilder,
                    playlistsBuilder,
                    canQuit,
                    fullscreen,
                    canSetFullscreen,
                    canRaise,
                    identity,
                    desktopEntry,
                    supportedUriSchemes,
                    supportedMimeTypes,
                    onRaise,
                    onQuit
            );
        }

        public MPRISMP2None buildNone(
                DBusConnection connection,
                PlayerBuilder playerBuilder
        ) {
            if(!desktopEntry.isEmpty() && !desktopEntry.toLowerCase(Locale.ROOT).endsWith(".desktop")) {
                throw new IllegalArgumentException("Desktop entry must be an desktop entry");
            }
            if(identity.isEmpty()) {
                identity = desktopEntry.split("/")[desktopEntry.split("/").length]
                        .replace(".desktop", "");
            }
            playerBuilder.build();
            return new MPRISMP2None(
                    connection,
                    playerBuilder,
                    canQuit,
                    fullscreen,
                    canSetFullscreen,
                    canRaise,
                    identity,
                    desktopEntry,
                    supportedUriSchemes,
                    supportedMimeTypes,
                    onRaise,
                    onQuit
            );
        }

        public MPRISMP2WPL buildWPL(
                DBusConnection connection,
                PlayerBuilder playerBuilder,
                PlaylistsBuilder playlistsBuilder
        ) {
            if(!desktopEntry.isEmpty() && !desktopEntry.toLowerCase(Locale.ROOT).endsWith(".desktop")) {
                throw new IllegalArgumentException("Desktop entry must be an desktop entry");
            }
            if(identity.isEmpty()) {
                identity = desktopEntry.split("/")[desktopEntry.split("/").length]
                        .replace(".desktop", "");
            }
            playerBuilder.build();
            playlistsBuilder.build();
            return new MPRISMP2WPL(
                    connection,
                    playerBuilder,
                    playlistsBuilder,
                    canQuit,
                    fullscreen,
                    canSetFullscreen,
                    canRaise,
                    identity,
                    desktopEntry,
                    supportedUriSchemes,
                    supportedMimeTypes,
                    onRaise,
                    onQuit
            );
        }

        public MPRISMP2WTL buildWTL(
                DBusConnection connection,
                PlayerBuilder playerBuilder,
                TrackListBuilder trackListBuilder
        ) {
            if(!desktopEntry.isEmpty() && !desktopEntry.toLowerCase(Locale.ROOT).endsWith(".desktop")) {
                throw new IllegalArgumentException("Desktop entry must be an desktop entry");
            }
            if(identity.isEmpty()) {
                identity = desktopEntry.split("/")[desktopEntry.split("/").length]
                        .replace(".desktop", "");
            }
            playerBuilder.build();
            trackListBuilder.build();
            return new MPRISMP2WTL(
                    connection,
                    playerBuilder,
                    trackListBuilder,
                    canQuit,
                    fullscreen,
                    canSetFullscreen,
                    canRaise,
                    identity,
                    desktopEntry,
                    supportedUriSchemes,
                    supportedMimeTypes,
                    onRaise,
                    onQuit
            );
        }
    }

    public static class PlayerBuilder {
        PlaybackStatus playbackStatus;
        LoopStatus loopStatus;
        double rate;
        boolean shuffle;
        Map<String, Variant<?>> metadata;
        double volume;
        int position;
        double minimumRate;
        double maximumRate;
        boolean canGoNext;
        boolean canGoPrevious;
        boolean canPlay;
        boolean canPause;
        boolean canSeek;
        boolean canControl;
        TypeRunnable<?> onNext;
        TypeRunnable<?> onPrevious;
        TypeRunnable<?> onPause;
        TypeRunnable<?> onPlayPause;
        TypeRunnable<?> onStop;
        TypeRunnable<?> onPlay;
        TypeRunnable<Integer> onSeek;
        TypeRunnable<Map<DBusPath, Integer>> onSetPosition;
        TypeRunnable<String> onOpenURI;
        TypeRunnable<Long> onSignalSeeked;

        public PlayerBuilder() {
            playbackStatus = PlaybackStatus.STOPPED;
            loopStatus = LoopStatus.NONE;
            rate = -1;
            shuffle = false;
            metadata = null;
            volume = -1;
            position = -1;
            minimumRate = -1;
            maximumRate = -1;
            canGoNext = false;
            canGoPrevious = false;
            canPlay = false;
            canPause = false;
            canSeek = false;
            canControl = false;
            onNext = (T) -> {};
            onPrevious = (T) -> {};
            onPause = (T) -> {};
            onPlayPause = (T) -> {};
            onSeek = (T) -> {};
            onSetPosition = (T) -> {};
            onOpenURI = (T) -> {};
            onSignalSeeked = (T) -> {};
        }

        public PlayerBuilder setPlaybackStatus(@NotNull PlaybackStatus playbackStatus) {
            this.playbackStatus = playbackStatus;
            return this;
        }

        public PlayerBuilder setLoopStatus(@NotNull LoopStatus loopStatus) {
            this.loopStatus = loopStatus;
            return this;
        }

        public PlayerBuilder setRate(double rate) throws IllegalArgumentException {
            this.rate = rate;
            return this;
        }

        public PlayerBuilder setShuffle(boolean shuffle) {
            this.shuffle = shuffle;
            return this;
        }

        public PlayerBuilder setMetadata(@NotNull Metadata metadata) {
            this.metadata = metadata.getInternalMap();
            return this;
        }
        
        public PlayerBuilder setVolume(double volume) {
            this.volume = volume;
            return this;
        }

        public PlayerBuilder setPosition(int position) {
            this.position = position;
            return this;
        }

        public PlayerBuilder setMinimumRate(double minimumRate) {
            this.minimumRate = minimumRate;
            return this;
        }

        public PlayerBuilder setMaximumRate(double maximumRate) {
            this.maximumRate = maximumRate;
            return this;
        }

        public PlayerBuilder setCanGoNext(boolean canGoNext) {
            this.canGoNext = canGoNext;
            return this;
        }

        public PlayerBuilder setCanGoPrevious(boolean canGoPrevious) {
            this.canGoPrevious = canGoPrevious;
            return this;
        }

        public PlayerBuilder setCanPlay(boolean canPlay) {
            this.canPlay = canPlay;
            return this;
        }

        public PlayerBuilder setCanPause(boolean canPause) {
            this.canPause = canPause;
            return this;
        }

        public PlayerBuilder setCanSeek(boolean canSeek) {
            this.canSeek = canSeek;
            return this;
        }

        public PlayerBuilder setCanControl(boolean canControl) {
            this.canControl = canControl;
            return this;
        }

        public PlayerBuilder setOnNext(@NotNull TypeRunnable<?> onNext) {
            this.onNext = onNext;
            return this;
        }

        public PlayerBuilder setOnPrevious(@NotNull TypeRunnable<?> onPrevious) {
            this.onPrevious = onPrevious;
            return this;
        }

        public PlayerBuilder setOnPause(@NotNull TypeRunnable<?> onPause) {
            this.onPause = onPause;
            return this;
        }

        public PlayerBuilder setOnPlayPause(@NotNull TypeRunnable<?> onPlayPause) {
            this.onPlayPause = onPlayPause;
            return this;
        }

        public PlayerBuilder setOnStop(@NotNull TypeRunnable<?> onStop) {
            this.onStop = onStop;
            return this;
        }

        public PlayerBuilder setOnPlay(@NotNull TypeRunnable<?> onPlay) {
            this.onPlay = onPlay;
            return this;
        }

        public PlayerBuilder setOnSeek(@NotNull TypeRunnable<Integer> onSeek) {
            this.onSeek = onSeek;
            return this;
        }

        public PlayerBuilder setOnSetPosition(@NotNull TypeRunnable<Map<DBusPath, Integer>> onSetPosition) {
            this.onSetPosition = onSetPosition;
            return this;
        }

        public PlayerBuilder setOnOpenURI(@NotNull TypeRunnable<String> onOpenURI) {
            this.onOpenURI = onOpenURI;
            return this;
        }

        public PlayerBuilder setOnSignalSeeked(@NotNull TypeRunnable<Long> onSignalSeeked) {
            this.onSignalSeeked = onSignalSeeked;
            return this;
        }

        void build() {
            if(metadata == null) throw new IllegalArgumentException("Metadata must be set");
            if(minimumRate != -1 && maximumRate != -1) {
                if(maximumRate < minimumRate) {
                    throw new IllegalArgumentException("Maximum rate is less than minimum rate");
                }

                if(minimumRate > maximumRate) {
                    throw new IllegalArgumentException("Minimum rate is greater than maximum rate");
                }

                if(rate < minimumRate) {
                    throw new IllegalArgumentException("Minimum rate is " + minimumRate + " but got " + rate);
                }

                if(rate > maximumRate) {
                    throw new IllegalArgumentException("Maximum rate is " + maximumRate + " but got " + rate);
                }
            }else {
                if(rate == -1) rate = 1.0;
                if (rate < 0.1 || rate > 1.0) {
                    throw new IllegalArgumentException("Rate must be between 0.1 and 1.0");
                }
            }
            if(position == -1) position = 0;
            if(position < 0 || position > (Integer) metadata.get("mpris:length").getValue()) {
                throw new IllegalArgumentException("Position must be between 0 and " + (Integer) metadata.get("mpris:length").getValue());
            }
            if(volume == -1) volume = 1.0;
            if(volume < 0.0) volume = 0.0;
        }
    }

    public static class TrackListBuilder {
        List<DBusPath> tracks;
        Boolean canEditTracks;
        ReturnableTypeRunnable<List<Map<String, Variant<?>>>, List<DBusPath>> onGetTracksMetadata;
        TypeRunnable<List<Object>> onAddTrack;
        TypeRunnable<DBusPath> onRemoveTrack;
        TypeRunnable<DBusPath> onGoTo;
        TypeRunnable<TrackList.TrackListReplaced> onSignalTrackListReplaced;
        ReturnableTypeRunnable<TrackList.TrackAdded, TrackList.TrackAdded> onSignalTrackAdded;
        TypeRunnable<TrackList.TrackRemoved> onSignalTrackRemoved;
        TypeRunnable<TrackList.TrackMetadataChanged> onSignalTrackMetadataChanged;

        public TrackListBuilder() {
            tracks = null;
            canEditTracks = null;
            onGetTracksMetadata = null;
            onAddTrack = null;
            onRemoveTrack = null;
            onGoTo = null;
            onSignalTrackListReplaced = null;
            onSignalTrackAdded = null;
            onSignalTrackMetadataChanged = null;
        }

        public TrackListBuilder setTracks(@NotNull DBusPath... tracks) {
            this.tracks = Arrays.asList(tracks);
            return this;
        }

        public TrackListBuilder setCanEditTracks(boolean canEditTracks) {
            this.canEditTracks = canEditTracks;
            return this;
        }

        public TrackListBuilder setOnGetTracksMetadata(@NotNull ReturnableTypeRunnable<List<Map<String, Variant<?>>>, List<DBusPath>> onGetTracksMetadata) {
            this.onGetTracksMetadata = onGetTracksMetadata;
            return this;
        }

        public TrackListBuilder setOnAddTrack(@NotNull TypeRunnable<List<Object>> onAddTrack) {
            this.onAddTrack = onAddTrack;
            return this;
        }

        public TrackListBuilder setOnRemoveTrack(@NotNull TypeRunnable<DBusPath> onRemoveTrack) {
            this.onRemoveTrack = onRemoveTrack;
            return this;
        }

        public TrackListBuilder setOnGoTo(@NotNull TypeRunnable<DBusPath> onGoTo) {
            this.onGoTo = onGoTo;
            return this;
        }

        public TrackListBuilder setOnSignalTrackListReplaced(@NotNull TypeRunnable<TrackList.TrackListReplaced> onSignalTrackListReplaced) {
            this.onSignalTrackListReplaced = onSignalTrackListReplaced;
            return this;
        }

        public TrackListBuilder setOnSignalTrackAdded(@NotNull ReturnableTypeRunnable<TrackList.TrackAdded, TrackList.TrackAdded> onSignalTrackAdded) {
            this.onSignalTrackAdded = onSignalTrackAdded;
            return this;
        }

        public TrackListBuilder setOnSignalTrackRemoved(@NotNull TypeRunnable<TrackList.TrackRemoved> onSignalTrackRemoved) {
            this.onSignalTrackRemoved = onSignalTrackRemoved;
            return this;
        }

        public TrackListBuilder setOnSignalTrackMetadataChanged(@NotNull TypeRunnable<TrackList.TrackMetadataChanged> onTrackMetadataChanged) {
            this.onSignalTrackMetadataChanged = onTrackMetadataChanged;
            return this;
        }

        void build() {
            if(tracks == null) throw new IllegalArgumentException("tracks must be set");
            if(canEditTracks == null) throw new IllegalArgumentException("canEditTracks must be set");
            if(onGetTracksMetadata == null) throw new IllegalArgumentException("onGetTracksMetadata must be set");
            if(onAddTrack == null) throw new IllegalArgumentException("onAddTrack must be set");
            if(onRemoveTrack == null) throw new IllegalArgumentException("onRemoveTrack must be set");
            if(onGoTo == null) throw new IllegalArgumentException("onGoTo must be set");
            if(onSignalTrackListReplaced == null) throw new IllegalArgumentException("onSignalTrackListReplaced must be set");
            if(onSignalTrackAdded == null) throw new IllegalArgumentException("onSignalTrackAdded must be set");
            if(onSignalTrackRemoved == null) throw new IllegalArgumentException("onSignalTrackRemoved must be set");
            if(onSignalTrackMetadataChanged == null) throw new IllegalArgumentException("onTrackMetadataChanged must be set");
        }
    }

    public static class PlaylistsBuilder {
        Integer playlistsCount;
        List<PlaylistOrdering> orderings;
        Playlists.Maybe_Playlist activePlaylist;
        TypeRunnable<DBusPath> onActivatePlaylist;
        ReturnableTypeRunnable<List<Playlists.Playlist>, List<Object>> onGetPlaylists;
        TypeRunnable<Playlists.PlaylistChanged> onSignalPlaylistChanged;

        public PlaylistsBuilder() {
            playlistsCount = null;
            orderings = null;
            activePlaylist = null;
            onActivatePlaylist = null;
            onGetPlaylists = null;
            onSignalPlaylistChanged = null;
        }

        public PlaylistsBuilder setPlaylistsCount(@NotNull Integer playlistsCount) {
            this.playlistsCount = playlistsCount;
            return this;
        }

        public PlaylistsBuilder setOrderings(@NotNull List<PlaylistOrdering> orderings) {
            this.orderings = orderings;
            return this;
        }

        public PlaylistsBuilder setActivePlaylist(@NotNull Playlists.Maybe_Playlist activePlaylist) {
            this.activePlaylist = activePlaylist;
            return this;
        }

        public PlaylistsBuilder setOnActivatePlaylist(@NotNull TypeRunnable<DBusPath> onActivatePlaylist) {
            this.onActivatePlaylist = onActivatePlaylist;
            return this;
        }

        public PlaylistsBuilder setOnGetPlaylists(@NotNull ReturnableTypeRunnable<List<Playlists.Playlist>, List<Object>> onGetPlaylists) {
            this.onGetPlaylists = onGetPlaylists;
            return this;
        }
        
        public PlaylistsBuilder setOnSignalPlaylistChanged(@NotNull TypeRunnable<Playlists.PlaylistChanged> onSignalPlaylistChanged) {
            this.onSignalPlaylistChanged = onSignalPlaylistChanged;
            return this;
        }

        void build() {
            if(playlistsCount == null) throw new IllegalArgumentException("playlistsCount must be set");
            if(playlistsCount < 0) throw new IllegalArgumentException("PlaylistsCount is less than 0");
            if(orderings == null) throw new IllegalArgumentException("orderings must be set");
            if(orderings.isEmpty()) {
                throw new IllegalArgumentException("At least one ordering must be offered");
            }
            if(activePlaylist == null) throw new IllegalArgumentException("activePlaylist must be set");
            if(onActivatePlaylist == null) throw new IllegalArgumentException("onActivatePlaylist must be set");
            if(onGetPlaylists == null) throw new IllegalArgumentException("onGetPlaylists must be set");
            if(onSignalPlaylistChanged == null) throw new IllegalArgumentException("onSignalPlaylistChanged must be set");
        }
    }

    public void create() throws DBusException {
        switch (buildMode) {
            case ALL:
                mprisMediaPlayer2All.init();
                connection.exportObject(mprisMediaPlayer2All.getObjectPath(), mprisMediaPlayer2All);
                break;
            case WPL:
                mprisMediaPlayer2WPL.init();
                connection.exportObject(mprisMediaPlayer2WPL.getObjectPath(), mprisMediaPlayer2WPL);
                break;
            case WTL:
                mprisMediaPlayer2WTL.init();
                connection.exportObject(mprisMediaPlayer2WTL.getObjectPath(), mprisMediaPlayer2WTL);
                break;
            case NONE:
                mprisMediaPlayer2None.init();
                connection.exportObject(mprisMediaPlayer2None.getObjectPath(), mprisMediaPlayer2None);
                break;
        }
        connection.requestBusName("org.mpris.MediaPlayer2." + playerName);
    }
}
