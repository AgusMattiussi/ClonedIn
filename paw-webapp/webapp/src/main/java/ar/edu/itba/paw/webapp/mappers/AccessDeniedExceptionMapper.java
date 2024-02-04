package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import org.springframework.security.access.AccessDeniedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    private static final String MESSAGE = "Access Denied";

    @Override
    public Response toResponse(AccessDeniedException e) {
        ErrorDTO errorDTO = new ErrorDTO(AccessDeniedException.class, MESSAGE, e.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(errorDTO).build();
    }
}
