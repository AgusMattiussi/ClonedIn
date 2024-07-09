package ar.edu.itba.paw.webapp.security.interfaces;

import ar.edu.itba.paw.models.enums.Role;
import org.springframework.security.core.userdetails.UserDetails;

public interface ClonedInUserDetails extends UserDetails {

    Role getRole();

    Long getId();

    default String getEmail() {
        return getUsername();
    }

    // We will not use these methods. We will default them to true.

    default boolean isAccountNonExpired() {
        return true;
    }

    default boolean isAccountNonLocked() {
        return true;
    }

    default boolean isCredentialsNonExpired() {
        return true;
    }

    default boolean isEnabled() {
        return true;
    }

}
