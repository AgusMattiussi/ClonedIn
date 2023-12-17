package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.AlreadyAppliedException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class AlreadyAppliedExceptionMapper implements ExceptionMapper<AlreadyAppliedException> {

    private static final String MESSAGE = "Already applied";

    @Override
    public Response toResponse(AlreadyAppliedException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.CONFLICT).entity(errorDTO).build();
    }
}
