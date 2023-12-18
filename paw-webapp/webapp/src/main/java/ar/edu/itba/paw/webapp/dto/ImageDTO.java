package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Image;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ImageDTO {

    private long imageID;
    private byte[] image;
    private URI self;

    public static ImageDTO fromImage(UriInfo uriInfo, Image image) {
        final ImageDTO dto = new ImageDTO();
        dto.imageID = image.getId();
        dto.image = image.getBytes();

        dto.self = uriInfo.getAbsolutePath();
        dto.self = uriInfo.getAbsolutePathBuilder().build();
        return dto;
    }

    public long getImageID() {
        return imageID;
    }

    public void setImageID(long imageID) {
        this.imageID = imageID;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
