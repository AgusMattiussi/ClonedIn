package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private static final GrantedAuthority USER_SIMPLE_GRANTED_AUTHORITY  = new SimpleGrantedAuthority(Role.USER.name());
    private static final GrantedAuthority ENTERPRISE_SIMPLE_GRANTED_AUTHORITY  = new SimpleGrantedAuthority(Role.ENTERPRISE.name());

    @Autowired
    private UserService us;
    @Autowired
    private EnterpriseService es;

    @Autowired
    public AuthUserDetailsService(final UserService us, final EnterpriseService es){
        this.us = us;
        this.es = es;
    }

    public AuthUserDetailsService(){};

    // Username = email
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        // Puede ser usuario normal
        final Optional<User> optUser = us.findByEmail(email);
        if(optUser.isPresent()) {
            return optUser.get();
        }
        
        // Puede ser empresa. Si no, no existe el usuario
        final Enterprise enterprise = es.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user by the email " + email));
        return enterprise;
    }
    

    public boolean isUser(Authentication loggedUser){
        return loggedUser.getAuthorities().contains(USER_SIMPLE_GRANTED_AUTHORITY);
    }

    public boolean isEnterprise(Authentication loggedUser){
        return loggedUser.getAuthorities().contains(ENTERPRISE_SIMPLE_GRANTED_AUTHORITY);
    }

    //TODO: idFromEmail()
    public long getLoggedUserId(Authentication loggedUser){
        if(isUser(loggedUser)) {
            User user = us.findByEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);
            return user.getId();
        } else {
            Enterprise enterprise = es.findByEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);
            return enterprise.getId();
        }
    }
}