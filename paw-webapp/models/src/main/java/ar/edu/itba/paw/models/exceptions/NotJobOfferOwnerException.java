package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class NotJobOfferOwnerException extends ClonedInException {

    private static final int STATUS = Response.Status.FORBIDDEN.getStatusCode();
    private static final String SIMPLE_MESSAGE = "Enterprise is not the owner of the job offer";
    private static final String DETAILS = "The enterprise with id '%d' is not the owner of the job offer with id '%d'";

    public NotJobOfferOwnerException(long enterpriseId, long jobOfferId) {
        super(String.format(DETAILS, enterpriseId, jobOfferId));
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
