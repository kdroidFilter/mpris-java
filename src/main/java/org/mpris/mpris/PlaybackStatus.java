package org.mpris.mpris;

public enum PlaybackStatus {
    PLAYING("Playing"),
    PAUSED("Paused"),
    STOPPED("Stopped");

    private final String asString;
    PlaybackStatus(String asString) {
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
