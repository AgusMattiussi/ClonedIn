package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.enums.EmployeeRanges;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidEmployeeRangeValidator implements ConstraintValidator<ValidEmployeeRange, String> {
    @Override
    public void initialize(ValidEmployeeRange constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null)
            return true;

        try {
            EmployeeRanges.fromString(s);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
