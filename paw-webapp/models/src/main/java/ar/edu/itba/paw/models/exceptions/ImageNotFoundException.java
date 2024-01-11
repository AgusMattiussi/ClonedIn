package ar.edu.itba.paw.models.exceptions;

import ar.edu.itba.paw.models.enums.Role;

import javax.ws.rs.core.Response;

public class ImageNotFoundException extends ClonedInException {

    private static final Response.Status STATUS = Response.Status.NOT_FOUND;
    private static final String SIMPLE_MESSAGE = "Image not found";
    private static final String DETAILS = "The image for the %s with id '%d' was not found or does not exist";

    public ImageNotFoundException(long imageId, Role role) {
        super(String.format(DETAILS, role.getName(), imageId));
    }

    @Override
    public String getSimpleMessage() {
        return SIMPLE_MESSAGE;
    }

    @Override
    public Response.Status getHttpStatus() {
        return STATUS;
    }
}
