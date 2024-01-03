package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public class CategoryNotFoundException extends ClonedInException{

    private static final Response.Status STATUS = Response.Status.NOT_FOUND;
    private static final String SIMPLE_MESSAGE = "Category not found";
    private static final String DETAILS_WITH_NAME = "The category with name '%s' was not found or does not exist.";
    private static final String DETAILS_WITH_ID = "The category with id %s was not found or does not exist.";

    public CategoryNotFoundException(String categoryName) {
        super(String.format(DETAILS_WITH_NAME, categoryName));
    }

    public CategoryNotFoundException(long categoryId) {
        super(String.format(DETAILS_WITH_ID, categoryId));
    }

    @Override
    public String getSimpleMessage() {
        return SIMPLE_MESSAGE;
    }

    @Override
    public Response.Status getHttpStatus() {
        return STATUS;
    }
}
