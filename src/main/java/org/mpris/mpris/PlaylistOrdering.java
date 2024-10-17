package org.mpris.mpris;

/**
 * Specifies the ordering of returned playlists.
 */
public enum PlaylistOrdering {
    CreationData("Created"),
    ModifiedDate("Modified"),
    LastPlayDate("Played"),
    UserDefined("User");

    private final String asString;
    PlaylistOrdering(String asString) {
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
