package ar.edu.itba.paw.webapp.validators;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotAfterCurrentYearValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotAfterCurrentYear {
    String message() default "Year cannot be after the current year.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
