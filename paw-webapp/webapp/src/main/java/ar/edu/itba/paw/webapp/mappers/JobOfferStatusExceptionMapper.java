package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.JobOfferStatusException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JobOfferStatusExceptionMapper implements ExceptionMapper<JobOfferStatusException> {

    @Override
    public Response toResponse(JobOfferStatusException e) {
        String message;
        switch (e.getStatus()) {
            case ACCEPTED:
                message = "Could not accept job offer";
                break;
            case CANCELLED:
                message = "Could not cancel job offer";
                break;
            case DECLINED:
                message = "Could not reject job offer";
                break;
            default:
                message = "Could not change job offer status";
                break;
        }

        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), message, e.getMessage());
        return Response.status(Response.Status.CONFLICT).entity(errorDTO).build();
    }
}
