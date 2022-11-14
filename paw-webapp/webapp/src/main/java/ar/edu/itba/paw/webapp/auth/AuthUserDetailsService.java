package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class AuthUserDetailsService implements UserDetailsService {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
    public static final String USER_ROLE = "ROLE_USER";
    public static final String ENTERPRISE_ROLE = "ROLE_ENTERPRISE";
    private static final GrantedAuthority USER_SIMPLE_GRANTED_AUTHORITY  = new SimpleGrantedAuthority(USER_ROLE);
    private static final GrantedAuthority ENTERPRISE_SIMPLE_GRANTED_AUTHORITY  = new SimpleGrantedAuthority(ENTERPRISE_ROLE);

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
            return defaultUserDetails(optUser.get());
        }
        
        // Puede ser empresa. Si no, no existe el usuario
        final Enterprise enterprise = es.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user by the email " + email));
        return enterpriseUserDetails(enterprise);
    }
    
    private UserDetails defaultUserDetails(User user){
        if(!BCRYPT_PATTERN.matcher(user.getPassword()).matches()){
            us.changePassword(user.getEmail(), user.getPassword());
            return loadUserByUsername(user.getEmail());
        }

        final Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(USER_SIMPLE_GRANTED_AUTHORITY);
        return new AuthUser(user.getEmail(), user.getPassword(), authorities);
    }
    
    private UserDetails enterpriseUserDetails(Enterprise enterprise){
        if(!BCRYPT_PATTERN.matcher(enterprise.getPassword()).matches()){
            es.changePassword(enterprise.getEmail(), enterprise.getPassword());
            return loadUserByUsername(enterprise.getEmail());
        }

        final Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(ENTERPRISE_SIMPLE_GRANTED_AUTHORITY);
        return new AuthUser(enterprise.getEmail(), enterprise.getPassword(), authorities);
    }

    public boolean isUser(Authentication loggedUser){
        return loggedUser.getAuthorities().contains(USER_SIMPLE_GRANTED_AUTHORITY);
    }

    public boolean isEnterprise(Authentication loggedUser){
        return loggedUser.getAuthorities().contains(ENTERPRISE_SIMPLE_GRANTED_AUTHORITY);
    }

    public long getLoggerUserId(Authentication loggedUser){
        if(isUser(loggedUser)) {
            User user = us.findByEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);
            return user.getId();
        } else {
            Enterprise enterprise = es.findByEmail(loggedUser.getName()).orElseThrow(UserNotFoundException::new);
            return enterprise.getId();
        }
    }
}