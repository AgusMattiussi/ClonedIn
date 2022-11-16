package ar.edu.itba.paw.webapp.security;

import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.models.exceptions.HiddenProfileException;
import ar.edu.itba.paw.models.exceptions.UserIsNotProfileOwnerException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private UserService userService;
    private EnterpriseService enterpriseService;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }


    private boolean canAccessProfile(long loggedUserID, long profileID) {
        if(loggedUserID != profileID)
            throw new UserIsNotProfileOwnerException();
        return true;
    }

    public boolean canAccessUserProfile(Authentication loggedUser, long profileID) {
        Long userID = userService.getIdForEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);
        return canAccessProfile(userID, profileID);
    }

    public boolean canAccessEnterpriseProfile(Authentication loggedEnterprise, long profileID) {
        Long enterpriseID = enterpriseService.getIdForEmail(loggedEnterprise.getName()).orElseThrow(UserNotFoundException::new);
        return canAccessProfile(enterpriseID, profileID);
    }

    public boolean isUserVisible(long userID){
        User user = userService.findById(userID).orElseThrow(UserNotFoundException::new);
        if(user.getVisibility() != Visibility.VISIBLE.getValue())
            throw new HiddenProfileException();
        return true;
    }

    public boolean isJobOfferOwner(long enterpriseID, long jobOfferId){
        Enterprise enterprise = enterpriseService.findById(enterpriseID).orElseThrow(UserNotFoundException::new);
        return enterprise.isJobOfferOwner(jobOfferId);
    }

    public boolean isJobOfferOwner(Authentication loggedEnterprise, long jobOfferId){
        Enterprise enterprise = enterpriseService.findByEmail(loggedEnterprise.getName()).orElseThrow(UserNotFoundException::new);
        return enterprise.isJobOfferOwner(jobOfferId);
    }

    public boolean isEducationOwner(long userID, long educationID){
        User user = userService.findById(userID).orElseThrow(UserNotFoundException::new);
        return user.hasEducation(educationID);
    }

    public boolean isExperienceOwner(long userID, long experienceID){
        User user = userService.findById(userID).orElseThrow(UserNotFoundException::new);
        return user.hasExperience(experienceID);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setEnterpriseService(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @Override
    public void setFilterObject(Object o) {

    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object o) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }
}
