package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.Image;
import org.springframework.web.multipart.MultipartFile;

public class ImageForm {
    @Image
    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
