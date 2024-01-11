package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class ExperienceNotFoundException extends ClonedInException{

    private static final Response.Status STATUS = Response.Status.NOT_FOUND;
    private static final String SIMPLE_MESSAGE = "Experience not found";
    private static final String DETAILS = "The experience with id %d was not found or does not exist";

    public ExperienceNotFoundException(long experienceId) {
        super(String.format(DETAILS, experienceId));
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
