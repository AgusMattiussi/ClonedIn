package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.webapp.form.EnterpriseForm;
import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);
    private final UserService userService;
    private final EnterpriseService enterpriseService;
    private final CategoryService categoryService;
    private final EmailService emailService;
    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    public RegisterController(UserService userService, EnterpriseService enterpriseService,
                              CategoryService categoryService, EmailService emailService) {
        this.userService = userService;
        this.enterpriseService = enterpriseService;
        this.categoryService = categoryService;
        this.emailService = emailService;
    }

    @RequestMapping(value = "/createUser", method = { RequestMethod.GET })
    public ModelAndView formRegisterUser(@ModelAttribute("userForm") final UserForm userForm) {
        ModelAndView mav = new ModelAndView("userRegisterForm");
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @RequestMapping(value = "/createUser", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("userForm") final UserForm userForm, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            LOGGER.warn("User register form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return formRegisterUser(userForm);
        }

        Category category = categoryService.findByName(userForm.getCategory()).orElseThrow(CategoryNotFoundException::new);

        final User u = userService.register(userForm.getEmail(), userForm.getPassword(), userForm.getName(), userForm.getCity(),
                category, userForm.getPosition(), userForm.getAboutMe(), userForm.getLevel());
        emailService.sendRegisterUserConfirmationEmail(u, LocaleContextHolder.getLocale());
        authWithAuthManager(request, userForm.getEmail(), userForm.getPassword());
        LOGGER.debug("A new user was registered under id: {}", u.getId());
        LOGGER.info("A new user was registered");
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value ="/createEnterprise", method = { RequestMethod.GET })
    public ModelAndView formRegisterEnterprise(@ModelAttribute("enterpriseForm") final EnterpriseForm enterpriseForm) {
        ModelAndView mav = new ModelAndView("enterpriseRegisterForm");
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @RequestMapping(value = "/createEnterprise", method = { RequestMethod.POST })
    public ModelAndView createEnterprise(@Valid @ModelAttribute("enterpriseForm") final EnterpriseForm enterpriseForm, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            LOGGER.warn("Enterprise register form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return formRegisterEnterprise(enterpriseForm);
        }
        final Enterprise e = enterpriseService.create(enterpriseForm.getEmail(), enterpriseForm.getName(), enterpriseForm.getPassword(), enterpriseForm.getCity(),
                enterpriseForm.getCategory(), enterpriseForm.getWorkers(), enterpriseForm.getYear().isEmpty()? null : Integer.valueOf(enterpriseForm.getYear()), enterpriseForm.getLink(), enterpriseForm.getAboutUs());
        emailService.sendRegisterEnterpriseConfirmationEmail(enterpriseForm.getEmail(), enterpriseForm.getName(), LocaleContextHolder.getLocale());
        authWithAuthManager(request, enterpriseForm.getEmail(), enterpriseForm.getPassword());
        LOGGER.debug("A new enterprise was registered under id: {}", e.getId());
        LOGGER.info("A new enterprise was registered");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET })
    public ModelAndView login(@ModelAttribute("loginForm") final LoginForm loginForm) {
        return new ModelAndView("login");
    }

    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
