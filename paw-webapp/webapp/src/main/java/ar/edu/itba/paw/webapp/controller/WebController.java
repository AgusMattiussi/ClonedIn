package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;

import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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
    public ModelAndView home(@RequestParam(value = "page", defaultValue = "1") final int page,
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
        return mav;
    }

    @RequestMapping("/register")
    public ModelAndView create(@RequestParam("email") final String email, @RequestParam("password") final String password, @RequestParam("name") final String name, @RequestParam("category") final String category) {
        final User user = userService.register(email, password, name, null, category, null, null, null);
        return new ModelAndView("redirect:/profile/" + user.getId());
    }

    @RequestMapping("/profile/{userId:[0-9]+}")
    public ModelAndView profile(@PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        mav.addObject("experiences", experienceService.findByUserId(userId));
        mav.addObject("educations", educationService.findByUserId(userId));
        mav.addObject("skills", userSkillService.getSkillsForUser(userId));
        return mav;
    }

    @RequestMapping("/profileE/{enterpriseId:[0-9]+}")
    public ModelAndView profileEnterprise(@PathVariable("enterpriseId") final long enterpriseId) {
        final ModelAndView mav = new ModelAndView("profileEnterprise");
        mav.addObject("enterprise", enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new));
        mav.addObject("joboffers", jobOfferService.findByEnterpriseId(enterpriseId));
        return mav;
    }

    @RequestMapping(value = "/createUser", method = { RequestMethod.GET })
    public ModelAndView formUser(@ModelAttribute("userForm") final UserForm userForm) {
        ModelAndView mav = new ModelAndView("formuser");
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @RequestMapping(value = "/createUser", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("userForm") final UserForm userForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return formUser(userForm);
        }
        final User u = userService.register(userForm.getEmail(), userForm.getPassword(), userForm.getName(), userForm.getCity(), userForm.getCategory(), userForm.getPosition(), userForm.getDesc(), null);
        return new ModelAndView("redirect:/profile/" + u.getId());
    }

    @RequestMapping(value = "/createEx/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formEx(@ModelAttribute("experienceForm") final ExperienceForm experienceForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("experienceform");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/createEx/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createEx(@Valid @ModelAttribute("experienceForm") final ExperienceForm experienceForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formEx(experienceForm, userId);
        }
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        experienceService.create(user.getId(), Date.valueOf(experienceForm.getDateFrom()), Date.valueOf(experienceForm.getDateTo()), experienceForm.getCompany(), experienceForm.getJob(), experienceForm.getJobDesc());
        return new ModelAndView("redirect:/profile/" + user.getId());

    }

    @RequestMapping(value = "/createEd/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formEd(@ModelAttribute("educationForm") final EducationForm educationForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("educationform");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/createEd/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createEd(@Valid @ModelAttribute("educationForm") final EducationForm educationForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formEd(educationForm, userId);
        }

        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        educationService.add(user.getId(), Date.valueOf(educationForm.getDateFrom()), Date.valueOf(educationForm.getDateTo()), educationForm.getDegree(), educationForm.getCollege(), educationForm.getComment());
        return new ModelAndView("redirect:/profile/" + user.getId());

    }

    @RequestMapping(value = "/createSkill/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formSkill(@ModelAttribute("skillForm") final SkillForm skillForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("skillsform");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/createSkill/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createSkill(@Valid @ModelAttribute("skillForm") final SkillForm skillForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formSkill(skillForm, userId);
        }

        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        userSkillService.addSkillToUser(skillForm.getLang(), user.getId());
        userSkillService.addSkillToUser(skillForm.getMore(), user.getId());
        userSkillService.addSkillToUser(skillForm.getSkill(), user.getId());
        return new ModelAndView("redirect:/profile/" + user.getId());
    }

    @RequestMapping(value = "/createJO/{enterpriseId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formJO(@ModelAttribute("joForm") final JOForm joForm, @PathVariable("enterpriseId") final long enterpriseId) {
        final ModelAndView mav = new ModelAndView("formjoboffer");
        mav.addObject("enterprise", enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/createJO/{enterpriseId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createJO(@Valid @ModelAttribute("joForm") final JOForm joForm, final BindingResult errors, @PathVariable("enterpriseId") final long enterpriseId) {
        if (errors.hasErrors()) {
            return formJO(joForm, enterpriseId);
        }
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);
        jobOfferService.create(enterprise.getId(), 0, joForm.getJobposition(), joForm.getJobdescription(), joForm.getSalary());
        return new ModelAndView("redirect:/profileE/" + enterprise.getId());

    }

    @RequestMapping(value ="/contact/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView contactForm(@ModelAttribute("simpleContactForm") final ContactForm form, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("simpleContactForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;

    }

    @RequestMapping(value = "/contact/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView contact(@Valid @ModelAttribute("simpleContactForm") final ContactForm form, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return contactForm(form, userId);
        }

        final Map<String, Object> mailMap = new HashMap<>();
        final User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);

        mailMap.put(EmailService.USERNAME_FIELD, user.getName());
        mailMap.put(EmailService.MESSAGE_FIELD, form.getMessage());
        mailMap.put(EmailService.CONTACT_INFO_FIELD, form.getContactInfo());

//        emailService.sendEmail(userService.findById(userId).get().getEmail(), form.getSubject(), form.getMessage(), form.getContactInfo());
        emailService.sendEmail(user.getEmail(), form.getSubject(), CONTACT_TEMPLATE, mailMap);

        return new ModelAndView("redirect:/");
    }

     @RequestMapping(value ="/createEnterprise", method = { RequestMethod.GET })
    public ModelAndView formEnterprise(@ModelAttribute("enterpriseForm") final EnterpriseForm enterpriseForm) {
         ModelAndView mav = new ModelAndView("formenterprise");
         mav.addObject("categories", categoryService.getAllCategories());
         return mav;
     }

    @RequestMapping(value = "/createEnterprise", method = { RequestMethod.POST })
    public ModelAndView createEnterprise(@Valid @ModelAttribute("enterpriseForm") final EnterpriseForm enterpriseForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return formEnterprise(enterpriseForm);
        }
        final Enterprise e = enterpriseService.create(enterpriseForm.getEmail(), enterpriseForm.getName(), enterpriseForm.getPassword(), enterpriseForm.getCity(), enterpriseForm.getCategory(), enterpriseForm.getDescription());
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