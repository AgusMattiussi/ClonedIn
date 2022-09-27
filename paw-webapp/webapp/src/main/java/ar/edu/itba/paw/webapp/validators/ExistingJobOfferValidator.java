package ar.edu.itba.paw.webapp.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingJobOfferValidator implements ConstraintValidator<ExistingJobOffer, Long> {
    @Override
    public void initialize(ExistingJobOffer existingJobOffer) {

    }

    @Override
    public boolean isValid(Long s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
