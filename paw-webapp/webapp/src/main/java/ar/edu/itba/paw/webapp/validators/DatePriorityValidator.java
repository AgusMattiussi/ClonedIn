package ar.edu.itba.paw.webapp.validators;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DatePriorityValidator implements ConstraintValidator<DatePriority, Object> {

    private String string1;
    private String string2;
    private int int1;
    private int int2;

    @Override
    public void initialize(DatePriority constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return true;
    }
}

