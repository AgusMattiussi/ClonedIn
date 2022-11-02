package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;

import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.helpers.PaginationHelper;
import ar.edu.itba.paw.models.helpers.SortHelper;
import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.models.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
public class EnterpriseController {

    private final UserService userService;
    private final EnterpriseService enterpriseService;
    private final CategoryService categoryService;
    private final SkillService skillService;
    private final EmailService emailService;
    private final JobOfferService jobOfferService;
    private final ContactService contactService;
    private final JobOfferSkillService jobOfferSkillService;
    private final ImageService imageService;
    private final AuthUserDetailsService authUserDetailsService;
    @Autowired
    protected AuthenticationManager authenticationManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseController.class);
    private static final int HOME_JOB_OFFERS_PER_PAGE = 8;
    private static final int ENTERPRISE_PROFILE_JOB_OFFERS_PER_PAGE = 3;
    private static final int CONTACTS_PER_PAGE = 10;

    @Autowired
    public EnterpriseController(final UserService userService, final EnterpriseService enterpriseService, final CategoryService categoryService,
                                final SkillService skillService, final EmailService emailService, final JobOfferService jobOfferService,
                                final ContactService contactService, final JobOfferSkillService jobOfferSkillService, final ImageService imageService,
                                final AuthUserDetailsService authUserDetailsService){
        this.userService = userService;
        this.enterpriseService = enterpriseService;
        this.categoryService = categoryService;
        this.skillService = skillService;
        this.emailService = emailService;
        this.jobOfferService = jobOfferService;
        this.contactService = contactService;
        this.jobOfferSkillService = jobOfferSkillService;
        this.imageService = imageService;
        this.authUserDetailsService = authUserDetailsService;
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET })
    public ModelAndView home(Authentication loggedUser, @RequestParam(value = "page", defaultValue = "1") final int page,
                             @Valid @ModelAttribute("enterpriseFilterForm") final EnterpriseFilterForm enterpriseFilterForm,
                             @Valid @ModelAttribute("searchForm") final SearchForm searchForm,
                             HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("enterpriseHome");
        final List<User> usersList;
        final long usersCount;
        StringBuilder path = new StringBuilder();

        //TODO: Revisar esto
        long categoryID;
        try {
            categoryID = Long.parseLong(enterpriseFilterForm.getCategory());
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid CategoryID {} in 'home'", enterpriseFilterForm.getCategory());
            categoryID = 0;
        }

        Category category = categoryService.findById(categoryID).orElse(null);

        String location = enterpriseFilterForm.getLocation();
        String educationLevel = enterpriseFilterForm.getEducationLevel();
        String skillDescription = enterpriseFilterForm.getSkill();
//        String minExperience = enterpriseFilterForm.getMinExperience();
//        String maxExperience = enterpriseFilterForm.getMaxExperience();

        if(request.getParameter("term") == null) {
            usersList = userService.getUsersListByFilters(category, location, educationLevel, skillDescription, page - 1, HOME_JOB_OFFERS_PER_PAGE);
            usersCount = userService.getUsersCountByFilters(category, location, educationLevel, skillDescription);
            path.append("?category=").append(enterpriseFilterForm.getCategory())
                    .append("&location=").append(location)
                    .append("&educationLevel=").append(educationLevel)
                    .append("&skill=").append(skillDescription);
//                    .append("&minExperience=").append(minExperience)
//                    .append("&maxExperience=").append(maxExperience)
//                    .append("&sortBy=").append(enterpriseFilterForm.getSortBy());
        }
        else {
            usersList = userService.getVisibleUsersByNameLike(searchForm.getTerm(),page - 1, HOME_JOB_OFFERS_PER_PAGE);
            usersCount = usersList.size();
            path.append("?term=").append(searchForm.getTerm());
        }


        mav.addObject("users", usersList);
        mav.addObject("categories", categoryService.getAllCategories());
        mav.addObject("skills", skillService.getAllSkills());
        mav.addObject("path", path.toString());
        mav.addObject("pages", PaginationHelper.getMaxPages(usersCount, HOME_JOB_OFFERS_PER_PAGE));
        mav.addObject("currentPage", page);
        mav.addObject("loggedUserID", authUserDetailsService.getLoggerUserId(loggedUser));
        return mav;
    }

    @PreAuthorize("(hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)) OR hasRole('ROLE_USER')")
    @RequestMapping("/profileEnterprise/{enterpriseId:[0-9]+}")
    public ModelAndView profileEnterprise(Authentication loggedUser, @PathVariable("enterpriseId") final long enterpriseId,
                                          @RequestParam(value = "page", defaultValue = "1") final int page) {
        final ModelAndView mav = new ModelAndView("enterpriseProfile");

        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> {
            LOGGER.error("/profile : Enterprise {} not found in profileEnterprise()", loggedUser.getName());
            return new UserNotFoundException();
        });

        List<JobOffer> jobOfferList = jobOfferService.findByEnterprise(enterprise, page - 1, ENTERPRISE_PROFILE_JOB_OFFERS_PER_PAGE);
        List<JobOffer> activeJobOfferList = jobOfferService.findActiveByEnterprise(enterprise, page - 1, ENTERPRISE_PROFILE_JOB_OFFERS_PER_PAGE);

        mav.addObject("enterprise", enterprise);
        mav.addObject("category", categoryService.findById(enterprise.getCategory().getId()));
        mav.addObject("jobOffers", jobOfferList);
        mav.addObject("activeJobOffers", activeJobOfferList);
        mav.addObject("enterprisePages", PaginationHelper.getMaxPages(jobOfferService.getJobOffersCountForEnterprise(enterprise), ENTERPRISE_PROFILE_JOB_OFFERS_PER_PAGE));
        mav.addObject("userPages", PaginationHelper.getMaxPages(jobOfferService.getActiveJobOffersCountForEnterprise(enterprise), ENTERPRISE_PROFILE_JOB_OFFERS_PER_PAGE));
        mav.addObject("currentPage", page);
        mav.addObject("loggedUserID", authUserDetailsService.getLoggerUserId(loggedUser));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/closeJobOffer/{jobOfferId:[0-9]+}")
    public ModelAndView closeJobOffer(Authentication loggedUser,
                                      @PathVariable("jobOfferId") final long jobOfferId,
                                      @RequestParam(value = "eid", defaultValue = "0") long enterpriseId) {

        long loggedUserId = authUserDetailsService.getLoggerUserId(loggedUser);
        Enterprise enterprise = enterpriseService.findById(loggedUserId).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in closeJobOffer()", loggedUser.getName());
            return new UserNotFoundException();
        });

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Job offer {} not found in closeJobOffer()", jobOfferId);
            return new JobOfferNotFoundException();
        });
        
        jobOfferService.closeJobOffer(jobOffer);
        return new ModelAndView("redirect:/profileEnterprise/" + enterprise.getId());
    }

    //TODO: Sirve este metodo?
    @PreAuthorize("hasRole('ROLE_ENTERPRISE')")
    @RequestMapping("/cancelJobOffer/{jobOfferId:[0-9]+}")
    public ModelAndView cancelJobOffer(Authentication loggedUser,
                                      @PathVariable("jobOfferId") final long jobOfferId) {

        long loggedUserId = authUserDetailsService.getLoggerUserId(loggedUser);
        Enterprise enterprise = enterpriseService.findById(loggedUserId).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in cancelJobOffer()", loggedUser.getName());
            return new UserNotFoundException();
        });

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Job offer {} not found in cancelJobOffer()", jobOfferId);
            return new JobOfferNotFoundException();
        });

        jobOfferService.cancelJobOffer(jobOffer);
        return new ModelAndView("redirect:/profileEnterprise/" + enterprise.getId());
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/cancelJobOffer/{userId:[0-9]+}/{jobOfferId:[0-9]+}")
    public ModelAndView cancelJobOffer(Authentication loggedUser,
                                      @PathVariable("userId") final long userId,
                                      @PathVariable("jobOfferId") final long jobOfferId,
                                      @RequestParam(value = "eid", defaultValue = "0") long enterpriseId) {

        long loggedUserId = authUserDetailsService.getLoggerUserId(loggedUser);
        Enterprise enterprise = enterpriseService.findById(loggedUserId).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in cancelJobOffer()", loggedUser.getName());
            return new UserNotFoundException();
        });
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Job Offer {} not found in cancelJobOffer()", jobOfferId);
            return new JobOfferNotFoundException();
        });
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User {} not found in cancelJobOffer()", userId);
            return new UserNotFoundException();
        });

        contactService.cancelJobOffer(user, jobOffer);
        emailService.sendCancelJobOfferEmail(user, enterprise.getName(), jobOffer.getPosition(), LocaleContextHolder.getLocale());

        return new ModelAndView("redirect:/contactsEnterprise/" + enterprise.getId());
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/answerApplication/{userId:[0-9]+}/{jobOfferId:[0-9]+}/{answer}")
    public ModelAndView answerApplication(Authentication loggedUser,
                                       @PathVariable("jobOfferId") final long jobOfferId,
                                       @PathVariable("userId") final long userId,
                                       @PathVariable("answer") final long answer,
                                       @RequestParam(value = "eid", defaultValue = "0") long enterpriseId) {

        long loggedUserId = authUserDetailsService.getLoggerUserId(loggedUser);
        Enterprise enterprise = enterpriseService.findById(loggedUserId).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in cancelJobOffer()", loggedUser.getName());
            return new UserNotFoundException();
        });
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Job Offer not found");
            return new JobOfferNotFoundException();
        });

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });

        if(answer==0) {
            contactService.rejectJobOffer(user, jobOffer);
            emailService.sendRejectApplicationEmail(user, enterprise.getName(), enterprise.getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
        }
        else {
            contactService.acceptJobOffer(user, jobOffer);
            emailService.sendAcceptApplicationEmail(user, enterprise.getName(), enterprise.getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
        }

        return new ModelAndView("redirect:/interestedEnterprise/" + loggedUserId);
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/contactsEnterprise/{enterpriseId:[0-9]+}")
    public ModelAndView contactsEnterprise(Authentication loggedUser, @PathVariable("enterpriseId") final long enterpriseId,
                                           @RequestParam(value = "status", defaultValue = "") final String status,
                                           @ModelAttribute("contactOrderForm") final ContactOrderForm contactOrderForm,
                                           @RequestParam(value = "page", defaultValue = "1") final int page,
                                           HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("enterpriseContacts");

        List<Contact> contactList;
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);
        StringBuilder path = new StringBuilder().append("/contactsEnterprise/").append(enterpriseId);

        if(request.getParameter("status") == null) {
            contactList = contactService.getContactsForEnterprise(enterprise, FilledBy.ENTERPRISE, SortHelper.getSortBy(contactOrderForm.getSortBy()),
                    page - 1, CONTACTS_PER_PAGE);
            path.append("?").append("&");
        }
        else {
            contactList = contactService.getContactsForEnterprise(enterprise, FilledBy.ENTERPRISE, status, SortHelper.getSortBy(contactOrderForm.getSortBy()),
                    page - 1, CONTACTS_PER_PAGE);
            path.append("?status=").append(status).append("&");
        }

        path.append("sortBy=").append(contactOrderForm.getSortBy());

        long contactsCount = status.isEmpty()? contactService.getContactsCountForEnterprise(enterpriseId) : contactList.size();

        mav.addObject("loggedUserID", authUserDetailsService.getLoggerUserId(loggedUser));
        mav.addObject("contactList", contactList);
        mav.addObject("status", status);
        mav.addObject("sortById", contactOrderForm.getSortBy());
        mav.addObject("path", path);
        mav.addObject("pages", PaginationHelper.getMaxPages(contactsCount, CONTACTS_PER_PAGE));
        mav.addObject("currentPage", page);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/interestedEnterprise/{enterpriseId:[0-9]+}")
    public ModelAndView interestedEnterprise(Authentication loggedUser, @PathVariable("enterpriseId") final long enterpriseId,
                                           @RequestParam(value = "status",defaultValue = "") final String status,
                                           @ModelAttribute("contactOrderForm") final ContactOrderForm contactOrderForm,
                                           @RequestParam(value = "page", defaultValue = "1") final int page,
                                           HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("enterpriseInterested");
        List<Contact> contactList;
        StringBuilder path = new StringBuilder().append("/interestedEnterprise/").append(enterpriseId);

        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);

        if(request.getParameter("status") == null) {
            contactList = contactService.getContactsForEnterprise(enterprise, FilledBy.USER, SortHelper.getSortBy(contactOrderForm.getSortBy()),
                    page - 1, CONTACTS_PER_PAGE);
            path.append("?").append("&");
        }
        else {
            contactList = contactService.getContactsForEnterprise(enterprise, FilledBy.USER, status, SortHelper.getSortBy(contactOrderForm.getSortBy()),
                    page - 1, CONTACTS_PER_PAGE);
            path.append("?status=").append(status).append("&");
        }

        long contactsCount = status.isEmpty()? contactService.getContactsForEnterprise(enterprise, FilledBy.USER).size() : contactList.size();

        path.append("sortBy=").append(contactOrderForm.getSortBy());

        mav.addObject("loggedUserID", authUserDetailsService.getLoggerUserId(loggedUser));
        mav.addObject("contactList", contactList);
        mav.addObject("status", status);
        mav.addObject("sortById", contactOrderForm.getSortBy());
        mav.addObject("path", path);
        mav.addObject("pages", PaginationHelper.getMaxPages(contactsCount, CONTACTS_PER_PAGE));
        mav.addObject("currentPage", page);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping(value = "/createJobOffer/{enterpriseId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formJobOffer(Authentication loggedUser, @ModelAttribute("jobOfferForm") final JobOfferForm jobOfferForm, @PathVariable("enterpriseId") final long enterpriseId) {
        final ModelAndView mav = new ModelAndView("enterpriseJobOfferForm");
        mav.addObject("enterprise", enterpriseService.findById(enterpriseId).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in formJobOffer()", loggedUser.getName());
            return new UserNotFoundException();
        }));
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping(value = "/createJobOffer/{enterpriseId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createJobOffer(Authentication loggedUser, @Valid @ModelAttribute("jobOfferForm") final JobOfferForm jobOfferForm, final BindingResult errors, @PathVariable("enterpriseId") final long enterpriseId) {
        if (errors.hasErrors()) {
            LOGGER.warn("Job Offer form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return formJobOffer(loggedUser, jobOfferForm, enterpriseId);
        }
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in createJobOffer()", loggedUser.getName());
            return new UserNotFoundException();
        });

        Category category = categoryService.findByName(jobOfferForm.getCategory()).orElseThrow(() -> {
            LOGGER.error("Category {} not found in createJobOffer()", jobOfferForm.getCategory());
            return new CategoryNotFoundException();
        });

        JobOffer jobOffer = jobOfferService.create(enterprise, category, jobOfferForm.getJobPosition(), jobOfferForm.getJobDescription(), jobOfferForm.getSalary(), jobOfferForm.getMode());

        if(!jobOfferForm.getSkill1().isEmpty()) {
            Skill skill1 = skillService.findByDescriptionOrCreate(jobOfferForm.getSkill1());
            jobOfferSkillService.addSkillToJobOffer(skill1, jobOffer);
        }
        if(!jobOfferForm.getSkill2().isEmpty()) {
            Skill skill2 = skillService.findByDescriptionOrCreate(jobOfferForm.getSkill2());
            jobOfferSkillService.addSkillToJobOffer(skill2, jobOffer);
        }

        LOGGER.debug("A new job offer was registered under id: {}", jobOffer.getId());
        LOGGER.info("A new job offer was registered");

        return new ModelAndView("redirect:/profileEnterprise/" + enterprise.getId());

    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping(value = "/editEnterprise/{enterpriseId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formEditEnterprise(Authentication loggedUser, @ModelAttribute("editEnterpriseForm") final EditEnterpriseForm editEnterpriseForm,
                                     @PathVariable("enterpriseId") final long enterpriseId) {
        ModelAndView mav = new ModelAndView("enterpriseEditForm");
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in formEditEnterprise()", loggedUser.getName());
            return new UserNotFoundException();
        });
        mav.addObject("enterprise", enterprise);
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping(value = "/editEnterprise/{enterpriseId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView editEnterprise(Authentication loggedUser, @Valid @ModelAttribute("editEnterpriseForm") final EditEnterpriseForm editEnterpriseForm,
                                 final BindingResult errors, @PathVariable("enterpriseId") final long enterpriseId) {
        if (errors.hasErrors()) {
            return formEditEnterprise(loggedUser, editEnterpriseForm, enterpriseId);
        }

        Category category = categoryService.findByName(editEnterpriseForm.getCategory()).orElseThrow(CategoryNotFoundException::new);
        Integer newYear = editEnterpriseForm.getYear().isEmpty() ? null : Integer.parseInt(editEnterpriseForm.getYear());

        enterpriseService.updateEnterpriseInformation(enterpriseId, editEnterpriseForm.getName(), editEnterpriseForm.getAboutUs(),
                editEnterpriseForm.getLocation(), category, editEnterpriseForm.getLink(),
                newYear, editEnterpriseForm.getWorkers());

        return new ModelAndView("redirect:/profileEnterprise/" + enterpriseId);
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping(value = "/uploadEnterpriseProfileImage/{enterpriseId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formImage(Authentication loggedUser, @ModelAttribute("imageForm") final ImageForm imageForm,
                                  @PathVariable("enterpriseId") final long enterpriseId) {
        final ModelAndView mav = new ModelAndView("imageForm");
        mav.addObject("enterprise", enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping(value = "/uploadEnterpriseProfileImage/{enterpriseId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView uploadImage(Authentication loggedUser, @Valid @ModelAttribute("imageForm") final ImageForm imageForm, final BindingResult errors,
                                    @PathVariable("enterpriseId") final long enterpriseId) throws IOException {
        if (errors.hasErrors()) {
            return formImage(loggedUser, imageForm, enterpriseId);
        }
        Image newImage = imageService.uploadImage(imageForm.getImage().getBytes());
        enterpriseService.updateProfileImage(enterpriseId, newImage);
        return new ModelAndView("redirect:/profileEnterprise/" + enterpriseId);
    }

    @RequestMapping(value = "/{enterpriseId:[0-9]+}/enterpriseImage/{imageId}", method = RequestMethod.GET, produces = "image/*")
    public @ResponseBody byte[] getProfileImage(@PathVariable("enterpriseId") final long enterpriseId, @PathVariable("imageId") final int imageId) {
        LOGGER.debug("Trying to access profile image");
        byte[] profileImage = new byte[0];
        try {
            profileImage = enterpriseService.getProfileImage(imageId).orElseThrow(UserNotFoundException::new).getBytes();
        } catch (UserNotFoundException e) {
            LOGGER.error("Error loading image {} in getProfileImage()", imageId);
        }
        LOGGER.info("Profile image accessed.");
        return profileImage;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE')")
    @RequestMapping(value ="/contact/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView contactForm(Authentication loggedUser, @ModelAttribute("simpleContactForm") final ContactForm form, @PathVariable("userId") final long userId) {
        long loggedUserID = authUserDetailsService.getLoggerUserId(loggedUser);
        final ModelAndView mav = new ModelAndView("enterpriseSimpleContactForm");

        Enterprise enterprise = enterpriseService.findById(loggedUserID).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in contactForm()", loggedUserID);
            return new UserNotFoundException();
        });

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User {} not found in contactForm()", userId);
            return new UserNotFoundException();
        });

        mav.addObject("user", user);
        mav.addObject("jobOffers", jobOfferService.findActiveByEnterprise(enterprise));
        mav.addObject("loggedUserID", loggedUserID);
        return mav;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ENTERPRISE')")
    @RequestMapping(value = "/contact/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView contact(Authentication loggedUser, @Valid @ModelAttribute("simpleContactForm") final ContactForm form,
                                final BindingResult errors, @PathVariable("userId") final long userId) {
        long jobOfferId = form.getJobOfferId();
        boolean alreadyContacted = contactService.alreadyContacted(userId, jobOfferId);

        if (errors.hasErrors() || alreadyContacted) {
            if(alreadyContacted)
                errors.rejectValue("jobOfferId", "ExistingJobOffer", "You've already sent this job offer to this user.");

            LOGGER.warn("Contact form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return contactForm(loggedUser, form, userId);
        }

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Job Offer {} not found in contact()", jobOfferId);
            return new JobOfferNotFoundException();
        });
        Enterprise enterprise = enterpriseService.findByEmail(loggedUser.getName()).orElseThrow(() -> {
            LOGGER.error("Enterprise {} not found in contact()", loggedUser.getName());
            return new UserNotFoundException();
        });
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User {} not found in contact()", userId);
            return new UserNotFoundException();
        });

        emailService.sendContactEmail(user, enterprise, jobOffer, form.getMessage(), LocaleContextHolder.getLocale());
        contactService.addContact(enterprise, user, jobOffer, FilledBy.ENTERPRISE);

        return new ModelAndView("redirect:/");
    }
}