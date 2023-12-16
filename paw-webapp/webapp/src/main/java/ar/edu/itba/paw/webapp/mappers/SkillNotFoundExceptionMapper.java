package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.SkillNotFoundException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SkillNotFoundExceptionMapper implements ExceptionMapper<SkillNotFoundException> {

    private static final String MESSAGE = "Skill not found";

    @Override
    public Response toResponse(SkillNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), MESSAGE, e.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(errorDTO).build();
    }
}
