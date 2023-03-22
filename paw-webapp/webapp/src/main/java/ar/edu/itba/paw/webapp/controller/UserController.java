package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.SortBy;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.models.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.models.helpers.DateHelper;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

//@Controller
/*public class UserController {
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
    // DONE
    @RequestMapping(value = "/home", method = { RequestMethod.GET })
    public ModelAndView home(Authentication loggedUser, @RequestParam(value = "page", defaultValue = "1") final int page,
                             @Valid @ModelAttribute("userFilterForm") final UserFilterForm filterForm,
                             @ModelAttribute("contactOrderForm") final ContactOrderForm contactOrderForm,
                             HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("userHome");

        User user = userService.findByEmail(loggedUser.getName()).orElseThrow(() -> {
            LOGGER.error("User {} not found", loggedUser.getName());
            return new UserNotFoundException();
        });

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

        mav.addObject("jobOffers", jobOfferList);
        mav.addObject("contactedJobOffers", userService.getUserContactMap(user.getContacts()));
        mav.addObject("categories", categoryService.getAllCategories());
        mav.addObject("pages",  PaginationHelper.getMaxPages(jobOffersCount, JOB_OFFERS_PER_PAGE));
        mav.addObject("currentPage", page);
        mav.addObject("path", path.toString());
        mav.addObject("loggedUserID", authUserDetailsService.getLoggerUserId(loggedUser));
        return mav;
    }
    // DONE
    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/applyToJobOffer/{jobOfferId:[0-9]+}")
    public ModelAndView applyToJobOffer(Authentication loggedUser,
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

        if(contactService.alreadyContacted(user.getId(), jobOfferId))
            return new ModelAndView("redirect:/jobOffer/" + jobOfferId);

        contactService.addContact(enterprise, user, jobOffer, FilledBy.USER);
        emailService.sendApplicationEmail(enterprise, user, jobOffer.getPosition(), LocaleContextHolder.getLocale());

        return new ModelAndView("redirect:/applicationsUser/" + user.getId());
    }
    // DONE
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
    // DONE
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
    // DONE
    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/acceptJobOffer/{userId:[0-9]+}/{jobOfferId:[0-9]+}")
    public ModelAndView acceptJobOffer(Authentication loggedUser,
                                       @PathVariable("userId") final long userId,
                                       @PathVariable("jobOfferId") final long jobOfferId) {

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

        boolean accepted = contactService.acceptJobOffer(user, jobOffer);
        if(accepted)
            emailService.sendAcceptJobOfferEmail(enterprise, user.getName(), user.getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());

        return new ModelAndView("redirect:/notificationsUser/" + userId);
    }
    // DONE
    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/rejectJobOffer/{userId:[0-9]+}/{jobOfferId:[0-9]+}")
    public ModelAndView rejectJobOffer(Authentication loggedUser,
                                       @PathVariable("userId") final long userId,
                                       @PathVariable("jobOfferId") final long jobOfferId) {

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

        boolean rejected = contactService.rejectJobOffer(user, jobOffer);
        if(rejected)
            emailService.sendRejectJobOfferEmail(enterprise, user.getName(), user.getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());

        return new ModelAndView("redirect:/notificationsUser/" + userId);
    }
    // DONE
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
    // DONE
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
    // DONE
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
    // DONE
    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId) AND isExperienceOwner(#userId, #experienceId)")
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
    // DONE
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
    // DONE
    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId) AND isEducationOwner(#userId, #educationId)")
    @RequestMapping(value = "/deleteEducation/{userId:[0-9]+}/{educationId:[0-9]+}", method = { RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET })
    public ModelAndView deleteEducation(Authentication loggedUser, @PathVariable("userId") final long userId, @PathVariable("educationId") final long educationId) {
        educationService.deleteEducation(educationId);
        return new ModelAndView("redirect:/profileUser/" + userId);
    }
    // DONE
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
    // DONE
    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/deleteSkill/{userId:[0-9]+}/{skillId:[0-9]+}", method = { RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET })
    public ModelAndView deleteSkill(Authentication loggedUser, @PathVariable("userId") final long userId, @PathVariable("skillId") final long skillId) {
        userSkillService.deleteSkillFromUser(userId, skillId);
        return new ModelAndView("redirect:/profileUser/" + userId);
    }
    // DONE
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
    // DONE
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
    // DONE
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
    // DONE
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
    // DONE
    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/hideUserProfile/{userId:[0-9]+}", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView hideUserProfile(Authentication loggedUser, @PathVariable("userId") final long userId) {
        userService.hideUserProfile(userId);
        return new ModelAndView("redirect:/profileUser/" + userId);
    }
    // DONE
    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/showUserProfile/{userId:[0-9]+}", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView showUserProfile(Authentication loggedUser, @PathVariable("userId") final long userId) {
        userService.showUserProfile(userId);
        return new ModelAndView("redirect:/profileUser/" + userId);
    }
}*/


