package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;

import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.models.helpers.SortHelper;
import ar.edu.itba.paw.webapp.dto.ContactDTO;
import ar.edu.itba.paw.webapp.dto.EnterpriseDTO;
import ar.edu.itba.paw.webapp.dto.JobOfferDTO;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

//@Controller
/*public class EnterpriseController {

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
                             HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("enterpriseHome");
        final List<User> usersList;
        final long usersCount;
        StringBuilder path = new StringBuilder();

        Enterprise enterprise = enterpriseService.findByEmail(loggedUser.getName()).orElseThrow(() -> {
            LOGGER.error("/ : Enterprise {} not found in home()", loggedUser.getName());
            return new UserNotFoundException();
        });

        long categoryID;
        try {
            categoryID = Long.parseLong(enterpriseFilterForm.getCategory());
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid CategoryID {} in 'home'", enterpriseFilterForm.getCategory());
            categoryID = 0;
        }
        
        Category category = categoryService.findById(categoryID).orElse(null);
        String educationLevel = enterpriseFilterForm.getEducationLevel();
        String term = enterpriseFilterForm.getTerm();
        String minExperience = enterpriseFilterForm.getMinExperience();
        String maxExperience = enterpriseFilterForm.getMaxExperience();

        Integer minExpInt = null;
        Integer maxExpInt = null;

        if(!minExperience.isEmpty()){
            try {
                minExpInt = Integer.valueOf(minExperience);
            } catch (NumberFormatException e){
                LOGGER.error("Invalid Minimum Experience {} in 'home'", minExperience);
            }
        }

        if(!maxExperience.isEmpty()){
            try {
                maxExpInt = Integer.valueOf(maxExperience);
            } catch (NumberFormatException e){
                LOGGER.error("Invalid Maximum Experience {} in 'home'", maxExperience);
            }
        }

        usersList = userService.getUsersListByFilters(category, educationLevel, term, minExpInt, maxExpInt, page-1, HOME_JOB_OFFERS_PER_PAGE);
        usersCount = userService.getUsersCountByFilters(category, educationLevel, term, minExpInt, maxExpInt);

        path.append("?category=").append(enterpriseFilterForm.getCategory())
                .append("&educationLevel=").append(educationLevel)
                .append("&minExperience=").append(minExperience)
                .append("&maxExperience=").append(maxExperience)
                .append("&term=").append(term);

        mav.addObject("users", usersList);
        mav.addObject("contactedUsers", enterpriseService.getUserContactMap(enterprise.getContacts()));
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

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)  AND isJobOfferOwner(#enterpriseId, #jobOfferId)")
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


    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND isJobOfferOwner(#loggedUser, #jobOfferId)")
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

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId) AND isJobOfferOwner(#enterpriseId, #jobOfferId)")
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

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId) AND isJobOfferOwner(#enterpriseId, #jobOfferId)")
    @RequestMapping("/acceptApplication/{userId:[0-9]+}/{jobOfferId:[0-9]+}")
    public ModelAndView acceptApplication(Authentication loggedUser,
                                          @PathVariable("jobOfferId") final long jobOfferId,
                                          @PathVariable("userId") final long userId,
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

        boolean accepted = contactService.acceptJobOffer(user, jobOffer);
        if(accepted) {
            emailService.sendAcceptApplicationEmail(user, enterprise.getName(), user.getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
        }

        return new ModelAndView("redirect:/interestedEnterprise/" + loggedUserId);
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/rejectApplication/{userId:[0-9]+}/{jobOfferId:[0-9]+}")
    public ModelAndView rejectApplication(Authentication loggedUser,
                                          @PathVariable("jobOfferId") final long jobOfferId,
                                          @PathVariable("userId") final long userId,
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

        boolean rejected = contactService.rejectJobOffer(user, jobOffer);
        if(rejected) {
            emailService.sendRejectApplicationEmail(user, enterprise.getName(), user.getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
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
        if(!jobOfferForm.getSkill3().isEmpty()) {
            Skill skill3 = skillService.findByDescriptionOrCreate(jobOfferForm.getSkill3());
            jobOfferSkillService.addSkillToJobOffer(skill3, jobOffer);
        }
        if(!jobOfferForm.getSkill4().isEmpty()) {
            Skill skill4 = skillService.findByDescriptionOrCreate(jobOfferForm.getSkill4());
            jobOfferSkillService.addSkillToJobOffer(skill4, jobOffer);
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

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND isUserVisible(#userId)")
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
    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND isUserVisible(#userId)")
    @RequestMapping(value = "/contact/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView contact(Authentication loggedUser, @Valid @ModelAttribute("simpleContactForm") final ContactForm form,
                                final BindingResult errors, @PathVariable("userId") final long userId) {
        long jobOfferId = form.getJobOfferId();
        boolean alreadyContacted = contactService.alreadyContacted(userId, jobOfferId);

        if (errors.hasErrors() || alreadyContacted) {
            if(alreadyContacted) {
                errors.rejectValue("jobOfferId", "ExistingJobOffer", "You've already sent this job offer to this user.");
            }

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

    @PreAuthorize("hasRole('ROLE_USER') OR (canAccessEnterpriseProfile(#loggedUser, #enterpriseId) AND isJobOfferOwner(#enterpriseId, #jobOfferId))")
    @RequestMapping("/jobOffer/{jobOfferId:[0-9]+}")
    public ModelAndView jobOffer(Authentication loggedUser,
                                 @PathVariable("jobOfferId") final long jobOfferId,
                                 @RequestParam(value = "eid", defaultValue = "0") long enterpriseId) {
        final ModelAndView mav = new ModelAndView("jobOfferView");

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("/jobOffer : Job Offer {} not found in jobOffer()", jobOfferId);
            return new JobOfferNotFoundException();
        });

        mav.addObject("job", jobOffer);
        mav.addObject("loggedUserID", authUserDetailsService.getLoggerUserId(loggedUser));
        return mav;
    }
}*/

