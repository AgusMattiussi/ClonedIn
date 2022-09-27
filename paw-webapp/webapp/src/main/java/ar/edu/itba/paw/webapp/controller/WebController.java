package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;

import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserIsNotProfileOwnerException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final ContactService contactService;
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
                         final EmailService emailService, final JobOfferService jobOfferService, final ContactService contactService){
        this.userService = userService;
        this.enterpriseService = enterpriseService;
        this.experienceService = experienceService;
        this.educationService = educationService;
        this.categoryService = categoryService;
        this.skillService = skillService;
        this.userSkillService = userSkillService;
        this.emailService = emailService;
        this.jobOfferService = jobOfferService;
        this.contactService = contactService;
    }

     private boolean isUser(Authentication loggedUser){
        return loggedUser.getAuthorities().contains(AuthUserDetailsService.getUserSimpleGrantedAuthority());
    }

    private boolean isEnterprise(Authentication loggedUser){
        return loggedUser.getAuthorities().contains(AuthUserDetailsService.getEnterpriseSimpleGrantedAuthority());
    }

    private long getLoggerUserId(Authentication loggedUser){
        if(isUser(loggedUser)) {
            User user = userService.findByEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);
            return user.getId();
        } else {
            Enterprise enterprise = enterpriseService.findByEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);
            return enterprise.getId();
        }
    }

    private boolean isLoggedUserProfile(Authentication loggedUser, long profileID) {
        long loggedUserId = getLoggerUserId(loggedUser);
        return loggedUserId == profileID;
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET })
    public ModelAndView home(Authentication loggedUser, @RequestParam(value = "page", defaultValue = "1") final int page,
                             @Valid @ModelAttribute("filterForm") final FilterForm filterForm,
                             @Valid @ModelAttribute("searchForm") final SearchForm searchForm,
                             HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("index");

        final List<User> usersList;

        //TODO: refactor?
        if(request.getParameter("term") == null)
                usersList = userService.getUsersListByFilters(page-1, itemsPerPage,
                            filterForm.getCategory(), filterForm.getLocation(), filterForm.getEducationLevel());
        else
            usersList = userService.getUsersListByName(page - 1, itemsPerPage, searchForm.getTerm());

        mav.addObject("users", usersList);
        mav.addObject("categories", categoryService.getAllCategories());
        mav.addObject("skills", skillService.getAllSkills());
        mav.addObject("pages", usersList.size() / itemsPerPage + 1);
        mav.addObject("currentPage", page);
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') OR canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/profileUser/{userId:[0-9]+}")
    public ModelAndView profileUser(Authentication loggedUser, @PathVariable("userId") final long userId) {

        final ModelAndView mav = new ModelAndView("profileUser");
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        mav.addObject("user", user);
        mav.addObject("experiences", experienceService.findByUserId(userId));
        mav.addObject("educations", educationService.findByUserId(userId));
        mav.addObject("skills", userSkillService.getSkillsForUser(userId));
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') OR canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/notificationsUser/{userId:[0-9]+}")
    public ModelAndView notificationsUser(Authentication loggedUser, @PathVariable("userId") final long userId,
                                          @RequestParam(value = "page", defaultValue = "1") final int page) {
        final ModelAndView mav = new ModelAndView("userNotifications");

        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        mav.addObject("jobOffers", contactService.getJobOffersForUser(userId));
//        mav.addObject("pages", jobOffersCount / itemsPerPage + 1);
//        mav.addObject("currentPage", page);
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
        final User u = userService.register(userForm.getEmail(), userForm.getPassword(), userForm.getName(), userForm.getCity(), userForm.getCategory(), userForm.getPosition(), userForm.getAboutMe(), userForm.getLevel());
        sendRegisterEmail(userForm.getEmail(), userForm.getName());

        authWithAuthManager(request, userForm.getEmail(), userForm.getPassword());

        return new ModelAndView("redirect:/profileUser/" + u.getId());
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') OR canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createExperience/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formExperience(Authentication loggedUser, @ModelAttribute("experienceForm") final ExperienceForm experienceForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("experienceForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') OR canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createExperience/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createExperience(Authentication loggedUser, @Valid @ModelAttribute("experienceForm") final ExperienceForm experienceForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formExperience(loggedUser, experienceForm, userId);
        }
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        experienceService.create(user.getId(), Date.valueOf("2020-01-01"), Date.valueOf("2020-01-01"), experienceForm.getCompany(), experienceForm.getJob(), experienceForm.getJobDesc());
        return new ModelAndView("redirect:/profileUser/" + user.getId());

    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') OR canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createEducation/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formEducation(Authentication loggedUser, @ModelAttribute("educationForm") final EducationForm educationForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("educationForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') OR canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createEducation/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createEducation(Authentication loggedUser, @Valid @ModelAttribute("educationForm") final EducationForm educationForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formEducation(loggedUser, educationForm, userId);
        }

        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        educationService.add(user.getId(), Date.valueOf("2020-01-01"), Date.valueOf("2020-01-01"), educationForm.getDegree(), educationForm.getCollege(), educationForm.getComment());
        return new ModelAndView("redirect:/profileUser/" + user.getId());

    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') OR canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createSkill/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formSkill(Authentication loggedUser, @ModelAttribute("skillForm") final SkillForm skillForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("skillsForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') OR canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createSkill/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createSkill(Authentication loggedUser, @Valid @ModelAttribute("skillForm") final SkillForm skillForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formSkill(loggedUser, skillForm, userId);
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

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/profileEnterprise/{enterpriseId:[0-9]+}")
    public ModelAndView profileEnterprise(Authentication loggedUser, @PathVariable("enterpriseId") final long enterpriseId) {
        final ModelAndView mav = new ModelAndView("profileEnterprise");
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);
        mav.addObject("enterprise", enterprise);
        mav.addObject("category", categoryService.findById(enterprise.getCategoryId_fk()));
        mav.addObject("joboffers", jobOfferService.findByEnterpriseId(enterpriseId));
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/contactsEnterprise/{enterpriseId:[0-9]+}")
    public ModelAndView contactsEnterprise(Authentication loggedUser, @PathVariable("enterpriseId") final long enterpriseId) {
        final ModelAndView mav = new ModelAndView("contacts");
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        mav.addObject("jobOffers", jobOfferService.findByEnterpriseId(enterpriseId));
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

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping(value = "/createJobOffer/{enterpriseId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formJobOffer(Authentication loggedUser, @ModelAttribute("jobOfferForm") final JobOfferForm jobOfferForm, @PathVariable("enterpriseId") final long enterpriseId) {
        final ModelAndView mav = new ModelAndView("jobOfferForm");
        mav.addObject("enterprise", enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new));
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping(value = "/createJobOffer/{enterpriseId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createJobOffer(Authentication loggedUser, @Valid @ModelAttribute("jobOfferForm") final JobOfferForm jobOfferForm, final BindingResult errors, @PathVariable("enterpriseId") final long enterpriseId) {
        if (errors.hasErrors()) {
            return formJobOffer(loggedUser, jobOfferForm, enterpriseId);
        }
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);
        jobOfferService.create(enterprise.getId(), 1, jobOfferForm.getJobPosition(), jobOfferForm.getJobDescription(), jobOfferForm.getSalary(), jobOfferForm.getMode());
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
        mailMap.put("salary", String.valueOf(jobOffer.getSalary()));
        mailMap.put("modality", jobOffer.getModality());
        mailMap.put("enterpriseName", enterprise.getName());
        mailMap.put("enterpriseEmail", enterprise.getEmail());
        mailMap.put("message", form.getMessage());

        mailMap.put("congratulationsMsg", messageSource.getMessage("contactMail.congrats", null, Locale.getDefault()));
        mailMap.put("enterpriseMsg", messageSource.getMessage("contactMail.enterprise", null, Locale.getDefault()));
        mailMap.put("positionMsg", messageSource.getMessage("contactMail.position", null, Locale.getDefault()));
        mailMap.put("descriptionMsg", messageSource.getMessage("contactMail.description", null, Locale.getDefault()));
        mailMap.put("salaryMsg", messageSource.getMessage("contactMail.salary", null, Locale.getDefault()));
        mailMap.put("modalityMsg", messageSource.getMessage("contactMail.modality", null, Locale.getDefault()));
        mailMap.put("additionalCommentsMsg", messageSource.getMessage("contactMail.additionalComments", null, Locale.getDefault()));
        mailMap.put("buttonMsg", messageSource.getMessage("contactMail.button", null, Locale.getDefault()));

        String subject = messageSource.getMessage("contactMail.subject", null, Locale.getDefault()) + enterprise.getName();
        emailService.sendEmail(user.getEmail(), subject, CONTACT_TEMPLATE, mailMap);
        // TODO: validar clave duplicada
        contactService.addContact(enterprise.getId(), user.getId(), jobOfferId);


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

    @ExceptionHandler(UserIsNotProfileOwnerException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ModelAndView userIsNotProfileOwner() {
        return new ModelAndView("403");
    }

    private void sendRegisterEmail(String email, String username){
        final Map<String, Object> mailMap = new HashMap<>();

        mailMap.put("username", username);
        mailMap.put("welcomeMsg", messageSource.getMessage("registerMail.welcomeMsg", null, Locale.getDefault()));
        mailMap.put("bodyMsg", messageSource.getMessage("registerMail.bodyMsg", null, Locale.getDefault()));
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