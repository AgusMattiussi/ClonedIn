package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserSkillDTO {
    private User user;
    private String description;
    private URI self;

    public static UserSkillDTO fromSkill(final UriInfo uriInfo, final User user, final Skill skill) {
        final UserSkillDTO dto = new UserSkillDTO();
        dto.user = user;
        dto.description = skill.getDescription();

        //FIXME: Corregir para que tenga sentido
        final UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("webapp_war/skills").path(String.valueOf(skill.getId()));
        dto.self = userUriBuilder.build();

        return dto;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
