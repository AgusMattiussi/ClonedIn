package ar.edu.itba.paw.models.exceptions;

public class NotExperienceOwnerException extends RuntimeException{

    private static final String STRING_MSG = "The user with id %d is not the owner of the experience with id %d";

    public NotExperienceOwnerException(long userId, long experienceId) {
        super(String.format(STRING_MSG, userId, experienceId));
    }

}
