package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.enums.Visibility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VisibilityValidator implements ConstraintValidator<VisibilityEnum, String> {
    @Override
    public void initialize(VisibilityEnum constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null)
            return true;
        try {
            Visibility.fromString(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
