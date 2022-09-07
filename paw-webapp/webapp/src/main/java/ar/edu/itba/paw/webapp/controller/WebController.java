package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.User;

import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.ContactForm;
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
    private CategoryService cs;
    private SkillService ss;
    private EmailService emailService;



    @Autowired
    public WebController(final UserService us, final CategoryService cs, final SkillService ss, final EmailService emailService){
        this.us = us;
        this.cs = cs;
        this.ss = ss;
        this.emailService = emailService;
    }

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("users", us.getAllUsers());
        mav.addObject("categories", cs.getAllCategories());
        mav.addObject("skills", ss.getAllSkills());
        return mav;
    }

    @RequestMapping("/register")
    public ModelAndView create(@RequestParam("email") final String email, @RequestParam("password") final String password, @RequestParam("name") final String name, @RequestParam("category") final String category) {
        final User user = us.register(email, password, name, null, category, null, null, null);
        return new ModelAndView("redirect:/profile/" + user.getId());
    }

    @RequestMapping("/profile/{userId:[0-9]+}")
    public ModelAndView profile(@PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", us.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value ="/create", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute("simpleUserForm") final UserForm form) {
        return new ModelAndView("simpleform");
    }

    @RequestMapping(value = "/create", method = { RequestMethod.POST })
    public ModelAndView register(@Valid @ModelAttribute("simpleUserForm") UserForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return registerForm(form);
        }
        final User u = us.register(form.getEmail(), "superPassword", form.getName(), "CABA", "Alguna Categoria", "CEO", form.getDescription(), "ITBA");
        return new ModelAndView("redirect:/profile/" + u.getId());

    }

    @RequestMapping(value ="/contact", method = { RequestMethod.GET })
    public ModelAndView contactForm(@ModelAttribute("simpleContactForm") final ContactForm form) {
        return new ModelAndView("simpleContactForm");

    }
    @RequestMapping(value = "/contact", method = { RequestMethod.POST })
    public ModelAndView contact(@Valid @ModelAttribute("companyForm") final ContactForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return contactForm(form);
        }
        emailService.sendEmail(form.getSubject(), form.getMessage());
        return new ModelAndView("redirect:/");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound() {
        return new ModelAndView("404");
    }

}