package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;

import ar.edu.itba.paw.webapp.exceptions.ExperienceNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.CompanyForm;
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
    private ExperienceService ex;



    @Autowired
    public WebController(final UserService us, final ExperienceService ex, final EnterpriseService es, final CategoryService cs, final SkillService ss, final EmailService emailService){
        this.us = us;
        this.ex = ex;
        this.es = es;
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
        mav.addObject("experience", ex.findByUserId(userId).orElseThrow(ExperienceNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/createUser", method = { RequestMethod.GET })
    public ModelAndView formUser(@ModelAttribute("userForm") final UserForm userForm) {
        return new ModelAndView("formuser");
    }

    @RequestMapping(value = "/createUser", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("userForm") final UserForm userForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return formUser(userForm);
        }
        final User u = us.register(userForm.getEmail(), userForm.getPassword(), userForm.getName(), userForm.getCity(), "Alguna Categoria", userForm.getPosition(), userForm.getDesc(), null);
//        ex.create(u.getId(), null,null, userForm.getCompany(), userForm.getJob(), userForm.getJobdesc());
        return new ModelAndView("redirect:/profile/" + u.getId());

    }

    @RequestMapping(value ="/contact/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView contactForm(@ModelAttribute("simpleContactForm") final ContactForm form, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("simpleContactForm");
        mav.addObject("user", us.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;

    }
    @RequestMapping(value = "/contact/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView contact(@Valid @ModelAttribute("simpleContactForm") final ContactForm form, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return contactForm(form, userId);
        }
        emailService.sendEmail(us.findById(userId).get().getEmail(), form.getSubject(), form.getMessage(), form.getContactInfo());
        return new ModelAndView("redirect:/");
    }

     @RequestMapping(value ="/createEnterprise", method = { RequestMethod.GET })
    public ModelAndView formEnterprise(@ModelAttribute("companyForm") final CompanyForm companyForm) {
        return new ModelAndView("formenterprise");
    }

    @RequestMapping(value = "/createEnterprise", method = { RequestMethod.POST })
    public ModelAndView createEnterprise(@Valid @ModelAttribute("companyForm") final CompanyForm companyForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return formEnterprise(companyForm);
        }
        final Enterprise e = es.create(companyForm.getCemail(), companyForm.getCname(), companyForm.getCpassword(), companyForm.getCcity(), 0, companyForm.getCdesc());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET })
    public ModelAndView login(@ModelAttribute("loginForm") final UserForm userForm) {
        return new ModelAndView("login");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound() {
        return new ModelAndView("404");
    }

}