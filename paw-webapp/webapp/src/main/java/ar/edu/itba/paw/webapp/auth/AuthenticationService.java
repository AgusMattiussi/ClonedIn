package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.form.AuthenticationRequest;
import ar.edu.itba.paw.webapp.form.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private AuthenticationManager authenticationManager ;
    /*@Autowired
    private AuthUserDetailsService authUserDetailsService;*/

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthenticationException{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Si quisieramos agregar al JWT info adicional del usuario:
        //  UserDetails user = authUserDetailsService.loadUserByUsername(request.getEmail());
        String jwt = jwtHelper.generateAccessToken(request.getEmail());

        return new AuthenticationResponse(jwt);
    }
}
