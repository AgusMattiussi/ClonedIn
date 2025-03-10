package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.enums.EducationLevel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EducationLevelValidator implements ConstraintValidator<EducationLevelEnum, String> {

    @Override
    public void initialize(EducationLevelEnum constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(s == null)
            return true;
        try {
            EducationLevel.fromString(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
