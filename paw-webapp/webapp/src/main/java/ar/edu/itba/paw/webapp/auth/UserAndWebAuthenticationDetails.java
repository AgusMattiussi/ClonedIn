package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.security.interfaces.ClonedInUserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class UserAndWebAuthenticationDetails extends WebAuthenticationDetails {

    private final AuthType authType;
    private final ClonedInUserDetails userDetails;

    public UserAndWebAuthenticationDetails(HttpServletRequest request, AuthType authType, ClonedInUserDetails userDetails) {
        super(request);
        this.authType = authType;
        this.userDetails = userDetails;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public ClonedInUserDetails getUserDetails() {
        return userDetails;
    }

}
