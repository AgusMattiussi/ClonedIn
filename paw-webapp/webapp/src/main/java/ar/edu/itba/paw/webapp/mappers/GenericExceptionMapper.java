package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import io.jsonwebtoken.MalformedJwtException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


public class GenericExceptionMapper implements ExceptionMapper<Exception>{

    private static final String MESSAGE = "An exception occurred";

    // TODO: Me parece que no deberiamos mostrar e.getMessage() para excepciones no conocidas
    @Override
    public Response toResponse(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorDTO).build();
    }
}
