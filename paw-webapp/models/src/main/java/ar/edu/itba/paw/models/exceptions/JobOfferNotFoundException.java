package ar.edu.itba.paw.models.exceptions;

public class JobOfferNotFoundException extends RuntimeException{

    private static final String MESSAGE_WITH_ID = "The job offer with id %s was not found or does not exist.";
    private static final String MESSAGE_WITH_TITLE = "The job offer with title '%s' was not found or does not exist.";

    public JobOfferNotFoundException(long jobOfferId) {
        super(String.format(MESSAGE_WITH_ID, jobOfferId));
    }

    public JobOfferNotFoundException(String title) {
        super(String.format(MESSAGE_WITH_TITLE, title));
    }
}
