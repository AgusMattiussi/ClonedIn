package ar.edu.itba.paw.webapp.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {
    private static final long MAX_SIZE = 2*1024*1024;
    private static final String ACCEPTED_MIME_TYPE = "image/";


    @Override
    public void initialize(Image image) {

    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        return true;
        //return !multipartFile.isEmpty() && multipartFile.getSize() < MAX_SIZE && multipartFile.getContentType().contains(ACCEPTED_MIME_TYPE);

    }

}
