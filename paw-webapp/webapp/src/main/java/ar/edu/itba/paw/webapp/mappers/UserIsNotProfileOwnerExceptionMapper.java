package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.UserIsNotProfileOwnerException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UserIsNotProfileOwnerExceptionMapper implements ExceptionMapper<UserIsNotProfileOwnerException> {

    private static final String MESSAGE = "Current user is not profile owner";

    @Override
    public Response toResponse(UserIsNotProfileOwnerException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(errorDTO).build();
    }
}
