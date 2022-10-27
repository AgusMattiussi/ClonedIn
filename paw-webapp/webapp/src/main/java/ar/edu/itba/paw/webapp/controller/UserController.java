package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.SkillNotFoundException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.models.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.io.IOException;

//@Transactional
@Controller
public class UserController {
    private final UserService userService;
    private final ExperienceService experienceService;
    private final EducationService educationService;
    private final UserSkillService userSkillService;
    private final EmailService emailService;
    private final JobOfferService jobOfferService;
    private final JobOfferSkillService jobOfferSkillService;
    private final ContactService contactService;
    private final EnterpriseService enterpriseService;
    private final ImageService imageService;
    private final CategoryService categoryService;
    private final SkillService skillService;
    private static final String ACCEPT = "acceptMsg";
    private static final String REJECT = "rejectMsg";
    private static final Map<String, Integer> monthToNumber = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(final UserService userService, final EnterpriseService enterpriseService, final ExperienceService experienceService,
                          final EducationService educationService, final UserSkillService userSkillService,
                          final EmailService emailService, final JobOfferService jobOfferService,
                          final JobOfferSkillService jobOfferSkillService, final ContactService contactService,
                          final ImageService imageService, final CategoryService categoryService, final SkillService skillService){
        this.userService = userService;
        this.enterpriseService = enterpriseService;
        this.experienceService = experienceService;
        this.educationService = educationService;
        this.userSkillService = userSkillService;
        this.emailService = emailService;
        this.jobOfferService = jobOfferService;
        this.jobOfferSkillService = jobOfferSkillService;
        this.contactService = contactService;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.skillService = skillService;

        monthToNumber.put("No-especificado", 0);
        monthToNumber.put("Enero", 1);
        monthToNumber.put("Febrero", 2);
        monthToNumber.put("Marzo", 3);
        monthToNumber.put("Abril", 4);
        monthToNumber.put("Mayo", 5);
        monthToNumber.put("Junio", 6);
        monthToNumber.put("Julio", 7);
        monthToNumber.put("Agosto", 8);
        monthToNumber.put("Septiembre", 9);
        monthToNumber.put("Octubre", 10);
        monthToNumber.put("Noviembre", 11);
        monthToNumber.put("Diciembre", 12);

    }
    @RequestMapping(value = "/home", method = { RequestMethod.GET })
    public ModelAndView Home(Authentication loggedUser, @RequestParam(value = "page", defaultValue = "1") final int page,
                                    @Valid @ModelAttribute("filtersForm") final FiltersForm filtersForm,
                                    @Valid @ModelAttribute("searchForm") final SearchForm searchForm,
                                    HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("home");

        final List<JobOffer> jobOfferList;

        final int itemsPerPage = 8;

        final int jobOffersCount = jobOfferService.getJobOffersCount();

        //if(request.getParameter("term") == null)
            jobOfferList = jobOfferService.getjobOffersListByFilters(page - 1, itemsPerPage,
                    filtersForm.getCategory(), filtersForm.getModality());
//        else
//            jobOfferList = jobOfferService.getJobOffersListByEnterprise(page - 1, itemsPerPage, searchForm.getTerm());

        mav.addObject("jobOffers", jobOfferList);
        mav.addObject("categories", categoryService.getAllCategories());
        mav.addObject("pages", jobOffersCount / itemsPerPage + 1);
        mav.addObject("currentPage", page);
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND isUserVisible(#userId) OR canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/profileUser/{userId:[0-9]+}")
    public ModelAndView profileUser(Authentication loggedUser, @PathVariable("userId") final long userId) {

        final ModelAndView mav = new ModelAndView("profileUser");
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User {} not found", loggedUser.getName());
            return new UserNotFoundException();
        });
        mav.addObject("user", user);
        mav.addObject("experiences", experienceService.findByUserId(userId));
        mav.addObject("educations", educationService.findByUserId(userId));
        mav.addObject("skills", userSkillService.getSkillsForUser(user));
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
    }


    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/answerJobOffer/{userId:[0-9]+}/{jobOfferId:[0-9]+}/{answer}")
    public ModelAndView answerJobOffer(Authentication loggedUser,
                                       @PathVariable("userId") final long userId,
                                       @PathVariable("jobOfferId") final long jobOfferId,
                                       @PathVariable("answer") final long answer) {

        User user = userService.findById(getLoggerUserId(loggedUser)).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Job Offer not found");
            return new JobOfferNotFoundException();
        });
        Enterprise enterprise = enterpriseService.findById(jobOffer.getEnterpriseID()).orElseThrow(() -> {
            LOGGER.error("Enterprise not found");
            return new UserNotFoundException();
        });

        if(answer==0) {
            contactService.rejectJobOffer(user, jobOffer);
            emailService.sendReplyJobOfferEmail(enterprise, user.getName(), user.getEmail(), jobOffer.getPosition(), REJECT, LocaleContextHolder.getLocale());
        }
        else {
            contactService.acceptJobOffer(user, jobOffer);
            emailService.sendReplyJobOfferEmail(enterprise, user.getName(), user.getEmail(), jobOffer.getPosition(), ACCEPT, LocaleContextHolder.getLocale());
        }

        return new ModelAndView("redirect:/notificationsUser/" + userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/notificationsUser/{userId:[0-9]+}")
    public ModelAndView notificationsUser(Authentication loggedUser, @PathVariable("userId") final long userId,
                                          @RequestParam(value = "status",defaultValue = "") final String status,
                                          @RequestParam(value = "page", defaultValue = "1") final int page,
                                          HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("userNotifications");
        final int itemsPerPage = 4;
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });
        List<JobOfferStatusEnterpriseData> jobOffersList;

        if(request.getParameter("status") == null)
            jobOffersList = contactService.getAllJobOffersWithStatusEnterpriseData(userId,page - 1, itemsPerPage);
        else
            jobOffersList = contactService.getJobOffersWithStatusEnterpriseData(userId,page - 1, itemsPerPage, status);

        Map<Long, List<Skill>> jobOfferSkillMap = contactService.getJobOfferSkillsMapForUser(jobOffersList);
        long contactsCount = status.isEmpty()? contactService.getContactsCountForUser(userId) : jobOffersList.size();

        mav.addObject("user", user);
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        mav.addObject("jobOffers", jobOffersList);
        mav.addObject("jobOffersSkillMap", jobOfferSkillMap);
        mav.addObject("pages", contactsCount / itemsPerPage + 1);
        mav.addObject("currentPage", page);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createExperience/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formExperience(Authentication loggedUser, @ModelAttribute("experienceForm") final ExperienceForm experienceForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("experienceForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        }));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createExperience/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createExperience(Authentication loggedUser, @Valid @ModelAttribute("experienceForm") final ExperienceForm experienceForm, final BindingResult errors, @PathVariable("userId") final long userId) {

        String formYearTo = experienceForm.getYearTo();
        String formMonthTo = experienceForm.getMonthTo();
        String formYearFrom = experienceForm.getYearFrom();

        boolean yearToIsEmpty = formYearTo.isEmpty();
        boolean monthToIsEmpty = formMonthTo.equals("No-especificado");
        boolean monthOrYearEmpty = yearToIsEmpty && !monthToIsEmpty || !yearToIsEmpty && monthToIsEmpty;
        boolean yearFromIsEmpty = formYearFrom.isEmpty();
        boolean yearWrongFormat = false;

        Integer yearTo;
        Integer yearFrom;

        try {
            yearTo = yearToIsEmpty ? null : Integer.valueOf(formYearTo);
            yearFrom = yearFromIsEmpty ? null : Integer.valueOf(formYearFrom);
        } catch (NumberFormatException e) {
            yearWrongFormat = true;
            yearTo = null;
            yearFrom = null;
        };

        Integer monthTo = monthToIsEmpty ? null : monthToNumber.get(formMonthTo);
        Integer monthFrom = monthToNumber.get(experienceForm.getMonthFrom());

        boolean invalidDate = !yearToIsEmpty && !monthToIsEmpty && !yearWrongFormat &&
                (yearTo.compareTo(yearFrom) < 0 || yearTo.equals(yearFrom) && monthTo.compareTo(monthFrom) < 0);


        if (errors.hasErrors() || yearFromIsEmpty || yearWrongFormat || monthOrYearEmpty || invalidDate) {
            if(monthOrYearEmpty)
                errors.rejectValue("yearTo", "YearOrMonthEmpty", "You must pick a year and month, or let both fields empty");
            else if (invalidDate)
                errors.rejectValue("yearTo", "InvalidDate", "End date cannot be before initial date");

            LOGGER.warn("Experience form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return formExperience(loggedUser, experienceForm, userId);
        }

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });

        Experience experience = experienceService.create(user, monthToNumber.get(experienceForm.getMonthFrom()), Integer.parseInt(experienceForm.getYearFrom()),
                monthTo, yearTo,experienceForm.getCompany(),
                experienceForm.getJob(), experienceForm.getJobDesc());

        LOGGER.debug("A new experience was registered under id: {}", experience.getId());
        LOGGER.info("A new experience was registered");

        return new ModelAndView("redirect:/profileUser/" + user.getId());

    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/deleteExperience/{userId:[0-9]+}/{experienceId:[0-9]+}", method = { RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET })
    public ModelAndView deleteExperience(Authentication loggedUser, @PathVariable("userId") final long userId, @PathVariable("experienceId") final long experienceId) {
        experienceService.deleteExperience(experienceId);
        return new ModelAndView("redirect:/profileUser/" + userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createEducation/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formEducation(Authentication loggedUser, @ModelAttribute("educationForm") final EducationForm educationForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("educationForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        }));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createEducation/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createEducation(Authentication loggedUser, @Valid @ModelAttribute("educationForm") final EducationForm educationForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        int yearFlag = educationForm.getYearTo().compareTo(educationForm.getYearFrom());
        int monthFlag = monthToNumber.get(educationForm.getMonthTo()).compareTo(monthToNumber.get(educationForm.getMonthFrom()));

        if (errors.hasErrors() || yearFlag < 0 || monthFlag < 0) {
            if(yearFlag < 0)
                errors.rejectValue("yearTo", "InvalidDate", "End date cannot be before initial date");
            else if (yearFlag == 0 && monthFlag < 0)
                errors.rejectValue("monthTo", "InvalidDate", "End date cannot be before initial date");
            LOGGER.warn("Education form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return formEducation(loggedUser, educationForm, userId);
        }

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });
        Education education = educationService.add(user, monthToNumber.get(educationForm.getMonthFrom()), Integer.parseInt(educationForm.getYearFrom()),
                monthToNumber.get(educationForm.getMonthTo()), Integer.parseInt(educationForm.getYearTo()), educationForm.getDegree(), educationForm.getCollege(), educationForm.getComment());

        LOGGER.debug("A new experience was registered under id: {}", education.getId());
        LOGGER.info("A new experience was registered");

        return new ModelAndView("redirect:/profileUser/" + user.getId());

    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/deleteEducation/{userId:[0-9]+}/{educationId:[0-9]+}", method = { RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET })
    public ModelAndView deleteEducation(Authentication loggedUser, @PathVariable("userId") final long userId, @PathVariable("educationId") final long educationId) {
        educationService.deleteEducation(educationId);
        return new ModelAndView("redirect:/profileUser/" + userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createSkill/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formSkill(Authentication loggedUser, @ModelAttribute("skillForm") final SkillForm skillForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("skillsForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        }));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createSkill/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createSkill(Authentication loggedUser, @Valid @ModelAttribute("skillForm") final SkillForm skillForm,
                                    final BindingResult errors, @PathVariable("userId") final long userId) {
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });

        Skill skill = skillService.findByDescriptionOrCreate(skillForm.getSkill());

        if (errors.hasErrors() || userSkillService.alreadyExists(skill, user)) {
            errors.rejectValue("skill", "ExistingSkillForUser", "You already have this skill for this user.");
            LOGGER.warn("Skill form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return formSkill(loggedUser, skillForm, userId);
        }

        userSkillService.addSkillToUser(skill, user);

        LOGGER.debug("A new skill has been added to user {}", user.getId());
        LOGGER.info("A new skill has been added to user");

        return new ModelAndView("redirect:/profileUser/" + user.getId());
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/deleteSkill/{userId:[0-9]+}/{skillId:[0-9]+}", method = { RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET })
    public ModelAndView deleteSkill(Authentication loggedUser, @PathVariable("userId") final long userId, @PathVariable("skillId") final long skillId) {
        userSkillService.deleteSkillFromUser(userId, skillId);
        return new ModelAndView("redirect:/profileUser/" + userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/uploadProfileImage/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formImage(Authentication loggedUser, @ModelAttribute("imageForm") final ImageForm imageForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("imageForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/uploadProfileImage/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView uploadImage(Authentication loggedUser, @Valid @ModelAttribute("imageForm") final ImageForm imageForm, final BindingResult errors,
                                    @PathVariable("userId") final long userId) throws IOException {
        if (errors.hasErrors()) {
            return formImage(loggedUser, imageForm, userId);
        }
        userService.updateProfileImage(userId, imageForm.getImage().getBytes());
        return new ModelAndView("redirect:/profileUser/" + userId);
    }

    @RequestMapping(value = "/{userId:[0-9]+}/image/{imageId}", method = RequestMethod.GET, produces = "image/*")
    public @ResponseBody byte[] getProfileImage(@PathVariable("userId") final long userId, @PathVariable("imageId") final int imageId) {
        LOGGER.debug("Trying to access profile image");
        byte[] profileImage = new byte[0];
        try {
            profileImage = userService.getProfileImage(imageId).orElseThrow(UserNotFoundException::new).getBytes();
        } catch (UserNotFoundException e) {
            LOGGER.error("Error loading image {}", imageId);
        }
        LOGGER.info("Profile image accessed.");
        return profileImage;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/editUser/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formEditUser(Authentication loggedUser, @ModelAttribute("editUserForm") final EditUserForm editUserForm,
                                     @PathVariable("userId") final long userId) {
        ModelAndView mav = new ModelAndView("userEditForm");
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });
        mav.addObject("user", user);
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/editUser/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView editUser(Authentication loggedUser, @Valid @ModelAttribute("editUserForm") final EditUserForm editUserForm,
                                 final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formEditUser(loggedUser, editUserForm, userId);
        }
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        userService.updateUserInformation(user, editUserForm.getName(), editUserForm.getAboutMe(), editUserForm.getLocation(),
                editUserForm.getPosition(), editUserForm.getCategory(), editUserForm.getLevel());
        return new ModelAndView("redirect:/profileUser/" + userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/hideUserProfile/{userId:[0-9]+}", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView hideUserProfile(Authentication loggedUser, @PathVariable("userId") final long userId) {
        userService.hideUserProfile(userId);
        return new ModelAndView("redirect:/profileUser/" + userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/showUserProfile/{userId:[0-9]+}", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView showUserProfile(Authentication loggedUser, @PathVariable("userId") final long userId) {
        userService.showUserProfile(userId);
        return new ModelAndView("redirect:/profileUser/" + userId);
    }

    private boolean isUser(Authentication loggedUser){
        return loggedUser.getAuthorities().contains(AuthUserDetailsService.getUserSimpleGrantedAuthority());
    }

    private long getLoggerUserId(Authentication loggedUser){
        if(isUser(loggedUser)) {
            User user = userService.findByEmail(loggedUser.getName()).orElseThrow(() -> {
                LOGGER.error("User not found");
                return new UserNotFoundException();
            });
            return user.getId();
        } else {
            Enterprise enterprise = enterpriseService.findByEmail(loggedUser.getName()).orElseThrow(() -> {
                LOGGER.error("Enterprise not found");
                return new UserNotFoundException();
            });
            return enterprise.getId();
        }
    }


}
