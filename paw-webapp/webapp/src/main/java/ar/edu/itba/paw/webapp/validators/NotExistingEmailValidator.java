package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotExistingEmailValidator implements ConstraintValidator<NotExistingEmail, String> {
    @Autowired
    private UserService userService;

    @Override
    public void initialize(NotExistingEmail existingEmail) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userService.userExists(email);
    }
}