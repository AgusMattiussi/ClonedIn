package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    private static final String MESSAGE = "Web Application Exception";

    @Override
    public Response toResponse(WebApplicationException e) {
        ErrorDTO errorDTO = new ErrorDTO(WebApplicationException.class, MESSAGE, e.getMessage());
        int statusCode = e.getResponse().getStatus();
        return Response.status(Response.Status.fromStatusCode(statusCode)).entity(errorDTO).build();
    }
}
