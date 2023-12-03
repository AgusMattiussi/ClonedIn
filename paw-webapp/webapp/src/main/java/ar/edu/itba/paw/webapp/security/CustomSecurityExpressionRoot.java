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
import org.springframework.security.core.userdetails.UserDetails;


public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private UserService userService;
    private EnterpriseService enterpriseService;

    public CustomSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }


    private boolean canAccessProfile(long requesterID, long profileID) {
        if(requesterID != profileID)
            throw new UserIsNotProfileOwnerException();
        return true;
    }

    public boolean canAccessUserProfile(UserDetails userDetails, long profileID) {
        System.out.println("\n\n\n\n Chequeando acceso");
        System.out.println("Requester: " + userDetails.getUsername());
        System.out.println("ProfileID: " + profileID);
        System.out.println("\n\n\n\n");
        Long userID = userService.getIdForEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);
        return canAccessProfile(userID, profileID);
    }

    public boolean canAccessEnterpriseProfile(Authentication requester, long profileID) {
        Long enterpriseID = enterpriseService.getIdForEmail(requester.getName()).orElseThrow(UserNotFoundException::new);
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
