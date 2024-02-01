package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class InvalidDateException extends ClonedInException {

    private static final int STATUS = Response.Status.BAD_REQUEST.getStatusCode();
    private static final String SIMPLE_MESSAGE = "Invalid Date";


    public InvalidDateException(String message) {
        super(message);
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
