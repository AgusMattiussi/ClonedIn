package ar.edu.itba.paw.webapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

// TODO: Por ahora esta clase no es necesaria
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authenticationToken = (String) authentication.getCredentials();
        String email = jwtHelper.extractUsername(authenticationToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if(jwtHelper.isTokenValid(authenticationToken, userDetails)) {
            authentication.setAuthenticated(true);
        }

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (authentication.isInstance(Authentication.class));
    }

}
