package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ControllerAdvice
/*public class ErrorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);
    @ExceptionHandler({UserNotFoundException.class, CategoryNotFoundException.class, ExperienceNotFoundException.class,
                        ImageNotFoundException.class, JobOfferNotFoundException.class, SkillNotFoundException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound() {
        LOGGER.error("Error 404 - Page not found");
        return new ModelAndView("404");
    }

    @ExceptionHandler({UserIsNotProfileOwnerException.class, HiddenProfileException.class})
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ModelAndView userIsNotProfileOwner() {
        LOGGER.error("Error 403 - Forbidden");
        return new ModelAndView("403");
    }
}*/
