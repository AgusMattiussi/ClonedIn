package ar.edu.itba.paw.webapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = StringMatchesValidator.class)
@Documented
public @interface StringMatches {
    String message() default "Passwords don't match";
    String string1();
    String string2();
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}