package ar.edu.itba.paw.models.exceptions;

public class AlreadyAppliedException extends RuntimeException{

    private static final String MESSAGE_WITH_ID = "The user with id %s has already applied to the job offer with id %s.";

    public AlreadyAppliedException(long userId, long jobOfferId) {
        super(String.format(MESSAGE_WITH_ID, userId, jobOfferId));
    }
}
