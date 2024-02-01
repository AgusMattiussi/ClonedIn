package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import io.jsonwebtoken.JwtException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JwtExceptionMapper implements ExceptionMapper<JwtException> {

    private static final String MESSAGE = "JWT Authentication failed";

    @Override
    public Response toResponse(JwtException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorDTO).build();
    }
}
