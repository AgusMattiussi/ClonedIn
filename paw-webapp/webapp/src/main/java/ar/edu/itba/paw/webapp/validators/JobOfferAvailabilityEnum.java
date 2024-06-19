package ar.edu.itba.paw.webapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = JobOfferAvailabilityValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface JobOfferAvailabilityEnum {
    String message() default "Invalid availability. Must be one of: 'Activa', 'Cancelada' or 'Cerrada'.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
