package ar.edu.itba.paw.models.exceptions;

public class CannotCancelJobOfferException extends RuntimeException{

    private static final String MESSAGE_WITH_ID = "The job offer with id '%s', applied by user with id '%d' cannot be cancelled. " +
            "Maybe it has already been cancelled or it was not applied to by the user.";

    public CannotCancelJobOfferException(long jobOfferId, long userId) {
        super(String.format(MESSAGE_WITH_ID, jobOfferId, userId));
    }


}
