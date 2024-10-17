package org.mpris;

public enum MPRISObjectPaths {
    MEDIAPLAYER2("org.mpris.MediaPlayer2"),
    PLAYER("org.mpris.MediaPlayer2.Player"),
    TRACKLIST("org.mpris.MediaPlayer2.TrackList"),
    PLAYLISTS("org.mpris.MediaPlayer2.Playlists"),;

    private final String path;
    MPRISObjectPaths(final String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}
