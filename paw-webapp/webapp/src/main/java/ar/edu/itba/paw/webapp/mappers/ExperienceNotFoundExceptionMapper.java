package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.ExperienceNotFoundException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExperienceNotFoundExceptionMapper implements ExceptionMapper<ExperienceNotFoundException> {

    private static final String MESSAGE = "Experience not found";

    @Override
    public Response toResponse(ExperienceNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(errorDTO).build();
    }
}
