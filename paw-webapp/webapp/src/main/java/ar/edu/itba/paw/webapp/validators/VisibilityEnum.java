package ar.edu.itba.paw.webapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = VisibilityValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface VisibilityEnum {
    String message() default "Invalid visibility. Possible values are: 'visible' and 'invisible'.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
