package org.mpris.mpris;

public enum LoopStatus {
    NONE("None"),
    TRACK("Track"),
    PLAYLIST("Playlist");

    private final String asString;
    LoopStatus(String asString) {
        this.asString = asString;
    }

    public String GetAsString() {
        return asString;
    }

    @Override
    public String toString() {
        return asString;
    }
}
