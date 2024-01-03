package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class AlreadyAppliedException extends ClonedInException{

    private static final Response.Status STATUS = Response.Status.CONFLICT;
    private static final String SIMPLE_MESSAGE = "Already applied";
    private static final String DETAILS = "The user with id %s has already applied to the job offer with id %s.";

    public AlreadyAppliedException(long userId, long jobOfferId) {
        super(String.format(DETAILS, userId, jobOfferId));
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
