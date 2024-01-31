package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class AlreadyContactedException extends ClonedInException {

    private static final int STATUS = Response.Status.CONFLICT.getStatusCode();
    private static final String SIMPLE_MESSAGE = "Already contacted";
    private static final String DETAILS = "The user with id %s has already been contacted for the job offer with id %s.";

    public AlreadyContactedException(long userId, long jobOfferId) {
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
