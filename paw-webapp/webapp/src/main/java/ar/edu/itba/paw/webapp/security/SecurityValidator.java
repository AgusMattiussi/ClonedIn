package ar.edu.itba.paw.webapp.security;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.webapp.auth.UserAndWebAuthenticationDetails;
import ar.edu.itba.paw.webapp.security.interfaces.ClonedInUserDetails;
import ar.edu.itba.paw.webapp.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.models.User;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SecurityValidator {

    @Autowired
    private UserService userService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private JobOfferService jobOfferService;
    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private EducationService educationService;

    private boolean isProfileOwner(long requesterID, long profileID) {
        if(requesterID != profileID)
            throw new NotProfileOwnerException(requesterID);
        return true;
    }

    public boolean isUserProfileOwner(long profileID) {
        String email = SecurityUtils.getPrincipalEmail();
        if(email == null)
            return false;
        Long userID = userService.getIdForEmail(email).orElseThrow(() -> new UserNotFoundException(profileID));
        return isProfileOwner(userID, profileID);
    }

    public boolean isEnterpriseProfileOwner(long profileID) {
        String email = SecurityUtils.getPrincipalEmail();
        if(email == null)
            return false;
        Long enterpriseID = enterpriseService.getIdForEmail(email).orElseThrow(() -> new UserNotFoundException(profileID));
        return isProfileOwner(enterpriseID, profileID);
    }

    public boolean isUserVisible(long userID){
        User user = userService.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));
        if(user.getVisibility() != Visibility.VISIBLE.getValue())
            throw new HiddenProfileException(user.getName());
        return true;
    }

    // Must be called after validating the requester is an enterprise
    public boolean isJobOfferOwner(long jobOfferId){
        Long enterpriseId = SecurityUtils.getPrincipalId();
        if(enterpriseId == null)
            return false;
        return jobOfferService.isJobOfferOwner(jobOfferId, enterpriseId);
    }

    // Must be called after validating the requester is a user
    public boolean isExperienceOwner(long experienceID){
        Long userId = SecurityUtils.getPrincipalId();
        if(userId == null)
            return false;
        return experienceService.isExperienceOwner(experienceID, userId);
    }

    // Must be called after validating the requester is a user
    public boolean isEducationOwner(long educationID){
        Long userId = SecurityUtils.getPrincipalId();
        if(userId == null)
            return false;
        return educationService.isEducationOwner(educationID, userId);
    }

    @Transactional
    public boolean isGetContactsValid(Long userId, Long enterpriseId, Long jobOfferId) {
        Role requesterRole = SecurityUtils.getPrincipalRole();
        if(requesterRole == Role.USER)
            // Un usuario no deberia poder usar el parametro "userID" a menos que coincida con su ID.
            return userId == null || userId.equals(SecurityUtils.getPrincipalId());
        else if(requesterRole == Role.ENTERPRISE) {
            // Una empresa no deberia poder usar el parametro "enterpriseID" a menos que coincida
            // con su ID ni un jobOfferId que no le pertenezca
            if(enterpriseId != null && jobOfferId != null)
                return enterpriseId.equals(SecurityUtils.getPrincipalId()) && isJobOfferOwner(jobOfferId);
            if(enterpriseId != null)
                return enterpriseId.equals(SecurityUtils.getPrincipalId());
            if(jobOfferId != null)
                return isJobOfferOwner(jobOfferId);
            return true;
        }
        return false;
    }

    public boolean isGetSkillsValid(Long userId, Long jobOfferId) {
        Role requesterRole = SecurityUtils.getPrincipalRole();
        if(requesterRole == Role.USER)
            // Un usuario no deberia poder usar el parametro "userId" a menos que coincida con su ID.
            return userId == null || userId.equals(SecurityUtils.getPrincipalId());
        else if(requesterRole == Role.ENTERPRISE) {
            // Una empresa no deberia poder usar un jobOfferId que no le pertenezca
            if(jobOfferId != null)
                return isJobOfferOwner(jobOfferId);
            return true;
        }
        return false;
    }

    public boolean canAccessContact(String contactId){
        Role requesterRole = SecurityUtils.getPrincipalRole();

        if(requesterRole == Role.USER) {
            long userId = Contact.splitId(contactId)[0];
            Long requesterId = SecurityUtils.getPrincipalId();
            return requesterId.equals(userId);
        }
        else if(requesterRole == Role.ENTERPRISE) {
            long jobOfferId = Contact.splitId(contactId)[1];
            return isJobOfferOwner(jobOfferId);
        }

        return false;
    }

    public boolean idMatchesPrincipal(long id) {
        return id == SecurityUtils.getPrincipalId();
    }
}
