package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class AuthUserDetailsService implements UserDetailsService {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    @Autowired
    private UserService us;

    @Autowired
    public AuthUserDetailsService(final UserService us){
        this.us = us;
    }

    // Username = email
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = us.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user by the email " + username));

        if(!BCRYPT_PATTERN.matcher(user.getPassword()).matches()){
            us.changePassword(user.getEmail(), user.getPassword());
            return loadUserByUsername(username);
        }

        final Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        //TODO: Este rol para la empresa
        //authorities.add(new SimpleGrantedAuthority("ROLE_ENTERPRISE"));

        return new AuthUser(username, user.getPassword(), authorities);
    }
}