package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class EnterpriseNotFoundException extends ClonedInException{

    private static final Response.Status STATUS = Response.Status.NOT_FOUND;
    private static final String SIMPLE_MESSAGE = "Enterprise not found";
    private static final String DETAILS_WITH_ID = "The enterprise with id %s was not found or does not exist.";
    private static final String DETAILS_WITH_NAME = "The enterprise with name '%s' was not found or does not exist.";

    public EnterpriseNotFoundException(long enterpriseId) {
        super(String.format(DETAILS_WITH_ID, enterpriseId));
    }

    public EnterpriseNotFoundException(String name) {
        super(String.format(DETAILS_WITH_NAME, name));
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
