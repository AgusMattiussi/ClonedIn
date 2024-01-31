package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class UserNotFoundException extends ClonedInException{

    private static final int STATUS = Response.Status.NOT_FOUND.getStatusCode();
    private static final String SIMPLE_MESSAGE = "User not found";
    private static final String DETAILS_WITH_ID = "The user with id %s was not found or does not exist.";
    private static final String DETAILS_WITH_EMAIL = "The user with email '%s' was not found or does not exist.";

    public UserNotFoundException(long userId) {
        super(String.format(DETAILS_WITH_ID, userId));
    }

    public UserNotFoundException(String email) {
        super(String.format(DETAILS_WITH_EMAIL, email));
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
