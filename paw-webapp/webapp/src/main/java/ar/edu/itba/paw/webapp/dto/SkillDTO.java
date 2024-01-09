package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Skill;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class SkillDTO {

    private static final String SKILLS_URL = "webapp_war/skills";

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

        public SkillDTOLinks(){}

        public SkillDTOLinks(UriInfo uriInfo, Skill skill) {
            final UriBuilder categoryUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath(SKILLS_URL)
                    .path(String.valueOf(skill.getId()));
            this.self = categoryUriBuilder.build();
        }

        public URI getSelf() {
            return self;
        }

        public void setSelf(URI self) {
            this.self = self;
        }
    }

}
