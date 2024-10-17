package org.mpris;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusSigHandler;
import org.freedesktop.dbus.interfaces.Properties;
import org.freedesktop.dbus.types.Variant;
import org.jetbrains.annotations.NotNull;
import org.mpris.mpris.*;

import java.util.*;

@SuppressWarnings("unused")
public class MPRISMP2None implements MediaPlayer2, Player, DBusProperties {
    private boolean canQuit;
    private boolean fullscreen;
    private boolean canSetFullscreen;
    private boolean canRaise;
    private boolean hasTracklist;
    private String identity;
    private String desktopEntry;
    private List<String> supportedUriSchemes;
    private List<String> supportedMimeTypes;
    private final TypeRunnable<?> onRaise;
    private final TypeRunnable<?> onQuit;
    private final DBusConnection connection;
    private PlaybackStatus playbackStatus;
    private LoopStatus loopStatus;
    private double rate;
    private boolean shuffle;
    private Map<String, Variant<?>> metadata;
    private double volume;
    private int position;
    private double minimumRate;
    private double maximumRate;
    private boolean canGoNext;
    private boolean canGoPrevious;
    private boolean canPlay;
    private boolean canPause;
    private boolean canSeek;
    private boolean canControl;
    private final TypeRunnable<?> onNext;
    private final TypeRunnable<?> onPrevious;
    private final TypeRunnable<?> onPause;
    private final TypeRunnable<?> onPlayPause;
    private final TypeRunnable<?> onStop;
    private final TypeRunnable<?> onPlay;
    private final TypeRunnable<Integer> onSeek;
    private final TypeRunnable<Map<DBusPath, Integer>> onSetPosition;
    private final TypeRunnable<String> onOpenURI;
    private final TypeRunnable<Long> onSignalSeeked;


    MPRISMP2None(
            DBusConnection connection,
            MPRISMediaPlayer.PlayerBuilder playerBuilder,
            boolean canQuit,
            boolean fullscreen,
            boolean canSetFullscreen,
            boolean canRaise,
            String identify,
            String desktopEntry,
            List<String> supportedUriSchemes,
            List<String> supportedMimeTypes,
            TypeRunnable<?> onRaise,
            TypeRunnable<?> onQuit
    ) {
        this.connection = connection;
        this.canQuit = canQuit;
        this.fullscreen = fullscreen;
        this.canSetFullscreen = canSetFullscreen;
        this.canRaise = canRaise;
        this.hasTracklist = true;
        this.identity = identify;
        this.desktopEntry = desktopEntry;
        this.supportedUriSchemes = supportedUriSchemes;
        this.supportedMimeTypes = supportedMimeTypes;
        this.onRaise = onRaise;
        this.onQuit = onQuit;
        this.playbackStatus = playerBuilder.playbackStatus;
        this.loopStatus = playerBuilder.loopStatus;
        this.rate = playerBuilder.rate;
        if(this.rate == -1) this.rate = 1.0;
        this.shuffle = playerBuilder.shuffle;
        this.metadata = playerBuilder.metadata;
        if(this.metadata == null) throw new IllegalArgumentException("No metadata");
        this.volume = playerBuilder.volume;
        if(this.volume == -1) this.volume = 1.0;
        this.position = playerBuilder.position;
        if(this.position == -1) this.position = 0;
        this.minimumRate = playerBuilder.minimumRate;
        if(this.minimumRate == -1) this.minimumRate = 1.0;
        this.maximumRate = playerBuilder.maximumRate;
        if(this.maximumRate == -1) this.maximumRate = 1.0;
        this.canGoNext = playerBuilder.canGoNext;
        this.canGoPrevious = playerBuilder.canGoPrevious;
        this.canPlay = playerBuilder.canPlay;
        this.canPause = playerBuilder.canPause;
        this.canSeek = playerBuilder.canSeek;
        this.canControl = playerBuilder.canControl;
        this.onNext = playerBuilder.onNext;
        this.onPrevious = playerBuilder.onPrevious;
        this.onPause = playerBuilder.onPause;
        this.onPlay = playerBuilder.onPlay;
        this.onStop = playerBuilder.onStop;
        this.onPlayPause = playerBuilder.onPlayPause;
        this.onSeek = playerBuilder.onSeek;
        this.onSetPosition = playerBuilder.onSetPosition;
        this.onOpenURI = playerBuilder.onOpenURI;
        this.onSignalSeeked = playerBuilder.onSignalSeeked;
    }

