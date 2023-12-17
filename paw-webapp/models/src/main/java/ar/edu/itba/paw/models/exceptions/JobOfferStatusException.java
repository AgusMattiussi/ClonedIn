package ar.edu.itba.paw.models.exceptions;

import ar.edu.itba.paw.models.enums.JobOfferStatus;

public class JobOfferStatusException extends RuntimeException{

    private static final String STRING_MSG = "Could not change status to '%s' for job offer with id %d, including user with id %d." +
            " Maybe new status is incompatible with current status or the user is not in a valid application for the job offer.";

    private final JobOfferStatus status;

    public JobOfferStatusException(JobOfferStatus status, long jobOfferId, long userId) {
        super(String.format(STRING_MSG, status, jobOfferId, userId));
        this.status = status;
    }

    public JobOfferStatus getStatus() {
        return status;
    }
}
