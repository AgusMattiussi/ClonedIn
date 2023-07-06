package ar.edu.itba.paw.webapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthUserDetailsService authUserDetailsService;

    /*public AuthenticationService(final JwtHelper jwtHelper, final AuthenticationManager authenticationManager, AuthUserDetailsService authUserDetailsService) {
        this.jwtHelper = jwtHelper;
        this.authenticationManager = authenticationManager;
        this.authUserDetailsService = authUserDetailsService;
    }*/

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        System.out.println("\n\n\n\n\n REQUEST: " + request.getEmail() + ", " + request.getPassword() + "\n\n\n\n\n\n");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails user = authUserDetailsService.loadUserByUsername(request.getEmail());
        String jwt = jwtHelper.generateToken(user);

        return new AuthenticationResponse(jwt);
    }
}