    @Override
    public Variant<?> Get(String interface_name, String property_name) throws DBusException {
        switch (interface_name) {
            case "org.mpris.MediaPlayer2":
                switch (property_name) {
                    case "CanQuit":
                        return new Variant<>(canQuit, "b");
                    case "Fullscreen":
                        return new Variant<>(fullscreen, "b");
                    case  "CanSetFullscreen":
                        return new Variant<>(canSetFullscreen, "b");
                    case "CanRaise":
                        return new Variant<>(canRaise, "b");
                    case "HasTracklist":
                        return new Variant<>(hasTracklist, "b");
                    case "Identity":
                        return new Variant<>(identity, "s");
                    case "DesktopEntry":
                        return new Variant<>(desktopEntry, "s");
                    case "SupportedUriSchemes":
                        return new Variant<>(supportedUriSchemes, "as");
                    case "SupportedMimeTypes":
                        return new Variant<>(supportedMimeTypes, "as");
                }
                break;
            case "org.mpris.MediaPlayer2.Player":
                switch (property_name) {
                    case "PlaybackStatus":
                        return new Variant<>(playbackStatus.GetAsString(), "s");
                    case "LoopStatus":
                        return new Variant<>(loopStatus.GetAsString(), "s");
                    case "Rate":
                        return new Variant<>(rate, "d");
                    case "Shuffle":
                        return new Variant<>(shuffle, "b");
                    case "Metadata":
                        return new Variant<>(metadata, "a{sv}");
                    case "Volume":
                        return new Variant<>(volume, "d");
                    case "Position":
                        return new Variant<>(position, "x");
                    case "MinimumRate":
                        return new Variant<>(minimumRate, "d");
                    case "MaximumRate":
                        return new Variant<>(maximumRate, "d");
                    case "CanGoNext":
                        return new Variant<>(canGoNext, "b");
                    case "CanGoPrevious":
                        return new Variant<>(canGoPrevious, "b");
                    case "CanPlay":
                        return new Variant<>(canPlay, "b");
                    case "CanPause":
                        return new Variant<>(canPause, "b");
                    case "CanSeek":
                        return new Variant<>(canSeek, "b");
                    case "CanControl":
                        return new Variant<>(canControl, "b");
                }
                break;
        }
        return new Variant<>("");
    }

    @Override
    public Map<String, Variant<?>> GetAll(String interface_name) {
        Map<String, Variant<?>> map = new HashMap<>();
        switch (interface_name) {
            case "org.mpris.MediaPlayer2":
                map.put("CanQuit", new Variant<>(canQuit, "b"));
                map.put("Fullscreen", new Variant<>(fullscreen, "b"));
                map.put("CanSetFullscreen", new Variant<>(canSetFullscreen, "b"));
                map.put("CanRaise", new Variant<>(canRaise, "b"));
                map.put("HasTrackList", new Variant<>(hasTracklist, "b"));
                map.put("Identity", new Variant<>(identity, "s"));
                map.put("DesktopEntry", new Variant<>(desktopEntry, "s"));
                map.put("SupportedUriSchemes", new Variant<>(supportedUriSchemes, "as"));
                map.put("SupportedMimeTypes", new Variant<>(supportedMimeTypes, "as"));
                break;
            case "org.mpris.MediaPlayer2.Player":
                map.put("PlaybackStatus", new Variant<>(playbackStatus.GetAsString(), "s"));
                map.put("LoopStatus", new Variant<>(loopStatus.GetAsString(), "s"));
                map.put("Rate", new Variant<>(rate, "d"));
                map.put("Shuffle", new Variant<>(shuffle, "b"));
                map.put("Metadata", new Variant<>(metadata, "a{sv}"));
                map.put("Volume", new Variant<>(volume, "d"));
                map.put("Position", new Variant<>(position, "x"));
                map.put("MinimumRate", new Variant<>(minimumRate, "d"));
                map.put("MaximumRate", new Variant<>(maximumRate, "d"));
                map.put("CanGoNext", new Variant<>(canGoNext, "b"));
                map.put("CanGoPrevious", new Variant<>(canGoPrevious, "b"));
                map.put("CanPlay", new Variant<>(canPlay, "b"));
                map.put("CanPause", new Variant<>(canPause, "b"));
                map.put("CanSeek", new Variant<>(canSeek, "b"));
                map.put("CanControl", new Variant<>(canControl, "b"));
                break;
        }
        return map;
    }

