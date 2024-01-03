package ar.edu.itba.paw.webapp.security;

import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.models.exceptions.EnterpriseNotFoundException;
import ar.edu.itba.paw.models.exceptions.HiddenProfileException;
import ar.edu.itba.paw.models.exceptions.UserIsNotProfileOwnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.models.User;

@Component
public class SecurityValidator {

    @Autowired
    private UserService userService;
    @Autowired
    private EnterpriseService enterpriseService;

    private String getAuthEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null)
            return auth.getName();
        return null;
    }

    private boolean isProfileOwner(long requesterID, long profileID) {
        if(requesterID != profileID)
            throw new UserIsNotProfileOwnerException();
        return true;
    }

    public boolean isUserProfileOwner(long profileID) {
        String email = getAuthEmail();
        if(email == null)
            return false;
        Long userID = userService.getIdForEmail(email).orElseThrow(() -> new UserNotFoundException(profileID));
        return isProfileOwner(userID, profileID);
    }

    public boolean isEnterpriseProfileOwner(long profileID) {
        String email = getAuthEmail();
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

    public boolean isJobOfferOwner(long jobOfferId){
        String email = getAuthEmail();
        if(email == null)
            return false;
        Enterprise enterprise = enterpriseService.findByEmail(email).orElseThrow(() -> new EnterpriseNotFoundException(email));
        return enterprise.isJobOfferOwner(jobOfferId);
    }

    public boolean isExperienceOwner(long experienceID){
        String email = getAuthEmail();
        if(email == null)
            return false;
        User user = userService.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return user.hasExperience(experienceID);
    }

    public boolean isEducationOwner(long educationID){
        String email = getAuthEmail();
        if(email == null)
            return false;
        User user = userService.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return user.hasEducation(educationID);
    }
}
