package ar.edu.itba.paw.webapp.validators;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EducationLevelValidator.class)
@Target( { ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EducationLevelEnum {
    String message() default "Invalid Education Level. Possible values are: 'Primario', 'Secundario', 'Terciario', 'Graduado' and 'Posgrado'.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}