    @Override
    public void Set(String interface_name, String property_name, Variant<?> value) throws DBusException {
        switch (interface_name) {
            case "org.mpris.MediaPlayer2":
                switch (property_name) {
                    case "CanQuit":
                        canQuit = (Boolean) value.getValue();
                        update("CanQuit", new Variant<>(canRaise, "b"), MPRISObjectPaths.MEDIAPLAYER2);
                        break;
                    case "Fullscreen":
                        fullscreen = (Boolean) value.getValue();
                        update("Fullscreen", new Variant<>(fullscreen, "b"), MPRISObjectPaths.MEDIAPLAYER2);
                        break;
                    case "CanSetFullscreen":
                        canSetFullscreen = (Boolean) value.getValue();
                        update("CanSetFullscreen", new Variant<>(canSetFullscreen, "b"), MPRISObjectPaths.MEDIAPLAYER2);
                        break;
                    case "CanRaise":
                        canRaise = (Boolean) value.getValue();
                        update("CanRaise", new Variant<>(canRaise, "b"), MPRISObjectPaths.MEDIAPLAYER2);
                        break;
                    case "HasTrackList":
                        hasTracklist = (Boolean) value.getValue();
                        update("HasTrackList", new Variant<>(hasTracklist, "b"), MPRISObjectPaths.MEDIAPLAYER2);
                        break;
                    case "Identity":
                        identity = (String) value.getValue();
                        update("Identity", new Variant<>(identity, "s"), MPRISObjectPaths.MEDIAPLAYER2);
                        break;
                    case "DesktopEntry":
                        desktopEntry = (String) value.getValue();
                        update("DesktopEntry", new Variant<>(desktopEntry, "s"), MPRISObjectPaths.MEDIAPLAYER2);
                        break;
                    case "SupportedUriSchemes":
                        supportedUriSchemes = (List<String>) value.getValue();
                        update("SupportedUriSchemes", new Variant<>(supportedUriSchemes, "as"), MPRISObjectPaths.MEDIAPLAYER2);
                        break;
                    case "SupportedMimeTypes":
                        supportedMimeTypes = (List<String>) value.getValue();
                        update("SupportedMimeTypes", new Variant<>(supportedMimeTypes, "as"), MPRISObjectPaths.MEDIAPLAYER2);
                        break;
                }
            case "org.mpris.MediaPlayer2.Player":
                switch (property_name) {
                    case "PlaybackStatus":
                        playbackStatus = PlaybackStatus.valueOf((String) value.getValue());
                        update("PlaybackStatus", new Variant<>(playbackStatus.GetAsString(), "s"), MPRISObjectPaths.PLAYER);
                        break;
                    case "LoopStatus":
                        loopStatus = LoopStatus.valueOf((String) value.getValue());
                        update("LoopStatus", new Variant<>(loopStatus.GetAsString(), "s"), MPRISObjectPaths.PLAYER);
                        break;
                    case "Rate":
                        rate = (Double) value.getValue();
                        update("Rate", new Variant<>(rate, "d"), MPRISObjectPaths.PLAYER);
                        break;
                    case "Shuffle":
                        shuffle = (Boolean) value.getValue();
                        update("Shuffle", new Variant<>(shuffle, "b"), MPRISObjectPaths.PLAYER);
                        break;
                    case "Metadata":
                        metadata = (Map<String, Variant<?>>) value.getValue();
                        update("Metadata", new Variant<>(metadata, "a{sv}"), MPRISObjectPaths.PLAYER);
                        break;
                    case "Volume":
                        volume = (Double) value.getValue();
                        update("Volume", new Variant<>(volume, "d"), MPRISObjectPaths.PLAYER);
                        break;
                    case "Position":
                        position = (Integer) value.getValue();
                        update("Position", new Variant<>(position, "x"), MPRISObjectPaths.PLAYER);
                        break;
                    case "MinimumRate":
                        minimumRate = (Double) value.getValue();
                        update("MinimumRate", new Variant<>(minimumRate, "d"), MPRISObjectPaths.PLAYER);
                        break;
                    case "MaximumRate":
                        maximumRate = (Double) value.getValue();
                        update("MaximumRate", new Variant<>(maximumRate, "d"), MPRISObjectPaths.PLAYER);
                        break;
                    case "CanGoNext":
                        canGoNext = (Boolean) value.getValue();
                        update("CanGoNext", new Variant<>(canGoNext, "b"), MPRISObjectPaths.PLAYER);
                        break;
                    case "CanGoPrevious":
                        canGoPrevious = (Boolean) value.getValue();
                        update("CanGoPrevious", new Variant<>(canGoPrevious, "b"), MPRISObjectPaths.PLAYER);
                        break;
                    case "CanPlay":
                        canPlay = (Boolean) value.getValue();
                        update("CanPlay", new Variant<>(canPlay, "b"), MPRISObjectPaths.PLAYER);
                        break;
                    case "CanPause":
                        canPause = (Boolean) value.getValue();
                        update("CanPause", new Variant<>(canPause, "b"), MPRISObjectPaths.PLAYER);
                        break;
                    case "CanSeek":
                        canSeek = (Boolean) value.getValue();
                        update("CanSeek", new Variant<>(canSeek, "b"), MPRISObjectPaths.PLAYER);
                        break;
                    case "CanControl":
                        canControl = (Boolean) value.getValue();
                        update("CanControl", new Variant<>(canControl, "b"), MPRISObjectPaths.PLAYER);
                        break;
                }
        }
    }

