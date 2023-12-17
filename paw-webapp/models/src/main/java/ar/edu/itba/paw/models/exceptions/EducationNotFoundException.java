package ar.edu.itba.paw.models.exceptions;

public class EducationNotFoundException extends RuntimeException {

    private static final String STRING_MSG = "The education with id %d was not found";

    public EducationNotFoundException(long educationId) {
        super(String.format(STRING_MSG, educationId));
    }
}