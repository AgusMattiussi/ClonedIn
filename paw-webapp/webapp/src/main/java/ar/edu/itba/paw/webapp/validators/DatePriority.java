package ar.edu.itba.paw.webapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DatePriorityValidator.class)
@Documented
public @interface DatePriority {
    String message() default "Dates are invalid";
    String string1();
    String string2();
    int int1();
    int int2();
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}