package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.ClonedInException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ClonedInExceptionMapper implements ExceptionMapper<ClonedInException> {

    @Override
    public Response toResponse(ClonedInException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), e.getSimpleMessage(), e.getDetails());
        return Response.status(e.getHttpStatus()).entity(errorDTO).build();
    }

}
