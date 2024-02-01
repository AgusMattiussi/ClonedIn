package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class ContactNotFoundException extends ClonedInException{

    private static final int STATUS = Response.Status.NOT_FOUND.getStatusCode();
    private static final String SIMPLE_MESSAGE = "Contact not found";
    private static final String DETAILS = "The contact of the user with id '%d' with the job offer with id '%d' was not found or does not exist";

    public ContactNotFoundException(long userId, long jobOfferId) {
        super(String.format(DETAILS, userId, jobOfferId));
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
