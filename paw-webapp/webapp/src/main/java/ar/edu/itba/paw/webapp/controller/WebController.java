package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;

import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.sql.Date;

@Controller
public class WebController {

    private final UserService userService;
    private final EnterpriseService enterpriseService;
    private final CategoryService categoryService;
    private final SkillService skillService;
    private final EmailService emailService;
    private final ExperienceService experienceService;
    private final EducationService educationService;
    private final UserSkillService userSkillService;
    private final JobOfferService jobOfferService;
    private static final int itemsPerPage = 8;
    private static final String CONTACT_TEMPLATE = "contactEmail.html";
    private static final String REGISTER_SUCCESS_TEMPLATE = "registerSuccess.html";

    @Autowired
    MessageSource messageSource;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    public WebController(final UserService userService, final EnterpriseService enterpriseService, final CategoryService categoryService, final ExperienceService experienceService,
                         final EducationService educationService, final SkillService skillService, final UserSkillService userSkillService,
                         final EmailService emailService, JobOfferService jobOfferService){
        this.userService = userService;
        this.enterpriseService = enterpriseService;
        this.experienceService = experienceService;
        this.educationService = educationService;
        this.categoryService = categoryService;
        this.skillService = skillService;
        this.userSkillService = userSkillService;
        this.emailService = emailService;
        this.jobOfferService = jobOfferService;
    }

    @RequestMapping("/")
    public ModelAndView home(Authentication loggedUser, @RequestParam(value = "page", defaultValue = "1") final int page,
                             @RequestParam(value = "category", defaultValue = "7") final int categoryId) {
        final ModelAndView mav = new ModelAndView("index");

        final List<User> usersList = categoryId == 7? userService.getUsersList(page - 1, itemsPerPage) :
                userService.getUsersListByCategory(page - 1, itemsPerPage, categoryId);

        final Integer usersCount = userService.getUsersCount().orElse(0);

        mav.addObject("users", usersList);
        mav.addObject("categories", categoryService.getAllCategories());
        mav.addObject("skills", skillService.getAllSkills());
        mav.addObject("pages", usersCount / itemsPerPage + 1);
        mav.addObject("currentPage", page);
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
    }

    @RequestMapping("/profileUser/{userId:[0-9]+}")
    public ModelAndView profileUser(Authentication loggedUser, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("profileUser");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        mav.addObject("experiences", experienceService.findByUserId(userId));
        mav.addObject("educations", educationService.findByUserId(userId));
        mav.addObject("skills", userSkillService.getSkillsForUser(userId));
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
    }

    @RequestMapping("/notificationsUser/{userId:[0-9]+}")
    public ModelAndView notificationsUser(Authentication loggedUser, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("userNotifications");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
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
            return formRegisterUser(userForm);
        }
        final User u = userService.register(userForm.getEmail(), userForm.getPassword(), userForm.getName(), userForm.getCity(), userForm.getCategory(), userForm.getPosition(), userForm.getAboutMe(), null);
        sendRegisterEmail(userForm.getEmail(), userForm.getName());

        authWithAuthManager(request, userForm.getEmail(), userForm.getPassword());

