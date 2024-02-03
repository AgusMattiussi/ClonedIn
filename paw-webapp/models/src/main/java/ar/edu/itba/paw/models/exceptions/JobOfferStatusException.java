package ar.edu.itba.paw.models.exceptions;

import ar.edu.itba.paw.models.enums.JobOfferStatus;

import javax.ws.rs.core.Response;

public class JobOfferStatusException extends ClonedInException {

    private static final int STATUS = Response.Status.CONFLICT.getStatusCode();
    private static final String DETAILS = "Could not change status to '%s' for job offer with id %d, including user with id %d." +
            " Maybe new status is incompatible with current status or the user is not in a valid application for the job offer.";
    private static final String DETAILS_NO_USER = "Could not change status to '%s' for job offer with id %d." +
            " Maybe new status is incompatible with current status or the user is not in a valid application for the job offer.";

    private final JobOfferStatus jobOfferStatus;

    public JobOfferStatusException(JobOfferStatus jobOfferStatus, long jobOfferId, long userId) {
        super(String.format(DETAILS, jobOfferStatus, jobOfferId, userId));
        this.jobOfferStatus = jobOfferStatus;
    }

    public JobOfferStatusException(JobOfferStatus jobOfferStatus, long jobOfferId) {
        super(String.format(DETAILS_NO_USER, jobOfferStatus, jobOfferId));
        this.jobOfferStatus = jobOfferStatus;
    }


    @Override
    public String getSimpleMessage() {
        switch (jobOfferStatus) {
            case ACCEPTED:
                return "Could not accept job offer";
            case CANCELLED:
                return "Could not cancel job offer";
            case DECLINED:
                return "Could not reject job offer";
            default:
                return "Could not change job offer status";
        }
    }

    @Override
    public int getHttpStatus() {
        return STATUS;
    }
}