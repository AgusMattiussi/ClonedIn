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

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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

    public String generateAccessToken(String basicHeader){
        String username = getUsernameFromBasicHeader(basicHeader);
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        return jwtHelper.generateAccessToken(user);
    }

    private String getUsernameFromBasicHeader(String basicHeader){
        String basic = basicHeader.substring(6); // "Basic ".length()

        String credentials = new String(Base64.getDecoder().decode(basic), StandardCharsets.UTF_8);
        String[] splitCredentials = credentials.split(":", 2);

        return splitCredentials[0];
    }
}
