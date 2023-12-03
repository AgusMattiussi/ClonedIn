package ar.edu.itba.paw.webapp.security;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.exceptions.UserIsNotProfileOwnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SecurityValidator {

    @Autowired
    private UserService userService;

    private boolean canAccessProfile(long requesterID, long profileID) {
        if(requesterID != profileID)
            throw new UserIsNotProfileOwnerException();
        return true;
    }

    public boolean canAccessUserProfile(long profileID) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("\n\n\n\n Chequeando acceso");
        System.out.println("Requester: " + email);
        System.out.println("ProfileID: " + profileID);
        System.out.println("\n\n\n\n");
        Long userID = userService.getIdForEmail(email).orElseThrow(UserNotFoundException::new);
        return canAccessProfile(userID, profileID);
    }
}
