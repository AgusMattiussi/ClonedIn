package ar.edu.itba.paw.webapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ContactStatusValidator.class)
@Target( { ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ContactStatusEnum {
    String message() default "Invalid Contact status. Possible values are: 'pendiente', 'cerrada', 'cancelada', 'aceptada' and 'rechazada'.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
