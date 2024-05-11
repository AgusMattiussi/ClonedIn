package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.webapp.auth.UserAndWebAuthenticationDetails;
import ar.edu.itba.paw.webapp.security.interfaces.ClonedInUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Role getPrincipalRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null)
            throw new IllegalStateException("No authentication found");

        ClonedInUserDetails userDetails = ((UserAndWebAuthenticationDetails) auth.getDetails()).getUserDetails();
        return userDetails.getRole();
    }

    public static String getPrincipalEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null)
            throw new IllegalStateException("No authentication found");
        return auth.getName();
    }

    public static Long getPrincipalId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null)
            throw new IllegalStateException("No authentication found");

        ClonedInUserDetails userDetails = ((UserAndWebAuthenticationDetails) auth.getDetails()).getUserDetails();
        return userDetails.getId();
    }
}
