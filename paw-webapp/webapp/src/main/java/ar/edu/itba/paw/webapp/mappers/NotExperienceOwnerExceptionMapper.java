package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.NotExperienceOwnerException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class NotExperienceOwnerExceptionMapper implements ExceptionMapper<NotExperienceOwnerException> {

    private static final String MESSAGE = "User is not the owner of the experience";

    @Override
    public Response toResponse(NotExperienceOwnerException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(errorDTO).build();
    }
}
