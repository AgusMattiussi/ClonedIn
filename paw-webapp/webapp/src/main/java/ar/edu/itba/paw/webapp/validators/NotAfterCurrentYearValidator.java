package ar.edu.itba.paw.webapp.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

public class NotAfterCurrentYearValidator implements ConstraintValidator<NotAfterCurrentYear, Integer> {

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        if(year == null)
            return true;
        return year <= Calendar.getInstance().get(Calendar.YEAR);
    }
}
