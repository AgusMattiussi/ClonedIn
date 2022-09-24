package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingEmailValidator implements ConstraintValidator<ExistingEmail, String> {
    @Autowired
    private UserService userService;
    @Autowired
    private EnterpriseService enterpriseService;

    @Override
    public void initialize(ExistingEmail existingEmail) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !userService.userExists(email) && !enterpriseService.enterpriseExists(email);
    }
}
