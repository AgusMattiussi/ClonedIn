package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;
//TODO: no se usa??
public class NotExperienceOwnerException extends ClonedInException {

    private static final int STATUS = Response.Status.FORBIDDEN.getStatusCode();
    private static final String SIMPLE_MESSAGE = "User is not the owner of the experience";
    private static final String DETAILS = "The user with id %d is not the owner of the experience with id %d";

    public NotExperienceOwnerException(long userId, long experienceId) {
        super(String.format(DETAILS, userId, experienceId));
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
