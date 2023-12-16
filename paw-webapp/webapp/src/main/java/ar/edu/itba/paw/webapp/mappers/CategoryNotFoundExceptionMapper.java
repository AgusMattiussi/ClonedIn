package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CategoryNotFoundExceptionMapper implements ExceptionMapper<CategoryNotFoundException> {

    private static final String MESSAGE = "Category not found";

    @Override
    public Response toResponse(CategoryNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO(CategoryNotFoundException.class, MESSAGE, e.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(errorDTO).build();
    }
}
