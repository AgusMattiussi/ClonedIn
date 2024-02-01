package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService us;
    @Autowired
    private EnterpriseService es;

    @Autowired
    public AuthUserDetailsService(final UserService us, final EnterpriseService es){
        this.us = us;
        this.es = es;
    }

    // Username = email
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        // Puede ser usuario normal
        final Optional<User> optUser = us.findByEmail(email);
        if(optUser.isPresent()) {
            return optUser.get();
        }
        
        // Puede ser empresa. Si no, no existe el usuario
        return es.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user by the email '%s'", email)));
    }
}