package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class HiddenProfileException extends ClonedInException{

    private static final Response.Status STATUS = Response.Status.FORBIDDEN;
    private static final String SIMPLE_MESSAGE = "Profile is hidden";
    private static final String DETAILS = "User '%s' has hidden their profile";

    public HiddenProfileException(String name) {
        super(String.format(DETAILS, name));
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
