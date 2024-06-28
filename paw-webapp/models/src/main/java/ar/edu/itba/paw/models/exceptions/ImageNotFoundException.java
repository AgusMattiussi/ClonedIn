package ar.edu.itba.paw.models.exceptions;

import ar.edu.itba.paw.models.enums.Role;
import javax.ws.rs.core.Response;

//TODO: no se usa??
public class ImageNotFoundException extends ClonedInException {

    private static final int STATUS = Response.Status.NOT_FOUND.getStatusCode();
    private static final String SIMPLE_MESSAGE = "Image not found";
    private static final String DETAILS = "The image for the %s with id '%d' was not found or does not exist";

    public ImageNotFoundException(long id, Role role) {
        super(String.format(DETAILS, role.getName(), id));
    }

    @Override
    public String getSimpleMessage() {
        return SIMPLE_MESSAGE;
    }

    @Override
    public int getHttpStatus() {
        return STATUS;
    }
}