public class UserController {

    private static final int PAGE_SIZE = 10;
    private static final int JOB_OFFERS_PER_PAGE = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService us;
    @Autowired
    private JobOfferService jobOfferService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private EducationService educationService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private UserSkillService userSkillService;
    //private final EmailService emailService;
    @Autowired
    protected AuthenticationManager authenticationManager;


    @Context
    private UriInfo uriInfo;
    private final ImageService imageService;

    // TODO: REVISAR EL TEMA DE LOS PERMISOS DE CADA USUARIOS PARA CADA METODO

    @Autowired
    public UserController(final UserService userService, final CategoryService categoryService, final JobOfferService jobOfferService,
                          final EnterpriseService enterpriseService, final ContactService contactService, final ExperienceService experienceService,
                          final EducationService educationService, final SkillService skillService, final UserSkillService userSkillService,
                          ImageService imageService) {
        this.us = userService;
        this.categoryService = categoryService;
        this.jobOfferService = jobOfferService;
        this.enterpriseService = enterpriseService;
        this.contactService = contactService;
        this.experienceService = experienceService;
        this.educationService = educationService;
        this.skillService = skillService;
        this.userSkillService = userSkillService;
        this.imageService = imageService;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response listUsers(@QueryParam("page") @DefaultValue("1") final int page) {
        final List<UserDTO> allUsers = us.getVisibleUsers(page-1, PAGE_SIZE).stream().map(u -> UserDTO.fromUser(uriInfo,u)).collect(Collectors.toList());

        if (allUsers.isEmpty()) {
            return Response.noContent().build();
        }

        //TODO: AGREGAR FILTROS

        return Response.ok(new GenericEntity<List<UserDTO>>(allUsers) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
    public Response createUser (@Valid final UserForm userForm /*, final BindingResult errors, HttpServletRequest request*/) {
        //TODO: Desarrollar errores del formulario como "reenvio la pagina"
        /*if (errors.hasErrors()) {
            LOGGER.warn("User register form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return;
        }*/

        Optional<Category> optCategory = categoryService.findByName(userForm.getCategory());
        if (!optCategory.isPresent()) {
            //TODO: Desarrollar errores
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final User user = us.register(userForm.getEmail(), userForm.getPassword(), userForm.getName(), userForm.getCity(), optCategory.get(),
                userForm.getPosition(), userForm.getAboutMe(), userForm.getLevel());

        //TODO: revisar uso de mail y autologeado
        //emailService.sendRegisterUserConfirmationEmail(u, LocaleContextHolder.getLocale());
        //authWithAuthManager(request, userForm.getEmail(), userForm.getPassword());

        LOGGER.debug("A new user was registered under id: {}", user.getId());
        LOGGER.info("A new user was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
        return Response.created(uri).build();
    }

    //FIXME: Pasarlo a JobOfferController?
    /*@GET
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response JobOfferList(@QueryParam("page") @DefaultValue("1") final int page, @Valid final UserFilterForm filterForm,
                             final ContactOrderForm contactOrderForm*//*, HttpServletRequest request*//*) {

        Optional<Category> optCategory = categoryService.findByName(filterForm.getCategory());
        if (!optCategory.isPresent()) {
            //TODO: Desarrollar errores
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

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

        final List<JobOfferDTO> jobOfferList = jobOfferService.getJobOffersListByFilters(category, modality, searchTerm, minSalaryBigDec,
                maxSalaryBigDec, page - 1, JOB_OFFERS_PER_PAGE).stream().map(u -> JobOfferDTO.fromJobOffer(uriInfo,u)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<JobOfferDTO>>(jobOfferList) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }*/

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getById(@PathParam("id") final long id) {
        Optional<UserDTO> maybeUser = us.findById(id).map(u -> UserDTO.fromUser(uriInfo,u));
        if (!maybeUser.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(maybeUser.get()).build();
    }

    /*@DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") final long id) {
        //us.deleteById(id);
        return Response.noContent().build();
    }*/

    //TODO: Agregar orden y filtros
    @GET
    @Path("/{id}/applications")
    public Response getApplications(@PathParam("id") final long id, @QueryParam("page") @DefaultValue("1") final int page){
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<ApplicationDTO> applications = contactService.getContactsForUser(optUser.get(), FilledBy.USER, SortBy.ANY, page-1, PAGE_SIZE)
                .stream().map(contact -> ApplicationDTO.fromContact(uriInfo, contact)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ApplicationDTO>>(applications) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }

    //TODO: Implementar GET /{id}/applications/{jobOfferId}

    @POST
    //@Transactional
    //@PreAuthorize("hasRole('ROLE_USER')")
    @Path("/{id}/applications")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
    public Response applyToJobOffer(@PathParam("id") final long id, @QueryParam("jobOfferId") final long jobOfferId){
        Optional<User> optUser = us.findById(id);
        if (!optUser.isPresent()) {
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<JobOffer> optJobOffer = jobOfferService.findById(jobOfferId);
        if(!optJobOffer.isPresent()){
            LOGGER.error("Job offer with ID={} not found", jobOfferId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        long enterpriseId = optJobOffer.get().getEnterpriseID();
        Optional<Enterprise> optEnterprise = enterpriseService.findById(enterpriseId);
        if(!optEnterprise.isPresent()){
            LOGGER.error("Enterprise with ID={} not found", enterpriseId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        //long userId = optUser.get().getId();
        if(contactService.alreadyContacted(id, jobOfferId)) {
            LOGGER.error("User with ID={} has already applied to job offer with ID={}", id, jobOfferId);
            return Response.status(Response.Status.CONFLICT).build();
        }

        contactService.addContact(optEnterprise.get(), optUser.get(), optJobOffer.get(), FilledBy.USER);
        //emailService.sendApplicationEmail(optEnterprise.get(), optUser.get(), optJobOffer.get().getPosition(), LocaleContextHolder.getLocale());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(jobOfferId)).build();
        return Response.created(uri).build();
    }

    @PUT
    //@PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @Path("/{id}/applications/{jobOfferId}")
    public Response cancelApplication(@PathParam("id") final long id, @PathParam("jobOfferId") final long jobOfferId) {
        Optional<User> optUser = us.findById(id);
        if (!optUser.isPresent()) {
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<JobOffer> optJobOffer = jobOfferService.findById(jobOfferId);
        if(!optJobOffer.isPresent()){
            LOGGER.error("Job offer with ID={} not found", jobOfferId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(!contactService.cancelJobOffer(optUser.get(), optJobOffer.get()))
            return Response.status(Response.Status.BAD_REQUEST).build();

        //TODO: Otra opcion seria devolver la nueva application (201: CREATED)
        //emailService.sendCancelApplicationEmail(optEnterprise.get(), optUser.get(), optJobOffer.get().getPosition(), LocaleContextHolder.getLocale());

        return Response.ok(optJobOffer.get()).build();
    }

    @PUT
    //@PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @Path("/{id}/notifications/{jobOfferId}")
    public Response statusJobOffer(@PathParam("id") final long id, @PathParam("jobOfferId") final long jobOfferId, @QueryParam("newStatus") final String NewStatus) {

        Optional<User> optUser = us.findById(id);
        if (!optUser.isPresent()) {
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<JobOffer> optJobOffer = jobOfferService.findById(jobOfferId);
        if(!optJobOffer.isPresent()){
            LOGGER.error("Job offer with ID={} not found", jobOfferId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (NewStatus.compareTo("aceptado") == 0) {
            if(!contactService.acceptJobOffer(optUser.get(), optJobOffer.get()))
                return Response.status(Response.Status.BAD_REQUEST).build();
            //TODO: Otra opcion seria devolver la nueva application (201: CREATED)
            //emailService.sendCancelApplicationEmail(optEnterprise.get(), optUser.get().getName(), optUser.get().getEmail(), optJobOffer.get().getPosition(), LocaleContextHolder.getLocale());
        }
        else if (NewStatus.compareTo("rechazado") == 0) {
            if(!contactService.rejectJobOffer(optUser.get(), optJobOffer.get()))
                return Response.status(Response.Status.BAD_REQUEST).build();
            //TODO: Otra opcion seria devolver la nueva application (201: CREATED)
            //emailService.sendRejectJobOfferEmail(optEnterprise.get(), optUser.get().getName(), optUser.get().getEmail(), optJobOffer.get().getPosition(), LocaleContextHolder.getLocale());
        }
        return Response.ok().build();

    }

    //TODO: metodo ambiguo con el accept -> diferenciar path con queryParam
/*    @PUT
    //@PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @Path("/{id}/notifications/{jobOfferId}")
    public Response rejectJobOffer(@PathParam("id") final long id, @PathParam("jobOfferId") final long jobOfferId) {

        Optional<User> optUser = us.findById(id);
        if (!optUser.isPresent()) {
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<JobOffer> optJobOffer = jobOfferService.findById(jobOfferId);
        if(!optJobOffer.isPresent()){
            LOGGER.error("Job offer with ID={} not found", jobOfferId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(!contactService.rejectJobOffer(optUser.get(), optJobOffer.get()))
            return Response.status(Response.Status.BAD_REQUEST).build();
        //TODO: Otra opcion seria devolver la nueva application (201: CREATED)
        //emailService.sendRejectJobOfferEmail(optEnterprise.get(), optUser.get().getName(), optUser.get().getEmail(), optJobOffer.get().getPosition(), LocaleContextHolder.getLocale());

        return Response.ok().build();
    }*/

    //TODO: REVISAR METODO Y COMO SERIA AGREGAR EL FILTRO POR STATUS
    @GET
    @Path("/{id}/notifications")
    public Response getNotifications(@PathParam("id") final long id, @QueryParam("page") @DefaultValue("1") final int page) {
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<NotificationDTO> notifications = contactService.getContactsForUser(optUser.get(), FilledBy.ENTERPRISE, SortBy.ANY, page-1, PAGE_SIZE)
                .stream().map(contact -> NotificationDTO.fromContact(uriInfo, contact)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<NotificationDTO>>(notifications) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }

    @GET
    @Path("/{id}/experiences")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getExperiences(@PathParam("id") final long id, @QueryParam("page") @DefaultValue("1") final int page) {
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<ExperienceDTO> experiences = experienceService.findByUser(optUser.get())
                .stream().map(exp -> ExperienceDTO.fromExperience(uriInfo, exp)).collect(Collectors.toList());

        //TODO: Generar links con sentido
        return Response.ok(new GenericEntity<List<ExperienceDTO>>(experiences) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }

    @GET
    @Path("/{id}/experiences/{expId}")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getExperienceById(@PathParam("id") final long id, @PathParam("expId") final long expId) {
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<ExperienceDTO> optExperience = experienceService.findById(expId).map(exp -> ExperienceDTO.fromExperience(uriInfo, exp));
        if (!optExperience.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(optExperience.get()).build();
    }

    @POST
    //@PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @Path("/{id}/experiences")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
    public Response addExperience(@PathParam("id") final long id, @Valid final ExperienceForm experienceForm /*, final BindingResult errors*/){
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        //TODO: Eliminar esta logica del controller
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
        }

        Integer monthTo = monthToIsEmpty ? null : DateHelper.monthToNumber(formMonthTo);
        Integer monthFrom = DateHelper.monthToNumber(experienceForm.getMonthFrom());

        boolean invalidDate = !yearToIsEmpty && !monthToIsEmpty && !yearWrongFormat && yearFrom != null &&
                !DateHelper.isDateValid(monthFrom, yearFrom, monthTo, yearTo);

        /*
        if (errors.hasErrors() || yearFromIsEmpty || yearWrongFormat || monthOrYearEmpty || invalidDate) {
            if(monthOrYearEmpty)
                errors.rejectValue("yearTo", "YearOrMonthEmpty", "You must pick a year and month, or let both fields empty");
            else if (invalidDate)
                errors.rejectValue("yearTo", "InvalidDate", "End date cannot be before initial date");

            LOGGER.warn("Experience form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }*/

        Experience experience = experienceService.create(optUser.get(), DateHelper.monthToNumber(experienceForm.getMonthFrom()),
                Integer.parseInt(experienceForm.getYearFrom()), monthTo, yearTo,experienceForm.getCompany(), experienceForm.getJob(), experienceForm.getJobDesc());

        LOGGER.debug("A new experience was registered under id: {}", experience.getId());
        LOGGER.info("A new experience was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(experience.getId())).build();
        return Response.created(uri).build();
    }

    @DELETE
    //@PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId) AND isExperienceOwner(#userId, #experienceId)")
    @Path("/{id}/experiences/{expId}")
    public Response deleteExperienceById(@PathParam("id") final long id, @PathParam("expId") final long expId) {
        experienceService.deleteExperience(expId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/educations")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getEducations(@PathParam("id") final long id, @QueryParam("page") @DefaultValue("1") final int page) {
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<EducationDTO> educations = educationService.findByUser(optUser.get())
                .stream().map(ed -> EducationDTO.fromEducation(uriInfo, ed)).collect(Collectors.toList());

        //TODO: Generar links con sentido
        return Response.ok(new GenericEntity<List<EducationDTO>>(educations) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }

    @GET
    @Path("/{id}/educations/{educationId}")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getEducationById(@PathParam("id") final long id, @PathParam("educationId") final long educationId) {
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<EducationDTO> optEducation = educationService.findById(educationId).map(ed -> EducationDTO.fromEducation(uriInfo, ed));
        if (!optEducation.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(optEducation.get()).build();
    }

    @POST
    //@PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @Path("/{id}/educations")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
    public Response addEducation(@PathParam("id") final long id, @Valid final EducationForm educationForm /*, final BindingResult errors*/){
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        //TODO: Eliminar esta logica del controller
        /*
        boolean isYearValid = DateHelper.isIntervalValid(educationForm.getYearFrom(), educationForm.getYearTo());
        boolean isMonthValid = DateHelper.isIntervalValid(DateHelper.monthToNumber(educationForm.getMonthFrom()), DateHelper.monthToNumber(educationForm.getMonthTo()));

        if (errors.hasErrors() || !isYearValid || !isMonthValid) {
            if(!isYearValid)
                errors.rejectValue("yearTo", "InvalidDate", "End date cannot be before initial date");
            else if (!isMonthValid)
                errors.rejectValue("monthTo", "InvalidDate", "End date cannot be before initial date");
            LOGGER.warn("Education form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        */

        Education education = educationService.add(optUser.get(), DateHelper.monthToNumber(educationForm.getMonthFrom()), Integer.parseInt(educationForm.getYearFrom()),
                DateHelper.monthToNumber(educationForm.getMonthTo()), Integer.parseInt(educationForm.getYearTo()), educationForm.getDegree(),
                educationForm.getCollege(), educationForm.getComment());

        LOGGER.debug("A new education was registered under id: {}", education.getId());
        LOGGER.info("A new education was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(education.getId())).build();
        return Response.created(uri).build();
    }

    @DELETE
    //@PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId) AND isExperienceOwner(#userId, #experienceId)")
    @Path("/{id}/educations/{educationId}")
    public Response deleteEducationById(@PathParam("id") final long id, @PathParam("educationId") final long educationId) {
        educationService.deleteEducation(educationId);
        return Response.noContent().build();
    }
    @GET
    @Path("/{id}/skills")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getSkills(@PathParam("id") final long id, @QueryParam("page") @DefaultValue("1") final int page) {
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<UserSkillDTO> skills = userSkillService.getSkillsForUser(optUser.get())
                .stream().map(s -> UserSkillDTO.fromSkill(uriInfo, optUser.get(), s)).collect(Collectors.toList());

        //TODO: Generar links con sentido
        return Response.ok(new GenericEntity<List<UserSkillDTO>>(skills) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }

    @GET
    @Path("/{id}/skills/{skillId}")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getSkillById(@PathParam("id") final long id, @PathParam("skillId") final long skillId) {
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<UserSkillDTO> optSkill = skillService.findById(skillId).map(s -> UserSkillDTO.fromSkill(uriInfo, optUser.get(), s));
        if (!optSkill.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(optSkill.get()).build();
    }

    @POST
    //@PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @Path("/{id}/skills")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
    public Response addSkill(@PathParam("id") final long id, @Valid final SkillForm skillForm /*, final BindingResult errors*/){
        Optional<User> optUser = us.findById(id);
        if(!optUser.isPresent()){
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Skill skill = skillService.findByDescriptionOrCreate(skillForm.getSkill());

        /*if (errors.hasErrors() || userSkillService.alreadyExists(skill, user)) {
            errors.rejectValue("skill", "ExistingSkillForUser", "You already have this skill for this user.");
            LOGGER.warn("Skill form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return formSkill(loggedUser, skillForm, userId);
        }*/

        userSkillService.addSkillToUser(skill, optUser.get());

        LOGGER.debug("A new skill was registered under id: {}", skill.getId());
        LOGGER.info("A new skill was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(skill.getId())).build();
        return Response.created(uri).build();
    }

    @DELETE
    //@PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId) AND isExperienceOwner(#userId, #experienceId)")
    @Path("/{id}/skills/{skillId}")
    public Response deleteSkillFromUserById(@PathParam("id") final long id, @PathParam("skillId") final long skillId) {
        userSkillService.deleteSkillFromUser(id, skillId);
        return Response.noContent().build();
    }

    @PUT
    //@PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @Path("/{id}/profile")
    public Response editUser(@Valid final EditUserForm editUserForm, /*final BindingResult errors,*/ @PathParam("id") final long id) {
        /*if (errors.hasErrors()) {
            return formEditUser(loggedUser, editUserForm, userId);
        }*/

        Optional<Category> optCategory = categoryService.findByName(editUserForm.getCategory());
        if (!optCategory.isPresent()) {
            //TODO: Desarrollar errores
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        us.updateUserInformation(id, editUserForm.getName(), editUserForm.getAboutMe(), editUserForm.getLocation(),
                editUserForm.getPosition(), optCategory.get(), editUserForm.getLevel());

        return Response.noContent().build();
    }

    @PUT
    // @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @Path("/{id}/visibility")
    public Response hideUserProfile(@PathParam("id") final long id) {
        Optional<User> optUser = us.findById(id);
        if (!optUser.isPresent()) {
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (optUser.get().getVisibility() == Visibility.VISIBLE.getValue()) {
            us.hideUserProfile(id);
        } else {
            us.showUserProfile(id);
        }

        return Response.ok().build();
    }

    @PUT
    // @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @Path("/{id}/image")
    public Response uploadImage(@PathParam("id") final long id, @Valid final ImageForm imageForm /* , final BindingResult errors */) throws IOException {
        /*if (errors.hasErrors()) {
            return formImage(loggedUser, imageForm, userId);
        }*/

        Image image = imageService.uploadImage(imageForm.getImage().getBytes());
        us.updateProfileImage(id, image);

        return Response.ok().build(); //TODO: NO SE QUE DEVOLVER
    }

    @GET
    @Path("/{id}/image")
    @Produces(value = {"image/webp"})
    public Response getProfileImage(@PathParam("id") final long id) {
        LOGGER.debug("Trying to access profile image");

        Optional<User> optUser = us.findById(id);
        if (!optUser.isPresent()) {
            LOGGER.error("User with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        long imageId = optUser.get().getImage().getId();

        Image profileImage = imageService.getImage(imageId).orElseThrow(() -> {
            LOGGER.error("Error loading image {}", imageId);
            return new ImageNotFoundException();
        });

        LOGGER.info("Profile image accessed.");
        return Response.ok(profileImage.getBytes()).build();
    }

    /** Autologin **/
    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}