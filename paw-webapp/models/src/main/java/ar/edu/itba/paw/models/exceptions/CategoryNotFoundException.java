package ar.edu.itba.paw.models.exceptions;

public class CategoryNotFoundException extends RuntimeException{

    private static final String MESSAGE_WITH_NAME = "The category with name '%s' was not found or does not exist.";
    private static final String MESSAGE_WITH_ID = "The category with id %s was not found or does not exist.";

    public CategoryNotFoundException(String categoryName) {
        super(String.format(MESSAGE_WITH_NAME, categoryName));
    }

    public CategoryNotFoundException(long categoryId) {
        super(String.format(MESSAGE_WITH_ID, categoryId));
    }


}
