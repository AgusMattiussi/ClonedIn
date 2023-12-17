package ar.edu.itba.paw.models.exceptions;

public class UserNotFoundException extends RuntimeException{

    private static final String MESSAGE_WITH_ID = "The user with id %s was not found or does not exist.";
    private static final String MESSAGE_WITH_EMAIL = "The user with email '%s' was not found or does not exist.";

    public UserNotFoundException(long userId) {
        super(String.format(MESSAGE_WITH_ID, userId));
    }

    public UserNotFoundException(String email) {
        super(String.format(MESSAGE_WITH_ID, email));
    }
}
