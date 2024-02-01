package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import io.jsonwebtoken.ExpiredJwtException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExpiredJwtExceptionFilter implements ExceptionMapper<ExpiredJwtException> {

    private static final String MESSAGE = "Access Token is expired and Refresh Token cookie is not valid";

    @Override
    public Response toResponse(ExpiredJwtException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorDTO).build();
    }
}
