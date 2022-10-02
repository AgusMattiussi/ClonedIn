package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.services.UserSkillService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingSkillValidator implements ConstraintValidator<ExistingSkill, String> {
    @Autowired
    private UserSkillService userSkillService;

    @Override
    public void initialize(ExistingSkill existingEmail) {

    }

    @Override
    public boolean isValid(String skill, ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}
