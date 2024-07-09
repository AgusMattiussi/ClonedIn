package ar.edu.itba.paw.webapp.security;

import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    // Always returns a ClonedInUserDetails object
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        // Puede ser usuario normal
        final Optional<User> optUser = us.findByEmail(email);
        if(optUser.isPresent()) {
            return new NormalUserDetails(optUser.get());
        }
        
        // Puede ser empresa. Si no, no existe el usuario
        Enterprise enterprise =  es.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user by the email '%s'", email)));

        return new EnterpriseUserDetails(enterprise);
    }
}