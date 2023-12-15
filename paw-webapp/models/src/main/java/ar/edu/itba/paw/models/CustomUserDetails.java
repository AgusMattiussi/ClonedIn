package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.enums.Role;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {

    public Role getRole();

    public Long getId();

}
