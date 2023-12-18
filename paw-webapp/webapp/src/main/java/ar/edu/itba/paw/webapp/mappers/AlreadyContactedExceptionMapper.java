package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.AlreadyContactedException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AlreadyContactedExceptionMapper implements ExceptionMapper<AlreadyContactedException> {

    private static final String MESSAGE = "Already contacted";

    @Override
    public Response toResponse(AlreadyContactedException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.CONFLICT).entity(errorDTO).build();
    }
}
