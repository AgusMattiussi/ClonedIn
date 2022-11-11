package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.helpers.DateHelper;
import ar.edu.itba.paw.models.helpers.PaginationHelper;
import ar.edu.itba.paw.models.helpers.SortHelper;
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
import org.thymeleaf.expression.Strings;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
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
    private final AuthUserDetailsService authUserDetailsService;
    private static final String ACCEPT = "acceptMsg";
    private static final String REJECT = "rejectMsg";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final int JOB_OFFERS_PER_PAGE = 3;
    private static final int CONTACTS_PER_PAGE = 3;

    @Autowired
    public UserController(final UserService userService, final EnterpriseService enterpriseService, final ExperienceService experienceService,
                          final EducationService educationService, final UserSkillService userSkillService,
                          final EmailService emailService, final JobOfferService jobOfferService,
                          final JobOfferSkillService jobOfferSkillService, final ContactService contactService,
                          final ImageService imageService, final CategoryService categoryService, final SkillService skillService,
                          final AuthUserDetailsService authUserDetailsService){
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
        this.authUserDetailsService = authUserDetailsService;
    }
    @RequestMapping(value = "/home", method = { RequestMethod.GET })
    public ModelAndView home(Authentication loggedUser, @RequestParam(value = "page", defaultValue = "1") final int page,
                             @Valid @ModelAttribute("userFilterForm") final UserFilterForm filterForm,
                             @ModelAttribute("contactOrderForm") final ContactOrderForm contactOrderForm,
                             HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("userHome");

        long categoryID;
        try {
            categoryID = Long.parseLong(filterForm.getCategory());
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid CategoryID {} in 'home'", filterForm.getCategory());
            categoryID = 0;
        }

        BigDecimal minSalaryBigDec = filterForm.getMinSalary();
        BigDecimal maxSalaryBigDec = filterForm.getMaxSalary();
        Category category = categoryService.findById(categoryID).orElse(null);
        String modality = filterForm.getModality();
        String searchTerm = filterForm.getTerm();
        String minSalary = minSalaryBigDec == null ? "" : String.valueOf(minSalaryBigDec);
        String maxSalary = maxSalaryBigDec == null ? "" : String.valueOf(maxSalaryBigDec);

        final long jobOffersCount = jobOfferService.getActiveJobOffersCount(category, modality, searchTerm, minSalaryBigDec, maxSalaryBigDec);

        final List<JobOffer> jobOfferList = jobOfferService.getJobOffersListByFilters(category, modality, searchTerm, minSalaryBigDec,
                maxSalaryBigDec, page - 1, JOB_OFFERS_PER_PAGE);

        StringBuilder path = new StringBuilder()
                .append("?category=").append(filterForm.getCategory())
                .append("&modality=").append(modality)
                .append("&minSalary=").append(minSalary)
                .append("&maxSalary=").append(maxSalary)
                .append("&term=").append(searchTerm);
//                .append("&sortBy=").append(filterForm.getSortBy());


        mav.addObject("jobOffers", jobOfferList);
        mav.addObject("categories", categoryService.getAllCategories());
        mav.addObject("pages",  PaginationHelper.getMaxPages(jobOffersCount, JOB_OFFERS_PER_PAGE));
        mav.addObject("currentPage", page);
        mav.addObject("path", path.toString());
        mav.addObject("loggedUserID", authUserDetailsService.getLoggerUserId(loggedUser));
        return mav;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/applyToJobOffer/{jobOfferId:[0-9]+}/{currentPage:[0-9]+}")
    public ModelAndView applyToJobOffer(Authentication loggedUser,
                                       @PathVariable("jobOfferId") final long jobOfferId,
                                        @PathVariable("currentPage") final long currentPage) {

        User user = userService.findByEmail(loggedUser.getName()).orElseThrow(() -> {
            LOGGER.error("User {} not found", loggedUser.getName());
            return new UserNotFoundException();
        });

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Job offer {} not found in cancelJobOffer()", jobOfferId);
            return new JobOfferNotFoundException();
        });

        Enterprise enterprise = enterpriseService.findById(jobOffer.getEnterpriseID()).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in applyToJobOffer()", jobOffer.getEnterpriseID());
            return new UserNotFoundException();
        });

        if(contactService.alreadyContacted(user.getId(), jobOfferId))
            return new ModelAndView("redirect:/home?page=" + currentPage);

        contactService.addContact(enterprise, user, jobOffer, FilledBy.USER);
        emailService.sendApplicationEmail(enterprise, user, jobOffer.getPosition(), LocaleContextHolder.getLocale());

        return new ModelAndView("redirect:/home?page=" + currentPage);
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND isUserVisible(#userId) OR canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/profileUser/{userId:[0-9]+}")
    public ModelAndView profileUser(Authentication loggedUser, @PathVariable("userId") final long userId) {

        final ModelAndView mav = new ModelAndView("userProfile");

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User {} not found", loggedUser.getName());
            return new UserNotFoundException();
        });

        mav.addObject("user", user);
        mav.addObject("experiences", experienceService.findByUser(user));
        mav.addObject("educations", educationService.findByUser(user));
        mav.addObject("skills", user.getSkills());
        mav.addObject("loggedUserID", authUserDetailsService.getLoggerUserId(loggedUser));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/cancelApplication/{userId:[0-9]+}/{jobOfferId:[0-9]+}")
    public ModelAndView cancelApplication(Authentication loggedUser,
                                       @PathVariable("userId") final long userId,
                                       @PathVariable("jobOfferId") final long jobOfferId) {

        User user = userService.findByEmail(loggedUser.getName()).orElseThrow(() -> {
            LOGGER.error("User {} not found", loggedUser.getName());
            return new UserNotFoundException();
        });

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Job offer {} not found in cancelJobOffer()", jobOfferId);
            return new JobOfferNotFoundException();
        });

        Enterprise enterprise = enterpriseService.findById(jobOffer.getEnterpriseID()).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in applyToJobOffer()", jobOffer.getEnterpriseID());
            return new UserNotFoundException();
        });

        boolean cancelled = contactService.cancelJobOffer(user, jobOffer);
        if(cancelled)
            emailService.sendCancelApplicationEmail(enterprise, user, jobOffer.getPosition(), LocaleContextHolder.getLocale());

        return new ModelAndView("redirect:/applicationsUser/" + user.getId());
    }


    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/answerJobOffer/{userId:[0-9]+}/{jobOfferId:[0-9]+}/{answer}")
    public ModelAndView answerJobOffer(Authentication loggedUser,
                                       @PathVariable("userId") final long userId,
                                       @PathVariable("jobOfferId") final long jobOfferId,
                                       @PathVariable("answer") final long answer) {

        User user = userService.findById(userId).orElseThrow(() -> {
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
            boolean rejected = contactService.rejectJobOffer(user, jobOffer);
            if(rejected)
                emailService.sendReplyJobOfferEmail(enterprise, user.getName(), user.getEmail(), jobOffer.getPosition(), REJECT, LocaleContextHolder.getLocale());
        }
        else {
            boolean accepted = contactService.acceptJobOffer(user, jobOffer);
            if(accepted)
                emailService.sendReplyJobOfferEmail(enterprise, user.getName(), user.getEmail(), jobOffer.getPosition(), ACCEPT, LocaleContextHolder.getLocale());
        }

        return new ModelAndView("redirect:/notificationsUser/" + userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/notificationsUser/{userId:[0-9]+}")
    public ModelAndView notificationsUser(Authentication loggedUser, @PathVariable("userId") final long userId,
                                          @RequestParam(value = "status",defaultValue = "") final String status,
                                          @ModelAttribute("contactOrderForm") final ContactOrderForm contactOrderForm,
                                          @RequestParam(value = "page", defaultValue = "1") final int page,
                                          HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("userNotifications");

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });

        List<Contact> contactList;
        StringBuilder path = new StringBuilder().append("/notificationsUser/").append(userId);

        if(request.getParameter("status") == null) {
            contactList = contactService.getContactsForUser(user, FilledBy.ENTERPRISE, SortHelper.getSortBy(contactOrderForm.getSortBy()),
                    page - 1, CONTACTS_PER_PAGE);
            path.append("?");
        }
        else {
            contactList = contactService.getContactsForUser(user, FilledBy.ENTERPRISE, status, SortHelper.getSortBy(contactOrderForm.getSortBy()),
                    page - 1, CONTACTS_PER_PAGE);
            path.append("?status=").append(status);
        }

        path.append("sortBy=").append(contactOrderForm.getSortBy());
        long contactsCount = status.isEmpty()? contactService.getContactsCountForUser(userId, FilledBy.ENTERPRISE) : contactList.size();

        mav.addObject("user", user);
        mav.addObject("loggedUserID", authUserDetailsService.getLoggerUserId(loggedUser));
        mav.addObject("contactList", contactList);
        mav.addObject("status", status);
        mav.addObject("sortById", contactOrderForm.getSortBy());
        mav.addObject("path", path);
        mav.addObject("pages", PaginationHelper.getMaxPages(contactsCount, CONTACTS_PER_PAGE));
        mav.addObject("currentPage", page);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/applicationsUser/{userId:[0-9]+}")
    public ModelAndView applicationsUser(Authentication loggedUser, @PathVariable("userId") final long userId,
                                         @RequestParam(value = "status",defaultValue = "") final String status,
                                         @ModelAttribute("contactOrderForm") final ContactOrderForm contactOrderForm,
                                         @RequestParam(value = "page", defaultValue = "1") final int page,
                                         HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("userApplications");

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });

        List<Contact> contactList;
        StringBuilder path = new StringBuilder().append("/applicationsUser/").append(userId);
        if(request.getParameter("status") == null) {
            contactList = contactService.getContactsForUser(user, FilledBy.USER, SortHelper.getSortBy(contactOrderForm.getSortBy()),
                    page - 1, CONTACTS_PER_PAGE);
            path.append("?").append("&");
        }
        else {
            contactList = contactService.getContactsForUser(user, FilledBy.USER, status, SortHelper.getSortBy(contactOrderForm.getSortBy()),
                    page - 1, CONTACTS_PER_PAGE);
            path.append("?status=").append(status).append("&");
        }

        path.append("sortBy=").append(contactOrderForm.getSortBy());

        long contactsCount = status.isEmpty()? contactService.getContactsCountForUser(userId, FilledBy.USER) : contactList.size();

        mav.addObject("user", user);
        mav.addObject("loggedUserID", authUserDetailsService.getLoggerUserId(loggedUser));
        mav.addObject("contactList", contactList);
        mav.addObject("status", status);
        mav.addObject("sortById", contactOrderForm.getSortBy());
        mav.addObject("path", path);
        mav.addObject("pages", PaginationHelper.getMaxPages(contactsCount, CONTACTS_PER_PAGE));
        mav.addObject("currentPage", page);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createExperience/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formExperience(Authentication loggedUser, @ModelAttribute("experienceForm") final ExperienceForm experienceForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("userExperienceForm");

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });

        mav.addObject("user", user);
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

        Integer monthTo = monthToIsEmpty ? null : DateHelper.monthToNumber(formMonthTo);
        Integer monthFrom = DateHelper.monthToNumber(experienceForm.getMonthFrom());

        boolean invalidDate = !yearToIsEmpty && !monthToIsEmpty && !yearWrongFormat && yearFrom != null &&
                !DateHelper.isDateValid(monthFrom, yearFrom, monthTo, yearTo);


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

        Experience experience = experienceService.create(user, DateHelper.monthToNumber(experienceForm.getMonthFrom()), Integer.parseInt(experienceForm.getYearFrom()),
                monthTo, yearTo,experienceForm.getCompany(), experienceForm.getJob(), experienceForm.getJobDesc());

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
        final ModelAndView mav = new ModelAndView("userEducationForm");

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });

        mav.addObject("user", user);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createEducation/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createEducation(Authentication loggedUser, @Valid @ModelAttribute("educationForm") final EducationForm educationForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        boolean isYearValid = DateHelper.isIntervalValid(educationForm.getYearFrom(), educationForm.getYearTo());
        boolean isMonthValid = DateHelper.isIntervalValid(DateHelper.monthToNumber(educationForm.getMonthFrom()), DateHelper.monthToNumber(educationForm.getMonthTo()));

        if (errors.hasErrors() || !isYearValid || !isMonthValid) {
            if(!isYearValid)
                errors.rejectValue("yearTo", "InvalidDate", "End date cannot be before initial date");
            else if (!isMonthValid)
                errors.rejectValue("monthTo", "InvalidDate", "End date cannot be before initial date");
            LOGGER.warn("Education form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return formEducation(loggedUser, educationForm, userId);
        }

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });

        Education education = educationService.add(user, DateHelper.monthToNumber(educationForm.getMonthFrom()), Integer.parseInt(educationForm.getYearFrom()),
                DateHelper.monthToNumber(educationForm.getMonthTo()), Integer.parseInt(educationForm.getYearTo()), educationForm.getDegree(),
                educationForm.getCollege(), educationForm.getComment());

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
        final ModelAndView mav = new ModelAndView("userSkillsForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        }));
        return mav;
    }

    @Transactional
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

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });

        mav.addObject("user", user);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/uploadProfileImage/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView uploadImage(Authentication loggedUser, @Valid @ModelAttribute("imageForm") final ImageForm imageForm, final BindingResult errors,
                                    @PathVariable("userId") final long userId) throws IOException {
        if (errors.hasErrors()) {
            return formImage(loggedUser, imageForm, userId);
        }

        Image image = imageService.uploadImage(imageForm.getImage().getBytes());
        userService.updateProfileImage(userId, image);

        return new ModelAndView("redirect:/profileUser/" + userId);
    }

    @RequestMapping(value = "/{userId:[0-9]+}/image/{imageId}", method = RequestMethod.GET, produces = "image/*")
    public @ResponseBody byte[] getProfileImage(@PathVariable("userId") final long userId, @PathVariable("imageId") final int imageId) {
        LOGGER.debug("Trying to access profile image");

        Image profileImage = imageService.getImage(imageId).orElseThrow(() -> {
            LOGGER.error("Error loading image {}", imageId);
            return new ImageNotFoundException();
        });

        LOGGER.info("Profile image accessed.");
        return profileImage.getBytes();
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

        Category category;
        String categoryName =  editUserForm.getCategory();
        if(categoryName.isEmpty()) {
            category = null;
        } else {
            category = categoryService.findByName(categoryName).orElseThrow(() -> {
                LOGGER.error("Category not found");
                return new CategoryNotFoundException();
            });
        }

        userService.updateUserInformation(userId, editUserForm.getName(), editUserForm.getAboutMe(), editUserForm.getLocation(),
                editUserForm.getPosition(), category, editUserForm.getLevel());
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


}
