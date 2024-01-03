package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class JobOfferNotFoundException extends ClonedInException{

    private static final Response.Status STATUS = Response.Status.NOT_FOUND;
    private static final String SIMPLE_MESSAGE = "Job offer not found";
    private static final String DETAILS_WITH_ID = "The job offer with id %s was not found or does not exist.";
    private static final String DETAILS_WITH_TITLE = "The job offer with title '%s' was not found or does not exist.";

    public JobOfferNotFoundException(long jobOfferId) {
        super(String.format(DETAILS_WITH_ID, jobOfferId));
    }

    public JobOfferNotFoundException(String title) {
        super(String.format(DETAILS_WITH_TITLE, title));
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
