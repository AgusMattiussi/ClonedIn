package ar.edu.itba.paw.models.exceptions;

public class SkillNotFoundException extends RuntimeException{

    private static final String STRING_MSG = "The skill with id %d was not found or does not exist";

    public SkillNotFoundException(long skillId) {
        super(String.format(STRING_MSG, skillId));
    }
}
