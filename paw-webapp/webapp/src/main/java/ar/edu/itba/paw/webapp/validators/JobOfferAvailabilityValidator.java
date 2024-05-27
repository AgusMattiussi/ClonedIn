package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.enums.JobOfferAvailability;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JobOfferAvailabilityValidator implements ConstraintValidator<JobOfferAvailabilityEnum, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            JobOfferAvailability.fromString(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