    void update(String propName, Variant value, MPRISObjectPaths objectPaths) throws DBusException {
        Map<String, Variant<?>> changedProps = new HashMap<>();
        changedProps.put(propName, value);
        Properties.PropertiesChanged changed = new Properties.PropertiesChanged(
                getObjectPath(),
                objectPaths.getPath(),
                changedProps,
                Collections.emptyList()
        );
        connection.sendMessage(changed);
    }

    public boolean getCanQuit() {
        return canQuit;
    }

    public void setCanQuit(boolean canQuit) throws DBusException {
        this.canQuit = canQuit;
        update("CanQuit", new Variant<>(canQuit, "b"), MPRISObjectPaths.MEDIAPLAYER2);
    }

    public boolean getFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) throws DBusException {
        this.fullscreen = fullscreen;
        update("Fullscreen", new Variant<>(fullscreen, "b"), MPRISObjectPaths.MEDIAPLAYER2);
    }

    public boolean getCanSetFullscreen() {
        return canSetFullscreen;
    }

    public void setCanSetFullscreen(boolean canSetFullscreen) throws DBusException {
        this.canSetFullscreen = canSetFullscreen;
        update("CanSetFullscreen", new Variant<>(canSetFullscreen, "b"), MPRISObjectPaths.MEDIAPLAYER2);
    }

    public boolean getCanRaise() {
        return canRaise;
    }

    public void setCanRaise(boolean canRaise) throws DBusException {
        this.canRaise = canRaise;
        update("CanRaise", new Variant<>(canRaise, "b"), MPRISObjectPaths.MEDIAPLAYER2);
    }

    public boolean getHasTracklist() {
        return hasTracklist;
    }

    public void setHasTrackList(boolean hasTracklist) throws DBusException {
        this.hasTracklist = hasTracklist;
        update("HasTrackList", new Variant<>(hasTracklist, "b"), MPRISObjectPaths.MEDIAPLAYER2);
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(@NotNull String identity) throws DBusException {
        this.identity = identity;
        update("Identity", new Variant<>(identity, "s"), MPRISObjectPaths.MEDIAPLAYER2);
    }

    public String getDesktopEntry() {
        return desktopEntry;
    }

    public void setDesktopEntry(@NotNull String desktopEntry) throws DBusException, IllegalArgumentException {
        if(!desktopEntry.isEmpty() && !desktopEntry.toLowerCase(Locale.ROOT).endsWith(".desktop")) {
            throw new IllegalArgumentException("Desktop entry must be an desktop entry");
        }
        this.desktopEntry = desktopEntry;
        update("DesktopEntry", new Variant<>(desktopEntry, "s"), MPRISObjectPaths.MEDIAPLAYER2);
    }

    public List<String> getSupportedUriSchemes() {
        return supportedUriSchemes;
    }

    public void setSupportedUriSchemes(@NotNull String... supportedUriSchemes) throws DBusException {
        this.supportedUriSchemes = Arrays.asList(supportedUriSchemes);
        update("SupportedUriSchemes", new Variant<>(supportedUriSchemes, "as"), MPRISObjectPaths.MEDIAPLAYER2);
    }

    public List<String> getSupportedMimeTypes() {
        return supportedMimeTypes;
    }

    public void setSupportedMimeTypes(@NotNull List<String> supportedMimeTypes) throws DBusException {
        this.supportedMimeTypes = supportedMimeTypes;
        update("SupportedMimeTypes", new Variant<>(supportedMimeTypes, "as"), MPRISObjectPaths.MEDIAPLAYER2);
    }

    @Override
    public void Raise() {
        if(canRaise) onRaise.run(null);
    }

    @Override
    public void Quit() {
        if(canQuit) onQuit.run(null);
    }

    public PlaybackStatus getPlaybackStatus() {
        return playbackStatus;
    }

    public void setPlaybackStatus(@NotNull PlaybackStatus playbackStatus) throws DBusException {
        this.playbackStatus = playbackStatus;
        update("PlaybackStatus", new Variant<>(playbackStatus.GetAsString(), "s"), MPRISObjectPaths.PLAYER);
    }

    public LoopStatus getLoopStatus() {
        return loopStatus;
    }

    public void setLoopStatus(@NotNull LoopStatus loopStatus) throws DBusException, IllegalArgumentException {
        if(!canControl) throw new IllegalArgumentException("CanControl is false");
        this.loopStatus = loopStatus;
        update("LoopStatus", new Variant<>(loopStatus.GetAsString(), "s"), MPRISObjectPaths.PLAYER);
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) throws DBusException, IllegalArgumentException {
        if(rate == 0) {
            setPlaybackStatus(PlaybackStatus.PAUSED);
            onPause.run(null);
            return;
        }
        if(minimumRate != -1 && maximumRate != -1) {
            if(rate < minimumRate) {
                throw new IllegalArgumentException("Minimum rate is " + minimumRate + " but got " + rate);
            }

            if(rate > maximumRate) {
                throw new IllegalArgumentException("Maximum rate is " + maximumRate + " but got " + rate);
            }
        }else{
            throw new IllegalArgumentException("Setting the rate is not supported by this player");
        }
        this.rate = rate;
        update("Rate", new Variant<>(rate, "d"), MPRISObjectPaths.PLAYER);
    }

    public boolean getShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) throws DBusException, IllegalArgumentException {
        if(!canControl) throw new IllegalArgumentException("CanControl is false");
        this.shuffle = shuffle;
        update("Shuffle", new Variant<>(shuffle, "b"), MPRISObjectPaths.PLAYER);
    }

    public Metadata getMetadata() {
        return new Metadata(metadata);
    }

    public void setMetadata(@NotNull Metadata metadata) throws DBusException {
        this.metadata = metadata.getInternalMap();
        update("Metadata", new Variant<>(metadata.getInternalMap(), "a{sv}"), MPRISObjectPaths.PLAYER);
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) throws DBusException, IllegalArgumentException {
        if(!canControl) throw new IllegalArgumentException("CanControl is false");
        if(volume < 0.0) volume = 0.0;
        this.volume = volume;
        update("Volume", new Variant<>(volume, "d"), MPRISObjectPaths.PLAYER);
    }

    public int getPosition() {
        return position;
    }

    public double getMinimumRate() {
        return minimumRate;
    }

    public void setMinimumRate(double minimumRate) throws DBusException, IllegalArgumentException {
        if(minimumRate < 0.1) {
            throw new IllegalArgumentException("Minimum rate is less than 0.1");
        }
        if(minimumRate > maximumRate) {
            throw new IllegalArgumentException("Minimum rate is greater than maximum rate");
        }
        this.minimumRate = minimumRate;
        update("MinimumRate", new Variant<>(minimumRate, "d"), MPRISObjectPaths.PLAYER);
    }

    public double getMaximumRate() {
        return maximumRate;
    }

    public void setMaximumRate(double maximumRate) throws DBusException {
        if(maximumRate < minimumRate) {
            throw new IllegalArgumentException("Maximum rate is less than minimum rate");
        }
        this.maximumRate = maximumRate;
        update("MaximumRate", new Variant<>(maximumRate, "d"), MPRISObjectPaths.PLAYER);
    }

    public boolean getCanGoNext() {
        if(!canControl) return false;
        return canGoNext;
    }

    public void setCanGoNext(boolean canGoNext) throws DBusException, IllegalArgumentException {
        if(!canControl) throw new IllegalArgumentException("CanControl is false");
        this.canGoNext = canGoNext;
        update("CanGoNext", new Variant<>(canGoNext, "b"), MPRISObjectPaths.PLAYER);
    }

    public boolean getCanGoPrevious() {
        if(!canControl) return false;
        return canGoPrevious;
    }

    public void setCanGoPrevious(boolean canGoPrevious) throws DBusException, IllegalArgumentException {
        if(!canControl) throw new IllegalArgumentException("CanControl is false");
        this.canGoPrevious = canGoPrevious;
        update("CanGoPrevious", new Variant<>(canGoPrevious, "b"), MPRISObjectPaths.PLAYER);
    }

    public boolean getCanPlay() {
        if(!canControl) return false;
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) throws DBusException, IllegalArgumentException {
        if(!canControl) throw new IllegalArgumentException("CanControl is false");
        this.canPlay = canPlay;
        update("CanPlay", new Variant<>(canPlay, "b"), MPRISObjectPaths.PLAYER);
    }

    public boolean getCanPause() {
        if(!canControl) return false;
        return canPause;
    }

    public void setCanPause(boolean canPause) throws DBusException, IllegalArgumentException {
        if(!canControl) throw new IllegalArgumentException("CanControl is false");
        this.canPause = canPause;
        update("CanPause", new Variant<>(canPause, "b"), MPRISObjectPaths.PLAYER);
    }

    public boolean getCanSeek() {
        if(!canControl) return false;
        return canSeek;
    }

    public void setCanSeek(boolean canSeek) throws DBusException, IllegalArgumentException {
        if(!canControl) throw new IllegalArgumentException("CanControl is false");
        this.canSeek = canSeek;
        update("CanSeek", new Variant<>(canSeek, "b"), MPRISObjectPaths.PLAYER);
    }

    public boolean getCanControl() {
        return canControl;
    }

    @Override
    public void Next() {
        if(canControl && canGoNext) onNext.run(null);
    }

    @Override
    public void Previous() {
        if(canControl && canGoPrevious) onPrevious.run(null);
    }

    @Override
    public void Pause() {
        if(canControl && canPause) onPause.run(null);
    }

    @Override
    public void PlayPause() {
        if(canControl && canPlay && canPause) onPlayPause.run(null);
    }

    @Override
    public void Stop() {
        if(canControl) onStop.run(null);
    }

    @Override
    public void Play() {
        if(canControl && canPlay) onPlay.run(null);
    }

    @Override
    public void Seek(int x) {
        if(canControl && canSeek) onSeek.run(x);
    }

    @Override
    public void SetPosition(DBusPath Track_Id, int x) {
        if(canControl && canSeek) onSetPosition.run(new HashMap<DBusPath, Integer>() {{
            put(Track_Id, x);
        }});
    }

    @Override
    public void OpenURI(String Uri) {
    }

    @Override
    public String getObjectPath() {
        return "/org/mpris/MediaPlayer2";
    }

    public void init() throws DBusException {
        connection.addSigHandler(Seeked.class, new DBusSigHandler<Seeked>() {
            @Override
            public void handle(Seeked s) {
                if(canControl && canSeek) {
                    position = position + Math.toIntExact(s.getTimeInUs());
                    onSignalSeeked.run(s.getTimeInUs());
                }
            }
        });
    }
}
