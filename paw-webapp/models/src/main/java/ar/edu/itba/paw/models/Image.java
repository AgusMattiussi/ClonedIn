package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "imagen")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imagen_id_seq")
    @SequenceGenerator(sequenceName = "imagen_id_seq", name = "imagen_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "bytes")
    private byte[] bytes;

    public Image(Long id, byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }

    public Image(byte[] bytes) {
        this(null, bytes);
    }

    /* package */ Image() {
        //Just for Hibernate, we love you!
    }

    public Long getId() {
        return id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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