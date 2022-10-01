package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.EducationForm;
import ar.edu.itba.paw.webapp.form.ExperienceForm;
import ar.edu.itba.paw.webapp.form.SkillForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private static final String ACCEPT = "acceptMsg";
    private static final String REJECT = "rejectMsg";

    //TODO: pasar esta lógica a la capa service
    private static final Map<String, Integer> monthToNumber = new HashMap<>();


    @Autowired
    public UserController(final UserService userService, final EnterpriseService enterpriseService, final ExperienceService experienceService,
                          final EducationService educationService, final UserSkillService userSkillService,
                          final EmailService emailService, final JobOfferService jobOfferService,
                          final JobOfferSkillService jobOfferSkillService, final ContactService contactService){
        this.userService = userService;
        this.enterpriseService = enterpriseService;
        this.experienceService = experienceService;
        this.educationService = educationService;
        this.userSkillService = userSkillService;
        this.emailService = emailService;
        this.jobOfferService = jobOfferService;
        this.jobOfferSkillService = jobOfferSkillService;
        this.contactService = contactService;

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

    @RequestMapping("/acceptJobOffer/{jobOfferId:[0-9]+}/{answer}")
    public ModelAndView acceptJobOffer(Authentication loggedUser, @PathVariable("jobOfferId") final long jobOfferId,
                                       @PathVariable("answer") final long answer) {

        User user = userService.findById(getLoggerUserId(loggedUser)).orElseThrow(UserNotFoundException::new);
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(JobOfferNotFoundException::new);
        Enterprise enterprise = enterpriseService.findById(jobOffer.getEnterpriseID()).orElseThrow(UserNotFoundException::new);

        if(answer==0) {
            contactService.rejectJobOffer(user.getId(), jobOfferId);
            emailService.sendReplyJobOfferEmail(enterprise.getEmail(), user.getName(), jobOffer.getPosition(), REJECT);
        }
        else {
            contactService.acceptJobOffer(user.getId(), jobOfferId);
            emailService.sendReplyJobOfferEmail(enterprise.getEmail(), user.getName(), jobOffer.getPosition(), ACCEPT);
        }

        return new ModelAndView("redirect:/notificationsUser/" + user.getId());
    }
    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping("/notificationsUser/{userId:[0-9]+}")
    public ModelAndView notificationsUser(Authentication loggedUser, @PathVariable("userId") final long userId,
                                          @RequestParam(value = "page", defaultValue = "1") final int page) {
        final ModelAndView mav = new ModelAndView("userNotifications");

        HashMap<Long, String> enterpriseMap = new HashMap<>();

        HashMap<Long, List<Skill>> skillsMap = new HashMap<>();

        for (JobOfferWithStatus jobOfferWithStatus : contactService.getJobOffersWithStatusForUser(userId)) {
            long enterpriseID = jobOfferWithStatus.getEnterpriseID();
            Enterprise enterprise = enterpriseService.findById(enterpriseID).orElseThrow(UserNotFoundException::new);
            enterpriseMap.put(enterpriseID, enterprise.getName());

            long jobOfferId = jobOfferWithStatus.getId();
            skillsMap.put(jobOfferId, jobOfferSkillService.getSkillsForJobOffer(jobOfferId));
        }

        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        mav.addObject("loggedUserID", getLoggerUserId(loggedUser));
        mav.addObject("jobOffers", contactService.getJobOffersWithStatusForUser(userId));
        mav.addObject("enterpriseMap", enterpriseMap);
        mav.addObject("skillsMap", skillsMap);

//        mav.addObject("pages", jobOffersCount / itemsPerPage + 1);
//        mav.addObject("currentPage", page);

        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createExperience/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formExperience(Authentication loggedUser, @ModelAttribute("experienceForm") final ExperienceForm experienceForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("experienceForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createExperience/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createExperience(Authentication loggedUser, @Valid @ModelAttribute("experienceForm") final ExperienceForm experienceForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        int yearFlag = experienceForm.getYearTo().compareTo(experienceForm.getYearFrom());
        int monthFlag = monthToNumber.get(experienceForm.getMonthTo()).compareTo(monthToNumber.get(experienceForm.getMonthFrom()));

        if (errors.hasErrors() || yearFlag < 0 || monthFlag < 0) {
            if(yearFlag < 0)
                errors.rejectValue("yearTo", "LowerYearTo", "Year to must be greater than year from");
            else if (yearFlag == 0 && monthFlag < 0)
                errors.rejectValue("monthTo", "LowerMonthTo", "Month to must be greater than month from if years are the same");
            return formExperience(loggedUser, experienceForm, userId);
        }

        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        experienceService.create(user.getId(), monthToNumber.get(experienceForm.getMonthFrom()), Integer.parseInt(experienceForm.getYearFrom()),
                monthToNumber.get(experienceForm.getMonthTo()), Integer.parseInt(experienceForm.getYearTo()),experienceForm.getCompany(),
                experienceForm.getJob(), experienceForm.getJobDesc());
        return new ModelAndView("redirect:/profileUser/" + user.getId());

    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createEducation/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formEducation(Authentication loggedUser, @ModelAttribute("educationForm") final EducationForm educationForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("educationForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createEducation/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createEducation(Authentication loggedUser, @Valid @ModelAttribute("educationForm") final EducationForm educationForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        int yearFlag = educationForm.getYearTo().compareTo(educationForm.getYearFrom());
        int monthFlag = monthToNumber.get(educationForm.getMonthTo()).compareTo(monthToNumber.get(educationForm.getMonthFrom()));

        if (errors.hasErrors() || yearFlag < 0 || monthFlag < 0) {
            if(yearFlag < 0)
                errors.rejectValue("yearTo", "LowerYearTo", "Year to must be greater than year from");
            else if (yearFlag == 0 && monthFlag < 0)
                errors.rejectValue("monthTo", "LowerMonthTo", "Month to must be greater than month from if years are the same");

            return formEducation(loggedUser, educationForm, userId);
        }

        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        educationService.add(user.getId(), monthToNumber.get(educationForm.getMonthFrom()), Integer.parseInt(educationForm.getYearFrom()),
                monthToNumber.get(educationForm.getMonthTo()), Integer.parseInt(educationForm.getYearTo()), educationForm.getDegree(), educationForm.getCollege(), educationForm.getComment());
        return new ModelAndView("redirect:/profileUser/" + user.getId());

    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createSkill/{userId:[0-9]+}", method = { RequestMethod.GET })
    public ModelAndView formSkill(Authentication loggedUser, @ModelAttribute("skillForm") final SkillForm skillForm, @PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("skillsForm");
        mav.addObject("user", userService.findById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND canAccessUserProfile(#loggedUser, #userId)")
    @RequestMapping(value = "/createSkill/{userId:[0-9]+}", method = { RequestMethod.POST })
    public ModelAndView createSkill(Authentication loggedUser, @Valid @ModelAttribute("skillForm") final SkillForm skillForm, final BindingResult errors, @PathVariable("userId") final long userId) {
        if (errors.hasErrors()) {
            return formSkill(loggedUser, skillForm, userId);
        }
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        userSkillService.addSkillToUser(skillForm.getSkill(), user.getId());
        return new ModelAndView("redirect:/profileUser/" + user.getId());
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
