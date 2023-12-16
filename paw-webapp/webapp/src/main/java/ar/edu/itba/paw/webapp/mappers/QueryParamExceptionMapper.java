package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import org.glassfish.jersey.server.ParamException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class QueryParamExceptionMapper implements ExceptionMapper<ParamException.QueryParamException> {


    private static final String MESSAGE = "Invalid query parameter";
    private static final String DESCRIPTION = "Parameter name: '%s'";

    @Override
    public Response toResponse(ParamException.QueryParamException e) {
        e.getParameterType();

        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, String.format(DESCRIPTION, e.getParameterName()));
        return Response.status(Response.Status.BAD_REQUEST).entity(errorDTO).build();
    }
}
