package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.HiddenProfileException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class HiddenProfileExceptionMapper implements ExceptionMapper<HiddenProfileException> {

    private static final String MESSAGE = "Profile is hidden";

    @Override
    public Response toResponse(HiddenProfileException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(errorDTO).build();
    }

}
