package ar.edu.itba.paw.webapp.security;

import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.webapp.security.interfaces.ClonedInUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EnterpriseUserDetails implements ClonedInUserDetails {

    private static final GrantedAuthority enterpriseGrantedAuthority = new SimpleGrantedAuthority(Role.ENTERPRISE.name());

    private final Enterprise enterprise;

    public EnterpriseUserDetails(Enterprise enterprise) {
        if (enterprise == null)
            throw new IllegalArgumentException("Enterprise cannot be null");
        this.enterprise = enterprise;
    }

    @Override
    public Role getRole() {
        return Role.ENTERPRISE;
    }

    @Override
    public Long getId() {
        return enterprise.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(enterpriseGrantedAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return enterprise.getPassword();
    }

    @Override
    public String getUsername() {
        return enterprise.getEmail();
    }

}
