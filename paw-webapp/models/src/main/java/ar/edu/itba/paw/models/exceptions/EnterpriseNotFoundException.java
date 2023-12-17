package ar.edu.itba.paw.models.exceptions;

public class EnterpriseNotFoundException extends RuntimeException{

    private static final String MESSAGE_WITH_ID = "The enterprise with id %s was not found or does not exist.";
    private static final String MESSAGE_WITH_NAME = "The enterprise with name '%s' was not found or does not exist.";

    public EnterpriseNotFoundException(long enterpriseId) {
        super(String.format(MESSAGE_WITH_ID, enterpriseId));
    }

    public EnterpriseNotFoundException(String name) {
        super(String.format(MESSAGE_WITH_NAME, name));
    }
}
