package net.craftcitizen.imagemaps.clcore;

public class SemanticVersion {
    private final int major;
    private final int minor;
    private final int revision;

    public SemanticVersion(final int major, final int minor, final int revision) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public int getRevision() {
        return this.revision;
    }

    public static SemanticVersion of(final String string) {
        final String[] arr = string.split("\\.");
        final int major = Utils.parseIntegerOrDefault((arr.length > 0) ? arr[0] : "0", 0);
        final int minor = Utils.parseIntegerOrDefault((arr.length > 1) ? arr[1] : "0", 0);
        final int revision = Utils.parseIntegerOrDefault((arr.length > 2) ? arr[2] : "0", 0);
        return new SemanticVersion(major, minor, revision);
    }
}
