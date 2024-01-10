package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Skill;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.*;

public class SkillDTO {

    private long id;
    private String description;
    private SkillDTOLinks links;

    public static SkillDTO fromSkill(UriInfo uriInfo, Skill skill) {
        final SkillDTO dto = new SkillDTO();
        dto.id = skill.getId();
        dto.description = skill.getDescription();

        dto.links = new SkillDTOLinks(uriInfo, skill);

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SkillDTOLinks getLinks() {
        return links;
    }

    public void setLinks(SkillDTOLinks links) {
        this.links = links;
    }

    //TODO: Pensar links utiles, como jobOffers con la skill, users con la skill, etc
    public static class SkillDTOLinks {
        private URI self;
        private URI usersWithSkill;

        public SkillDTOLinks(){}

        public SkillDTOLinks(UriInfo uriInfo, Skill skill) {
            final UriBuilder categoryUriBuilder = uriInfo.getAbsolutePathBuilder()
                    .replacePath(SKILLS_URL)
                    .path(String.valueOf(skill.getId()));
            this.self = categoryUriBuilder.build();

            this.usersWithSkill = uriInfo.getAbsolutePathBuilder()
                    .replacePath(USERS_URL)
                    .queryParam(SKILL_DESCRIPTION_PARAM, skill.getDescription())
                    .build();
        }

        public URI getSelf() {
            return self;
        }

        public void setSelf(URI self) {
            this.self = self;
        }

        public URI getUsersWithSkill() {
            return usersWithSkill;
        }

        public void setUsersWithSkill(URI usersWithSkill) {
            this.usersWithSkill = usersWithSkill;
        }
    }

}
