package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;

import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
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

@Transactional
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
    @Autowired
    protected AuthenticationManager authenticationManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseController.class);

    @Autowired
    public EnterpriseController(final UserService userService, final EnterpriseService enterpriseService, final CategoryService categoryService,
                                final SkillService skillService, final EmailService emailService, final JobOfferService jobOfferService,
                                final ContactService contactService, final JobOfferSkillService jobOfferSkillService){
        this.userService = userService;
        this.enterpriseService = enterpriseService;
        this.categoryService = categoryService;
        this.skillService = skillService;
        this.emailService = emailService;
        this.jobOfferService = jobOfferService;
        this.contactService = contactService;
        this.jobOfferSkillService = jobOfferSkillService;
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET })
    public ModelAndView home(Authentication loggedUser, @RequestParam(value = "page", defaultValue = "1") final int page,
                             @Valid @ModelAttribute("filterForm") final FilterForm filterForm,
                             @Valid @ModelAttribute("searchForm") final SearchForm searchForm,
                             HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("index");
        final List<User> usersList;
        final int itemsPerPage = 8;
        final int usersCount;

        if(request.getParameter("term") == null) {
            usersList = userService.getUsersListByFilters(page - 1, itemsPerPage,
                    filterForm.getCategory(), filterForm.getLocation(), filterForm.getEducationLevel());
            usersCount = userService.getUsersCountByFilters(filterForm.getCategory(), filterForm.getLocation(),
                    filterForm.getEducationLevel());
        }
        else {
            //usersList = userService.getUsersListBySkill(page - 1, itemsPerPage, searchForm.getTerm());
            usersList = userService.getUsersListByName(page - 1, itemsPerPage, searchForm.getTerm());
            usersCount = usersList.size();
        }

        mav.addObject("users", usersList);
        mav.addObject("categories", categoryService.getAllCategories());
        mav.addObject("skills", skillService.getAllSkills());
        mav.addObject("pages", usersCount / itemsPerPage + 1);
        mav.addObject("currentPage", page);
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/profileEnterprise/{enterpriseId:[0-9]+}")
    public ModelAndView profileEnterprise(Authentication loggedUser, @PathVariable("enterpriseId") final long enterpriseId,
                                          @RequestParam(value = "page", defaultValue = "1") final int page) {
        final ModelAndView mav = new ModelAndView("profileEnterprise");
        final int itemsPerPage = 3;
//        int jobOffersCount = jobOfferService.getJobOffersCountForEnterprise(enterpriseId);
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> {
            LOGGER.error("/profile : Enterprise {} not found", loggedUser.getName());
            return new UserNotFoundException();
        });
        List<JobOffer> jobOfferList = jobOfferService.findByEnterpriseId(enterpriseId, page - 1, itemsPerPage);
        Map<Long, List<Skill>> jobOfferSkillMap = jobOfferService.getJobOfferSkillsMapForEnterprise(enterpriseId, page - 1, itemsPerPage);

        mav.addObject("enterprise", enterprise);
        mav.addObject("category", categoryService.findById(enterprise.getCategory().getId()));
        mav.addObject("jobOffers", jobOfferList);
        mav.addObject("jobOffersSkillMap", jobOfferSkillMap);
        mav.addObject("pages", jobOfferList.size() / itemsPerPage + 1);
        mav.addObject("currentPage", page);
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE')")
    @RequestMapping("/closeJobOffer/{jobOfferId:[0-9]+}")
    public ModelAndView closeJobOffer(Authentication loggedUser,
                                      @PathVariable("jobOfferId") final long jobOfferId) {

        Enterprise enterprise = enterpriseService.findById(getLoggerUserId(loggedUser)).orElseThrow(() -> {
            LOGGER.error("Enterprise not found");
            return new UserNotFoundException();
        });

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Enterprise not found");
            return new JobOfferNotFoundException();
        });

        jobOfferService.closeJobOffer(jobOffer);
        return new ModelAndView("redirect:/profileEnterprise/" + enterprise.getId());
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE')")
    @RequestMapping("/cancelJobOffer/{jobOfferId:[0-9]+}")
    public ModelAndView cancelJobOffer(Authentication loggedUser,
                                      @PathVariable("jobOfferId") final long jobOfferId) {

        Enterprise enterprise = enterpriseService.findById(getLoggerUserId(loggedUser)).orElseThrow(() -> {
            LOGGER.error("Enterprise not found");
            return new UserNotFoundException();
        });

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Enterprise not found");
            return new JobOfferNotFoundException();
        });

        jobOfferService.cancelJobOffer(jobOffer);
        return new ModelAndView("redirect:/profileEnterprise/" + enterprise.getId());
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE')")
    @RequestMapping("/cancelJobOffer/{userId:[0-9]+}/{jobOfferId:[0-9]+}")
    public ModelAndView cancelJobOffer(Authentication loggedUser,
                                      @PathVariable("userId") final long userId,
                                      @PathVariable("jobOfferId") final long jobOfferId) {

        Enterprise enterprise = enterpriseService.findById(getLoggerUserId(loggedUser)).orElseThrow(() -> {
            LOGGER.error("Enterprise not found");
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

        contactService.cancelJobOffer(user, jobOffer);
        emailService.sendCancelJobOfferEmail(user, enterprise.getName(), jobOffer.getPosition(), LocaleContextHolder.getLocale());

        return new ModelAndView("redirect:/contactsEnterprise/" + enterprise.getId());
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/contactsEnterprise/{enterpriseId:[0-9]+}")
    public ModelAndView contactsEnterprise(Authentication loggedUser, @PathVariable("enterpriseId") final long enterpriseId,
                                           @RequestParam(value = "status",defaultValue = "") final String status,
                                           @RequestParam(value = "page", defaultValue = "1") final int page,
                                           HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("contacts");
        final int itemsPerPage = 12;
        List<Contact> contactList = new ArrayList<>();

        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);

        if(request.getParameter("status") == null)
            contactList = contactService.getContactsForEnterprise(enterprise,page - 1, itemsPerPage);
        else
            contactList = contactService.getContactsForEnterprise(enterprise, status,page - 1, itemsPerPage);

        long contactsCount = status.isEmpty()? contactService.getContactsCountForEnterprise(enterpriseId) : contactList.size();


        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        mav.addObject("contactList", contactList);
        mav.addObject("pages", contactsCount / itemsPerPage + 1);
        mav.addObject("currentPage", page);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping(value = "/createJobOffer/{enterpriseId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formJobOffer(Authentication loggedUser, @ModelAttribute("jobOfferForm") final JobOfferForm jobOfferForm, @PathVariable("enterpriseId") final long enterpriseId) {
        final ModelAndView mav = new ModelAndView("jobOfferForm");
        mav.addObject("enterprise", enterpriseService.findById(enterpriseId).orElseThrow(() -> {
            LOGGER.error("Enterprise not found");
            return new UserNotFoundException();
        }));
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping(value = "/createJobOffer/{enterpriseId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createJobOffer(Authentication loggedUser, @Valid @ModelAttribute("jobOfferForm") final JobOfferForm jobOfferForm, final BindingResult errors, @PathVariable("enterpriseId") final long enterpriseId) {
        if (errors.hasErrors()) {
            LOGGER.warn("Job Offer form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return formJobOffer(loggedUser, jobOfferForm, enterpriseId);
        }
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> {
            LOGGER.error("Enterprise not found");
            return new UserNotFoundException();
        });

        Category category = categoryService.findByName(jobOfferForm.getCategory()).orElseThrow(() -> {
            LOGGER.error("Category not found");
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
            LOGGER.error("Enterprise not found");
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
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);
        enterpriseService.updateEnterpriseInformation(enterprise, editEnterpriseForm.getName(), editEnterpriseForm.getAboutUs(),
                editEnterpriseForm.getLocation(), editEnterpriseForm.getCategory());
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
        enterpriseService.updateProfileImage(enterpriseId, imageForm.getImage().getBytes());
        return new ModelAndView("redirect:/profileEnterprise/" + enterpriseId);
    }

    @RequestMapping(value = "/{enterpriseId:[0-9]+}/enterpriseImage/{imageId}", method = RequestMethod.GET, produces = "image/*")
    public @ResponseBody byte[] getProfileImage(@PathVariable("enterpriseId") final long enterpriseId, @PathVariable("imageId") final int imageId) {
        LOGGER.debug("Trying to access profile image");
        byte[] profileImage = new byte[0];
        try {
            profileImage = enterpriseService.getProfileImage(imageId).orElseThrow(UserNotFoundException::new).getBytes();
        } catch (UserNotFoundException e) {
            LOGGER.error("Error loading image {}", imageId);
        }
        LOGGER.info("Profile image accessed.");
        return profileImage;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE')")
    @RequestMapping(value ="/contact/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView contactForm(Authentication loggedUser, @ModelAttribute("simpleContactForm") final ContactForm form, @PathVariable("userId") final long userId) {
        long loggedUserID = getLoggerUserId(loggedUser);
        final ModelAndView mav = new ModelAndView("simpleContactForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        }));
        mav.addObject("jobOffers", jobOfferService.findActiveByEnterpriseId(loggedUserID, 0, 100));
        mav.addObject("loggedUserID", loggedUserID);
        return mav;
    }

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
            LOGGER.error("Job Offer not found");
            return new JobOfferNotFoundException();
        });
        Enterprise enterprise = enterpriseService.findByEmail(loggedUser.getName()).orElseThrow(() -> {
            LOGGER.error("Enterprise not found");
            return new UserNotFoundException();
        });
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User not found");
            return new UserNotFoundException();
        });

        emailService.sendContactEmail(user, enterprise, jobOffer, form.getMessage(), LocaleContextHolder.getLocale());
        contactService.addContact(enterprise, user, jobOffer);

        return new ModelAndView("redirect:/");
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