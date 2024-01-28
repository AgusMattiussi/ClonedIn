package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Education;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.EDUCATIONS_SUBDIRECTORY;
import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.USERS_URL;

public class EducationDTO {

    private Integer monthFrom;
    private Integer yearFrom;
    private Integer monthTo;
    private Integer yearTo;
    private String title;
    private String institutionName;
    private String description;
    private Long id;
    private EducationDTOLinks links;

    public static EducationDTO fromEducation(final UriInfo uriInfo, final Education education) {
        final EducationDTO dto = new EducationDTO();
        dto.monthFrom = education.getMonthFrom();
        dto.yearFrom = education.getYearFrom();
        dto.monthTo = education.getMonthTo();
        dto.yearTo = education.getYearTo();
        dto.title = education.getTitle();
        dto.institutionName = education.getInstitutionName();
        dto.description = education.getDescription();
        dto.id = education.getId();

        dto.links = new EducationDTOLinks(uriInfo, education);
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
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

    public EducationDTOLinks getLinks() {
        return links;
    }

    public void setLinks(EducationDTOLinks links) {
        this.links = links;
    }

    public static class EducationDTOLinks {
        private URI user;
        private URI self;

        public EducationDTOLinks(){}

        public EducationDTOLinks(final UriInfo uriInfo, final Education education) {

            final UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder()
                .replacePath(USERS_URL)
                .path(education.getUser().getId().toString());
            this.user = userUriBuilder.build();

            final UriBuilder educationUriBuilder = userUriBuilder
                    .path(EDUCATIONS_SUBDIRECTORY)
                    .path(String.valueOf(education.getId()));
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
