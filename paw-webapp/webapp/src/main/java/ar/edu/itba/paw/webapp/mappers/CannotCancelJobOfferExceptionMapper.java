package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.CannotCancelJobOfferException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class CannotCancelJobOfferExceptionMapper implements ExceptionMapper<CannotCancelJobOfferException> {

    private static final String MESSAGE = "Cannot cancel job offer";

    @Override
    public Response toResponse(CannotCancelJobOfferException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.CONFLICT).entity(errorDTO).build();
    }
}