@Path("enterprises")
@Component
public class EnterpriseController {

    public static final int PAGE_SIZE = 10;
    private static final int CONTACTS_PER_PAGE = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseController.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JobOfferService jobOfferService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private JobOfferSkillService jobOfferSkillService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Context
    private UriInfo uriInfo;

    @Autowired
    public EnterpriseController(final EnterpriseService enterpriseService, final CategoryService categoryService,
                                final JobOfferService jobOfferService, final SkillService skillService,
                                final JobOfferSkillService jobOfferSkillService, final ContactService contactService,
                                final UserService userService, final EmailService emailService) {
        this.enterpriseService = enterpriseService;
        this.categoryService = categoryService;
        this.jobOfferService = jobOfferService;
        this.skillService = skillService;
        this.jobOfferSkillService = jobOfferSkillService;
        this.contactService = contactService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response createEnterprise(@Valid final EnterpriseForm enterpriseForm/*, final BindingResult errors, HttpServletRequest request*/) {

        //TODO: Desarrollar errores del formulario como "reenvio la pagina"
        /*if (errors.hasErrors()) {
            LOGGER.warn("Enterprise register form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return
        }*/

        Optional<Category> optCategory = categoryService.findByName(enterpriseForm.getCategory());
        if (!optCategory.isPresent()) {
            //TODO: Desarrollar errores
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Integer year = enterpriseForm.getYear().isEmpty() ? null : Integer.valueOf(enterpriseForm.getYear());

        final Enterprise enterprise = enterpriseService.create(enterpriseForm.getEmail(), enterpriseForm.getName(), enterpriseForm.getPassword(),
                enterpriseForm.getCity(), optCategory.get(), enterpriseForm.getWorkers(), year, enterpriseForm.getLink(), enterpriseForm.getAboutUs());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(enterprise.getId())).build();

        //TODO: revisar uso de mail y autologeado
        //emailService.sendRegisterEnterpriseConfirmationEmail(enterpriseForm.getEmail(), enterpriseForm.getName(), LocaleContextHolder.getLocale());
        //authWithAuthManager(request, enterpriseForm.getEmail(), enterpriseForm.getPassword());

        LOGGER.debug("A new enterprise was registered under id: {}", enterprise.getId());
        LOGGER.info("A new enterprise was registered");

        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id) {
        Optional<EnterpriseDTO> maybeEnterprise = enterpriseService.findById(id).map(e -> EnterpriseDTO.fromEnterprise(uriInfo, e));
        if (!maybeEnterprise.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(maybeEnterprise.get()).build();
    }

    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //TODO: Agregar orden y filtros
    @GET
    @Path("/{id}/jobOffers")
    public Response getJobOffers(@PathParam("id") final long id, @QueryParam("page") @DefaultValue("1") final int page) {
        Optional<Enterprise> optEnterprise = enterpriseService.findById(id);
        if (!optEnterprise.isPresent()) {
            LOGGER.error("Enterprise with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<JobOfferDTO> jobOffers = jobOfferService.findActiveByEnterprise(optEnterprise.get(), page - 1, PAGE_SIZE)
                .stream().map(jobOffer -> JobOfferDTO.fromJobOffer(uriInfo, jobOffer)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<JobOfferDTO>>(jobOffers) {
                })
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }

    @GET
    @Path("/{id}/jobOffers/{joid}")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getJobOfferById(@PathParam("id") final long id, @PathParam("joid") final long joid) {
        Optional<Enterprise> optEnterprise = enterpriseService.findById(id);
        if (!optEnterprise.isPresent()) {
            LOGGER.error("Enterprise with ID={} not found", id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<JobOfferDTO> optJobOffer = jobOfferService.findById(joid).map(jobOffer -> JobOfferDTO.fromJobOffer(uriInfo,jobOffer));
        if (!optJobOffer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(optJobOffer.get()).build();
    }

    @PUT
    @Path("/{id}/jobOffers/{joid}")
    @Produces({ MediaType.APPLICATION_JSON, })
    @Transactional
    public Response closeJobOffer(@PathParam("id") final long id, @PathParam("joid") final long joid) {
        Optional<Enterprise> optEnterprise = enterpriseService.findById(id);
        if (!optEnterprise.isPresent()) {
            LOGGER.error("Enterprise with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<JobOffer> optJobOffer = jobOfferService.findById(joid);
        if (!optJobOffer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        jobOfferService.closeJobOffer(optJobOffer.get());
        return Response.ok(optJobOffer.get()).build();
    }

    @POST
    @Path("/{id}/jobOffers")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Transactional
    public Response createJobOffer(@PathParam("id") final long id, @Valid final JobOfferForm jobOfferForm/*, final BindingResult errors*/) {
        Optional<Enterprise> optEnterprise = enterpriseService.findById(id);
        if (!optEnterprise.isPresent()) {
            LOGGER.error("Enterprise with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Category> optCategory = categoryService.findByName(jobOfferForm.getCategory());
        if (!optCategory.isPresent()) {
            LOGGER.error("Category '{}' not found in createJobOffer()", jobOfferForm.getCategory());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        JobOffer jobOffer = jobOfferService.create(optEnterprise.get(), optCategory.get(), jobOfferForm.getJobPosition(), jobOfferForm.getJobDescription(), jobOfferForm.getSalary(), jobOfferForm.getMode());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(jobOffer.getId())).build();

        if (!jobOfferForm.getSkill1().isEmpty()) {
            Skill skill1 = skillService.findByDescriptionOrCreate(jobOfferForm.getSkill1());
            jobOfferSkillService.addSkillToJobOffer(skill1, jobOffer);
        }
        if (!jobOfferForm.getSkill2().isEmpty()) {
            Skill skill2 = skillService.findByDescriptionOrCreate(jobOfferForm.getSkill2());
            jobOfferSkillService.addSkillToJobOffer(skill2, jobOffer);
        }
        if (!jobOfferForm.getSkill3().isEmpty()) {
            Skill skill3 = skillService.findByDescriptionOrCreate(jobOfferForm.getSkill3());
            jobOfferSkillService.addSkillToJobOffer(skill3, jobOffer);
        }
        if (!jobOfferForm.getSkill4().isEmpty()) {
            Skill skill4 = skillService.findByDescriptionOrCreate(jobOfferForm.getSkill4());
            jobOfferSkillService.addSkillToJobOffer(skill4, jobOffer);
        }

        LOGGER.debug("A new job offer was registered under id: {}", jobOffer.getId());
        LOGGER.info("A new job offer was registered");
        return Response.created(uri).build();
    }

    //TODO: Mejorar el SortBy, deberia ser mas descriptivo
    //TODO: Mejorar el FilledBy, deberia ser mas descriptivo
    // https://javaee.github.io/javaee-spec/javadocs/javax/ws/rs/QueryParam.html
    @GET
    @Path("/{id}/contacts")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getContacts(@PathParam("id") final long id,
                                    @QueryParam("page") @DefaultValue("1") final int page,
                                    @QueryParam("status") final String status,
                                    @QueryParam("filledBy") final int filledBy,
                                    @QueryParam("sortBy") final int sortBy) {
        Optional<Enterprise> optEnterprise = enterpriseService.findById(id);
        if (!optEnterprise.isPresent()) {
            LOGGER.error("Enterprise with ID={} not found", id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        //Fixme: Cambiar esto, ponerlo en un helper por ej
        FilledBy enumFilledBy;
        if(filledBy == 0)
            enumFilledBy = FilledBy.ENTERPRISE;
        else if(filledBy == 1)
            enumFilledBy = FilledBy.USER;
        else
            enumFilledBy = FilledBy.ANY;

        List<ContactDTO> contactList;
        //TODO: Cambiar el metodo en el back para no hacer esta bifurcacion solo por el status
        if(status == null) {
            contactList = contactService.getContactsForEnterprise(optEnterprise.get(), enumFilledBy, SortHelper.getSortBy(sortBy),
                    page - 1, CONTACTS_PER_PAGE).stream().map(c -> ContactDTO.fromContact(uriInfo, c)).collect(Collectors.toList());
        }
        else {
            contactList = contactService.getContactsForEnterprise(optEnterprise.get(), enumFilledBy, status, SortHelper.getSortBy(sortBy),
                    page - 1, CONTACTS_PER_PAGE).stream().map(c -> ContactDTO.fromContact(uriInfo, c)).collect(Collectors.toList());
        }


        return Response.ok(new GenericEntity<List<ContactDTO>>(contactList) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }

    @POST
    @Path("/{id}/contacts")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    //@Transactional
    public Response contactUser(@PathParam("id") final long id, @Valid final ContactForm contactForm, @QueryParam("uid") final long userId){

        Optional<Enterprise> optEnterprise = enterpriseService.findById(id);
        if (!optEnterprise.isPresent()) {
            LOGGER.error("Enterprise with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(userId <= 0) {
            LOGGER.error("Invalid userId: {}", userId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        long jobOfferId = contactForm.getJobOfferId();
        boolean alreadyContacted = contactService.alreadyContacted(userId, jobOfferId);
        if(alreadyContacted)
            return Response.status(Response.Status.CONFLICT).build();

        Optional<JobOffer> optJobOffer = jobOfferService.findById(jobOfferId);
        if(!optJobOffer.isPresent()){
            LOGGER.error("Job Offer {} not found in contactUser()", jobOfferId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<User> optUser = userService.findById(userId);
        if(!optUser.isPresent()){
            LOGGER.error("User with id={} not found in contactUser()", userId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        //TODO: emailService.sendContactEmail(optUser.get(), optEnterprise.get(), optJobOffer.get(), contactForm.getMessage(), LocaleContextHolder.getLocale());
        Contact contact = contactService.addContact(optEnterprise.get(), optUser.get(), optJobOffer.get(), FilledBy.ENTERPRISE);
        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(contact.getJobOffer().getId()))
                .path(String.valueOf(contact.getUser().getId()))
                .build();

        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}/contacts/{joid}")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getJobOfferContacts(@PathParam("id") final long id, @PathParam("joid") final long jobOfferId,
                                    @QueryParam("page") @DefaultValue("1") final int page,
                                    @QueryParam("filledBy") final int filledBy) {
        Optional<Enterprise> optEnterprise = enterpriseService.findById(id);
        if (!optEnterprise.isPresent()) {
            LOGGER.error("Enterprise with ID={} not found", id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<JobOffer> optJobOffer = jobOfferService.findById(jobOfferId);
        if (!optJobOffer.isPresent()) {
            LOGGER.error("JobOffer with ID={} not found", jobOfferId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        //Fixme: Cambiar esto, ponerlo en un helper por ej
        FilledBy enumFilledBy;
        if(filledBy == 0)
            enumFilledBy = FilledBy.ENTERPRISE;
        else if(filledBy == 1)
            enumFilledBy = FilledBy.USER;
        else
            enumFilledBy = FilledBy.ANY;

        List<ContactDTO> contactList = contactService.getContactsForEnterpriseAndJobOffer(optEnterprise.get(), optJobOffer.get(), enumFilledBy,
                    page - 1, CONTACTS_PER_PAGE).stream().map(c -> ContactDTO.fromContact(uriInfo, c)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ContactDTO>>(contactList) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }

    @GET
    @Path("/{id}/contacts/{joid}/{uid}")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getContactById(@PathParam("id") final long id, @PathParam("joid") final long jobOfferId,
                                    @PathParam("uid") final long userId) {
        Optional<Enterprise> optEnterprise = enterpriseService.findById(id);
        if (!optEnterprise.isPresent()) {
            LOGGER.error("Enterprise with ID={} not found", id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<ContactDTO> optJobOffer = contactService.findByPrimaryKey(userId, jobOfferId).map(c -> ContactDTO.fromContact(uriInfo,c));
        if (!optJobOffer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(optJobOffer.get()).build();
    }

    @GET
    @Path("/{id}/image")
    @Produces(value = {"image/webp"})
    public Response getProfileImage(@PathParam("id") final long id) {
        LOGGER.debug("Trying to access enterprise image");

        Optional<Enterprise> optEnterprise = enterpriseService.findById(id);
        if (!optEnterprise.isPresent()) {
            LOGGER.error("Enterprise with ID={} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Image image = optEnterprise.get().getImage();
        if(image == null) {
            LOGGER.error("Enterprise with ID={} has no image", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Image profileImage = imageService.getImage(image.getId()).orElseThrow(() -> {
            LOGGER.error("Error loading image {}", image.getId());
            return new ImageNotFoundException();
        });

        LOGGER.info("Enterprise image accessed.");
        return Response.ok(profileImage.getBytes()).build();
    }
}