package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;

import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.CompanyForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class WebController {

    private UserService us;
    private EnterpriseService es;

    @Autowired
    public WebController(final UserService us){
        this.us = us;
    }

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
//        final User user = us.register("paw@itba.edu.ar", "secret");
//        mav.addObject("user", user);
         mav.addObject("users", us.getAllUsers());
        return mav;
    }

    @RequestMapping("/register")
    public ModelAndView register(@RequestParam("email") final String email, @RequestParam("password") final String password, @RequestParam("name") final String name, @RequestParam("category") final String category) {
        final User user = us.register(email, password, name, null, category, null, null, null);
        return new ModelAndView("redirect:/profile/" + user.getId());
    }

    @RequestMapping("/profile/{userId:[0-9]+}")
    public ModelAndView profile(@PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", us.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/formenterprise")
    public ModelAndView formenterprise(@ModelAttribute("companyForm") final CompanyForm form) {
        final ModelAndView mav = new ModelAndView("formenterprise");
        return mav;
    }

    @RequestMapping("/formuser")
    public ModelAndView formuser(@ModelAttribute("userForm") final UserForm form) {
        final ModelAndView mav = new ModelAndView("formuser");
        return mav;
    }

    @RequestMapping(value = "/createUser", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return formuser(form);
        }
        final User u = us.register(form.getEmail(), form.getPassword(), form.getName(), form.getCity(), "Alguna Categoria", form.getJob(), form.getDesc(), form.getCollege());
        return new ModelAndView("redirect:/user?userId=" + u.getId());
    }

    @RequestMapping(value = "/createEnterprise", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("companyForm") final CompanyForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return formenterprise(form);
        }
        final Enterprise e = es.create(form.getCemail(), form.getCname(), form.getCpassword(), form.getCcity(), 0, form.getCdesc());
        return new ModelAndView("redirect:/");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound() {
        return new ModelAndView("404");
    }

}