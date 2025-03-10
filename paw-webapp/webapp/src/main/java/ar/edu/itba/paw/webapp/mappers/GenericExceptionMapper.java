package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception>{

    private static final String MESSAGE = "An exception occurred";

    // We decided not to show unknown exceptions to the user, since they could be a security risk
    @Override
    public Response toResponse(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, MESSAGE);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorDTO).build();
    }
}
