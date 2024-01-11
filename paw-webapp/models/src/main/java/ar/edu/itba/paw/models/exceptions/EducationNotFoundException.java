package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class EducationNotFoundException extends ClonedInException {

    private static final Response.Status STATUS = Response.Status.NOT_FOUND;
    private static final String SIMPLE_MESSAGE = "Education not found";
    private static final String DETAILS = "The education with id %d was not found";

    public EducationNotFoundException(long educationId) {
        super(String.format(DETAILS, educationId));
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