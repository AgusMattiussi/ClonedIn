package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class NotResizableException extends ClonedInException{

    private static final int STATUS = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    private static final String SIMPLE_MESSAGE = "Image is not resizable";
    private static final String DETAILS = "Could not resize the image with id %d";

    public NotResizableException(long imageId) {
        super(String.format(DETAILS, imageId));
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