        return new ModelAndView("redirect:/profileUser/" + u.getId());
    }

    @RequestMapping(value = "/createExperience/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formExperience(@ModelAttribute("experienceForm") final ExperienceForm experienceForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("experienceForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/createExperience/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createExperience(@Valid @ModelAttribute("experienceForm") final ExperienceForm experienceForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formExperience(experienceForm, userId);
        }
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        experienceService.create(user.getId(), Date.valueOf(experienceForm.getDateFrom()), Date.valueOf(experienceForm.getDateTo()), experienceForm.getCompany(), experienceForm.getJob(), experienceForm.getJobDesc());
        return new ModelAndView("redirect:/profileUser/" + user.getId());

    }

    @RequestMapping(value = "/createEducation/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formEducation(@ModelAttribute("educationForm") final EducationForm educationForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("educationForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/createEducation/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createEducation(@Valid @ModelAttribute("educationForm") final EducationForm educationForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formEducation(educationForm, userId);
        }

        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        educationService.add(user.getId(), Date.valueOf(educationForm.getDateFrom()), Date.valueOf(educationForm.getDateTo()), educationForm.getDegree(), educationForm.getCollege(), educationForm.getComment());
        return new ModelAndView("redirect:/profileUser/" + user.getId());

    }

    @RequestMapping(value = "/createSkill/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formSkill(@ModelAttribute("skillForm") final SkillForm skillForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("skillsForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/createSkill/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createSkill(@Valid @ModelAttribute("skillForm") final SkillForm skillForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formSkill(skillForm, userId);
        }

        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!skillForm.getLang().isEmpty())
            userSkillService.addSkillToUser(skillForm.getLang(), user.getId());
        if(!skillForm.getMore().isEmpty())
            userSkillService.addSkillToUser(skillForm.getMore(), user.getId());
        if(!skillForm.getSkill().isEmpty())
            userSkillService.addSkillToUser(skillForm.getSkill(), user.getId());
        return new ModelAndView("redirect:/profileUser/" + user.getId());
    }

    @RequestMapping("/profileEnterprise/{enterpriseId:[0-9]+}")
    public ModelAndView profileEnterprise(Authentication loggedUser, @PathVariable("enterpriseId") final long enterpriseId) {
        final ModelAndView mav = new ModelAndView("profileEnterprise");
        mav.addObject("enterprise", enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new));
        mav.addObject("joboffers", jobOfferService.findByEnterpriseId(enterpriseId));
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
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
            return formRegisterEnterprise(enterpriseForm);
        }
        final Enterprise e = enterpriseService.create(enterpriseForm.getEmail(), enterpriseForm.getName(), enterpriseForm.getPassword(), enterpriseForm.getCity(), enterpriseForm.getCategory(), enterpriseForm.getAboutUs());
        sendRegisterEmail(enterpriseForm.getEmail(), enterpriseForm.getName());

        authWithAuthManager(request, enterpriseForm.getEmail(), enterpriseForm.getPassword());

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/createJobOffer/{enterpriseId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formJobOffer(@ModelAttribute("jobOfferForm") final JobOfferForm jobOfferForm, @PathVariable("enterpriseId") final long enterpriseId) {
        final ModelAndView mav = new ModelAndView("jobOfferForm");
        mav.addObject("enterprise", enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new));
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @RequestMapping(value = "/createJobOffer/{enterpriseId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createJobOffer(@Valid @ModelAttribute("jobOfferForm") final JobOfferForm jobOfferForm, final BindingResult errors, @PathVariable("enterpriseId") final long enterpriseId) {
        if (errors.hasErrors()) {
            return formJobOffer(jobOfferForm, enterpriseId);
        }
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);
        jobOfferService.create(enterprise.getId(), 1, jobOfferForm.getJobPosition(), jobOfferForm.getJobDescription(), jobOfferForm.getSalary());
        return new ModelAndView("redirect:/profileEnterprise/" + enterprise.getId());

    }

    @RequestMapping(value ="/contact/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView contactForm(Authentication loggedUser, @ModelAttribute("simpleContactForm") final ContactForm form, @PathVariable("userId") final long userId) {
        long loggedUserID = getLoggerUserId(loggedUser);
        final ModelAndView mav = new ModelAndView("simpleContactForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        mav.addObject("jobOffers", jobOfferService.findByEnterpriseId(loggedUserID));
        mav.addObject("loggedUserID", loggedUserID);
        return mav;
    }

    @RequestMapping(value = "/contact/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView contact(Authentication loggedUser, @Valid @ModelAttribute("simpleContactForm") final ContactForm form, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return contactForm(loggedUser, form, userId);
        }
        long jobOfferId = form.getCategory();
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(JobOfferNotFoundException::new);

        Enterprise enterprise = enterpriseService.findByEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);

        final Map<String, Object> mailMap = new HashMap<>();
        final User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);

        mailMap.put(EmailService.USERNAME_FIELD, user.getName());
        mailMap.put("jobDesc", jobOffer.getDescription());
        mailMap.put("jobPos", jobOffer.getPosition());
        mailMap.put("enterpriseName", enterprise.getName());
        mailMap.put("enterpriseEmail", enterprise.getEmail());
        mailMap.put("message", form.getMessage());

        mailMap.put("congratulationsMsg", messageSource.getMessage("contactMail.congrats", null, Locale.getDefault()));
        mailMap.put("enterpriseMsg", messageSource.getMessage("contactMail.enterprise", null, Locale.getDefault()));
        mailMap.put("positionMsg", messageSource.getMessage("contactMail.position", null, Locale.getDefault()));
        mailMap.put("descriptionMsg", messageSource.getMessage("contactMail.description", null, Locale.getDefault()));
        mailMap.put("salaryMsg", messageSource.getMessage("contactMail.salary", null, Locale.getDefault()));
        mailMap.put("additionalCommentsMsg", messageSource.getMessage("contactMail.additionalComments", null, Locale.getDefault()));
        mailMap.put("buttonMsg", messageSource.getMessage("contactMail.button", null, Locale.getDefault()));

        String subject = messageSource.getMessage("contactMail.subject", null, Locale.getDefault()) + enterprise.getName();

        emailService.sendEmail(user.getEmail(), subject, CONTACT_TEMPLATE, mailMap);

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET })
    public ModelAndView login(@ModelAttribute("loginForm") final LoginForm loginForm) {
        return new ModelAndView("login");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound() {
        return new ModelAndView("404");
    }

    private long getLoggerUserId(Authentication loggedUser){
        if(loggedUser.getAuthorities().contains(AuthUserDetailsService.getUserSimpleGrantedAuthority())) {
            User user = userService.findByEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);
            return user.getId();
        } else {
            Enterprise enterprise = enterpriseService.findByEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);
            return enterprise.getId();
        }
    }

    private void sendRegisterEmail(String email, String username){
        final Map<String, Object> mailMap = new HashMap<>();

        mailMap.put("username", username);
        mailMap.put("welcomeMsg", messageSource.getMessage("registerMail.welcomeMsg", null, Locale.getDefault()));
        mailMap.put("buttonMsg", messageSource.getMessage("registerMail.button", null, Locale.getDefault()));

        String subject = messageSource.getMessage("registerMail.subject", null, Locale.getDefault());

        emailService.sendEmail(email, subject, REGISTER_SUCCESS_TEMPLATE, mailMap);
    }

    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}