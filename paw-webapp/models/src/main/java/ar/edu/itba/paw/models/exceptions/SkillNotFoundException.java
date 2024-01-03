package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class SkillNotFoundException extends ClonedInException{

    private static final Response.Status STATUS = Response.Status.NOT_FOUND;
    private static final String SIMPLE_MESSAGE = "Skill not found";
    private static final String DETAILS = "The skill with id %d was not found or does not exist";

    public SkillNotFoundException(long skillId) {
        super(String.format(DETAILS, skillId));
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
