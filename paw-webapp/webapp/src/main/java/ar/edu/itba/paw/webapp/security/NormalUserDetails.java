package ar.edu.itba.paw.webapp.security;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.webapp.security.interfaces.ClonedInUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NormalUserDetails implements ClonedInUserDetails {

    private static final GrantedAuthority userGrantedAuthority = new SimpleGrantedAuthority(Role.USER.name());

    private final User user;

    public NormalUserDetails(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        this.user = user;
    }


    @Override
    public Role getRole() {
        return Role.USER;
    }

    @Override
    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(userGrantedAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

}
