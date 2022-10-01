package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;

import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    public EnterpriseController(final UserService userService, final EnterpriseService enterpriseService, final CategoryService categoryService,
                                final SkillService skillService, final EmailService emailService, final JobOfferService jobOfferService,
                                final ContactService contactService){
        this.userService = userService;
        this.enterpriseService = enterpriseService;
        this.categoryService = categoryService;
        this.skillService = skillService;
        this.emailService = emailService;
        this.jobOfferService = jobOfferService;
        this.contactService = contactService;
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET })
    public ModelAndView home(Authentication loggedUser, @RequestParam(value = "page", defaultValue = "1") final int page,
                             @Valid @ModelAttribute("filterForm") final FilterForm filterForm,
                             @Valid @ModelAttribute("searchForm") final SearchForm searchForm,
                             HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView("index");

        final List<User> usersList;

//        final int usersCount = userService.getAllUsers().size();

        final int itemsPerPage = 8;

        //TODO: refactor?
        if(request.getParameter("term") == null)
                usersList = userService.getUsersListByFilters(page-1, itemsPerPage,
                            filterForm.getCategory(), filterForm.getLocation(), filterForm.getEducationLevel());
        else
            usersList = userService.getUsersListByName(page - 1, itemsPerPage, searchForm.getTerm());

        final int usersCount = usersList.size();

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
        int jobOffersCount = jobOfferService.getJobOffersCountForEnterprise(enterpriseId).orElseThrow(RuntimeException::new);
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);
        List<JobOffer> jobOfferList = jobOfferService.findByEnterpriseId(enterpriseId, page - 1, itemsPerPage);

        mav.addObject("enterprise", enterprise);
        mav.addObject("category", categoryService.findById(enterprise.getCategory().getId()));
        mav.addObject("joboffers", jobOfferList);
        mav.addObject("pages", jobOffersCount / itemsPerPage + 1);
        mav.addObject("currentPage", page);
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ENTERPRISE') AND canAccessEnterpriseProfile(#loggedUser, #enterpriseId)")
    @RequestMapping("/contactsEnterprise/{enterpriseId:[0-9]+}")
    public ModelAndView contactsEnterprise(Authentication loggedUser, @PathVariable("enterpriseId") final long enterpriseId,
                                           @RequestParam(value = "page", defaultValue = "1") final int page) {
        final ModelAndView mav = new ModelAndView("contacts");
        final int itemsPerPage = 8;
        int contactsCount = contactService.getContactsCountForEnterprise(enterpriseId).orElseThrow(RuntimeException::new);
        List<JobOfferStatusUserData> jobOffersList = contactService.getJobOffersWithStatusUserData(enterpriseId, page - 1, itemsPerPage);

        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        mav.addObject("jobOffers", jobOffersList);
        mav.addObject("pages", contactsCount / itemsPerPage + 1);
        mav.addObject("currentPage", page);
        return mav;
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
        long categoryID = categoryService.findByName(jobOfferForm.getCategory()).orElseThrow(UserNotFoundException::new).getId();
        jobOfferService.create(enterprise.getId(), categoryID, jobOfferForm.getJobPosition(), jobOfferForm.getJobDescription(), jobOfferForm.getSalary(), jobOfferForm.getMode());
//        JobOffer jobOffer = jobOfferService.create(enterprise.getId(), categoryID, jobOfferForm.getJobPosition(), jobOfferForm.getJobDescription(), jobOfferForm.getSalary(), jobOfferForm.getMode());
//        if(!jobOfferForm.getSkill1().isEmpty())
//            jobOfferSkillService.addSkillToJobOffer(jobOfferForm.getSkill1(), jobOffer.getId());
//        if(!jobOfferForm.getSkill2().isEmpty())
//            jobOfferSkillService.addSkillToJobOffer(jobOfferForm.getSkill1(), jobOffer.getId());
        return new ModelAndView("redirect:/profileEnterprise/" + enterprise.getId());

    }

    @RequestMapping(value = "/editEnterprise/{enterpriseId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formEditUser(@ModelAttribute("editEnterpriseForm") final EditEnterpriseForm editEnterpriseForm, @PathVariable("enterpriseId") final long enterpriseId) {
        ModelAndView mav = new ModelAndView("enterpriseEditForm");
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(UserNotFoundException::new);
        mav.addObject("enterprise", enterprise);
        mav.addObject("categories", categoryService.getAllCategories());
        return mav;
    }

    @RequestMapping(value ="/contact/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView contactForm(Authentication loggedUser, @ModelAttribute("simpleContactForm") final ContactForm form, @PathVariable("userId") final long userId) {
        long loggedUserID = getLoggerUserId(loggedUser);
        final ModelAndView mav = new ModelAndView("simpleContactForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        mav.addObject("jobOffers", jobOfferService.findByEnterpriseId(loggedUserID, 0, 100));
        mav.addObject("loggedUserID", loggedUserID);
        return mav;
    }

    @RequestMapping(value = "/contact/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView contact(Authentication loggedUser, @Valid @ModelAttribute("simpleContactForm") final ContactForm form,
                                final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors() || contactService.alreadyContacted(userId, form.getCategory())) {
            errors.rejectValue("category", "ExistingJobOffer", "You've already sent this job offer to this user.");
            return contactForm(loggedUser, form, userId);
        }
        long jobOfferId = form.getCategory();

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(JobOfferNotFoundException::new);
        Enterprise enterprise = enterpriseService.findByEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);

        emailService.sendContactEmail(user, enterprise, jobOffer, form.getMessage());
        // TODO: validar clave duplicada
        contactService.addContact(enterprise.getId(), user.getId(), jobOfferId);

        return new ModelAndView("redirect:/");
    }

    private boolean isUser(Authentication loggedUser){
        return loggedUser.getAuthorities().contains(AuthUserDetailsService.getUserSimpleGrantedAuthority());
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
}