package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return id == image.id && Arrays.equals(bytes, image.bytes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }
}