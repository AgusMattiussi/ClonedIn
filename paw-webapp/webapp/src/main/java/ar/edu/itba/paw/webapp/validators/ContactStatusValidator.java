package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.enums.ContactStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactStatusValidator implements ConstraintValidator<ContactStatusEnum, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            ContactStatus.fromString(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
