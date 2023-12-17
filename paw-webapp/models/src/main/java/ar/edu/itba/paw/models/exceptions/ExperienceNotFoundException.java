package ar.edu.itba.paw.models.exceptions;

public class ExperienceNotFoundException extends RuntimeException{

    private static final String STRING_MSG = "The experience with id %d was not found or does not exist";

    public ExperienceNotFoundException(long experienceId) {
        super(String.format(STRING_MSG, experienceId));
    }

    public ExperienceNotFoundException(long experienceId, Throwable var1) {
        super(String.format(STRING_MSG, experienceId),var1);
    }
}
