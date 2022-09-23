package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckPasswordValidator implements ConstraintValidator<CheckPassword, String> {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void initialize(CheckPassword checkPassword) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (password == null)
            return false;
        User toDelete = userService.findByEmail(auth.getName()).orElseThrow(UserNotFoundException::new);
        return passwordEncoder.matches(password, toDelete.getPassword());
    }
}
