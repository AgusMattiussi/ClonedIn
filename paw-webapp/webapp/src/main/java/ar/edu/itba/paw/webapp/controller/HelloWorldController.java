package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;

import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class HelloWorldController {

    private UserService us;

    @Autowired
    public HelloWorldController(final UserService us){
        this.us = us;
    }

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
//        final User user = us.register("paw@itba.edu.ar", "secret");
//        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping("/register")
    public ModelAndView register(@RequestParam("email") final String email, @RequestParam("password") final String password) {
        final User user = us.register(email, password);
        return new ModelAndView("redirect:/profile/" + user.getId());
    }

    @RequestMapping("/profile/{userId:[0-9]+}")
    public ModelAndView profile(@PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", us.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/formenterprise")
    public ModelAndView formenterprise() {
        final ModelAndView mav = new ModelAndView("formenterprise");
        return mav;
    }

    @RequestMapping("/formuser")
    public ModelAndView formuser() {
        final ModelAndView mav = new ModelAndView("formuser");
        return mav;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound() {
        return new ModelAndView("404");
    }

}