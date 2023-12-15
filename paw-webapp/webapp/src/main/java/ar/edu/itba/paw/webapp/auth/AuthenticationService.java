package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.CustomUserDetails;
import ar.edu.itba.paw.webapp.form.AuthenticationRequest;
import ar.edu.itba.paw.webapp.form.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private AuthenticationManager authenticationManager ;
    @Autowired
    private UserDetailsService userDetailsService;
    /*@Autowired
    private AuthUserDetailsService authUserDetailsService;*/

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthenticationException{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getEmail());
        String jwt = jwtHelper.generateAccessToken(user);

        return new AuthenticationResponse(jwt);
    }
}
