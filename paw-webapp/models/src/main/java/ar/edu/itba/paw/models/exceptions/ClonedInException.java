package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class ClonedInException extends RuntimeException implements ClonedInMappeableException{

    private static final int STATUS = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    private static final String SIMPLE_MESSAGE = "ClonedInException";

    protected ClonedInException(String message) {
        super(message);
    }

    @Override
    public String getSimpleMessage() {
        return SIMPLE_MESSAGE;
    }

    @Override
    public String getDetails() {
        return super.getMessage();
    }

    @Override
    public int getHttpStatus() {
        return STATUS;
    }
}
