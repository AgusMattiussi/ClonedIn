package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.EXPERIENCES_SUBDIRECTORY;
import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.USERS_URL;

public class ExperienceDTO {

    private Long id;
    private Integer monthFrom;
    private Integer yearFrom;
    private Integer monthTo;
    private Integer yearTo;
    private String position;
    private String enterpriseName;
    private String description;
    private ExperienceDTOLinks links;
    
    public static ExperienceDTO fromExperience(final UriInfo uriInfo, final Experience experience) {
        final ExperienceDTO dto = new ExperienceDTO();
        dto.monthFrom = experience.getMonthFrom();
        dto.yearFrom = experience.getYearFrom();
        dto.monthTo = experience.getMonthTo();
        dto.yearTo = experience.getYearTo();
        dto.position = experience.getPosition();
        dto.enterpriseName = experience.getEnterpriseName();
        dto.description = experience.getDescription();
        dto.id = experience.getId();

        dto.links = new ExperienceDTOLinks(uriInfo, experience);

        return dto;
    }

    public Integer getMonthFrom() {
        return monthFrom;
    }

    public void setMonthFrom(Integer monthFrom) {
        this.monthFrom = monthFrom;
    }

    public Integer getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(Integer yearFrom) {
        this.yearFrom = yearFrom;
    }

    public Integer getMonthTo() {
        return monthTo;
    }

    public void setMonthTo(Integer monthTo) {
        this.monthTo = monthTo;
    }

    public Integer getYearTo() {
        return yearTo;
    }

    public void setYearTo(Integer yearTo) {
        this.yearTo = yearTo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
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

    public ExperienceDTOLinks getLinks() {
        return links;
    }

    public void setLinks(ExperienceDTOLinks links) {
        this.links = links;
    }

    public static class ExperienceDTOLinks {
        private URI self;
        private URI user;

        public ExperienceDTOLinks() {
        }

        public ExperienceDTOLinks(final UriInfo uriInfo, final Experience experience) {
            final UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder()
                .replacePath(USERS_URL)
                .path(experience.getUser().getId().toString());
            this.user = userUriBuilder.build();

            final UriBuilder educationUriBuilder = userUriBuilder
                    .path(EXPERIENCES_SUBDIRECTORY)
                    .path(String.valueOf(experience.getId()));
            this.self = educationUriBuilder.build();
        }

        public URI getSelf() {
            return self;
        }

        public void setSelf(URI self) {
            this.self = self;
        }

        public URI getUser() {
            return user;
        }

        public void setUser(URI user) {
            this.user = user;
        }
    }
}
