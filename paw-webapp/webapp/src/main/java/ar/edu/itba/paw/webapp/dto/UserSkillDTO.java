package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserSkillDTO {
    private static final String USER_URL = "webapp_war/users";

    private URI user;
    private String description;
    private Long id;


    public static UserSkillDTO fromSkill(final UriInfo uriInfo, final User user, final Skill skill) {
        final UserSkillDTO dto = new UserSkillDTO();
        dto.description = skill.getDescription();
        dto.id = skill.getId();

        final UriBuilder userUriBuilder = uriInfo.getBaseUriBuilder()
                .replacePath(USER_URL)
                .path(String.valueOf(user.getId()));
        dto.user = userUriBuilder.build();

        return dto;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
