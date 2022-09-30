package ar.edu.itba.paw.models;

public class Image {
    private final long id;
    private final byte[] bytes;

    public Image(long id, byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }

    public long getId() {
        return id;
    }

    public byte[] getBytes() {
        return bytes;
    }
}