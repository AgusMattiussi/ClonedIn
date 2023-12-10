package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.enums.AuthType;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class AuthTypeWebAuthenticationDetails extends WebAuthenticationDetails {

    private final AuthType authType;

    public AuthTypeWebAuthenticationDetails(HttpServletRequest request, AuthType authType) {
        super(request);
        this.authType = authType;
    }

    public AuthType getAuthType() {
        return authType;
    }
}
