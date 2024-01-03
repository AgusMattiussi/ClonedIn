package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class NotProfileOwnerException extends ClonedInException{

    private static final Response.Status STATUS = Response.Status.FORBIDDEN;
    private static final String SIMPLE_MESSAGE = "User is not the profile owner";
    private static final String DETAILS = "The user with id %d is not the owner of this profile";

    public NotProfileOwnerException(long requesterId) {
        super(String.format(DETAILS, requesterId));
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
