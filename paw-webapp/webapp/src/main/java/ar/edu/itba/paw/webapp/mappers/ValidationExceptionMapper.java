package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ErrorDTO;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final String MESSAGE = "Invalid parameter: %s = %s.";
            //cv.getInvalidValue().toString()

    @Override
    public Response toResponse (final ConstraintViolationException exception) {
        final List<ErrorDTO> errors = exception.getConstraintViolations().stream()
                .map(cv -> {
                    String propertyPathString = cv.getPropertyPath() != null ? cv.getPropertyPath().toString()
                            .split("\\.")[1] : null; // Removes the method name from the path
                    String invalidValueString = cv.getInvalidValue() != null ? cv.getInvalidValue().toString() : null;

                    return new ErrorDTO(ConstraintViolationException.class,
                            String.format(MESSAGE, propertyPathString, invalidValueString),
                            cv.getMessage());
                })
                .collect(Collectors.toList());
        return Response.status(Response.Status.BAD_REQUEST).entity(new GenericEntity<List<ErrorDTO>>(errors) {}).build();
    }
}
