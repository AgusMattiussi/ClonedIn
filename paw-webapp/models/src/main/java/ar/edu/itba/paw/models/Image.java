package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.exceptions.NotResizableException;

import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.ws.rs.core.EntityTag;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "imagen")
public class Image {

    public static final int IMAGE_MAX_SIZE_BYTES = 2 * 1024 * 1024; // 2MB

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

    public String getMimeType() throws IOException {
        ByteArrayInputStream imageBytes = new ByteArrayInputStream(bytes);
        return URLConnection.guessContentTypeFromStream(imageBytes);
    }

    public EntityTag getEntityTag() {
        return new EntityTag(String.valueOf(id));
    }

    public BufferedImage getResized(int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(this.bytes));
            java.awt.Image resultingImage = originalImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.SCALE_SMOOTH);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
            return outputImage;
        } catch (IOException e) {
            throw new NotResizableException(this.id);
        }
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
        return id.equals(image.id) && Arrays.equals(bytes, image.bytes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Image{");
        sb.append("id=").append(id);
        sb.append(", bytes=").append(Arrays.toString(bytes));
        sb.append('}');
        return sb.toString();
    }

}