package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.UserIsNotProfileOwnerException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound() {
        return new ModelAndView("404");
    }

    @ExceptionHandler(UserIsNotProfileOwnerException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ModelAndView userIsNotProfileOwner() {
        return new ModelAndView("403");
    }
}
