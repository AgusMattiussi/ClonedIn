package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class EducationDTO {
    private User user;
    private int monthFrom;
    private int yearFrom;
    private int monthTo;
    private int yearTo;
    private String title;
    private String institutionName;
    private String description;
    private URI self;

    public static EducationDTO fromEducation(final UriInfo uriInfo, final Education education) {
        final EducationDTO dto = new EducationDTO();
        dto.user = education.getUser();
        dto.monthFrom = education.getMonthFrom();
        dto.yearFrom = education.getYearFrom();
        dto.monthTo = education.getMonthTo();
        dto.yearTo = education.getYearTo();
        dto.title = education.getTitle();
        dto.institutionName = education.getInstitutionName();
        dto.description = education.getDescription();

        //FIXME: Corregir para que tenga sentido
        final UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("webapp_war/educations").path(String.valueOf(education.getId()));
        dto.self = userUriBuilder.build();

        return dto;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getMonthFrom() {
        return monthFrom;
    }

    public void setMonthFrom(int monthFrom) {
        this.monthFrom = monthFrom;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(int yearFrom) {
        this.yearFrom = yearFrom;
    }

    public int getMonthTo() {
        return monthTo;
    }

    public void setMonthTo(Integer monthTo) {
        this.monthTo = monthTo;
    }

    public int getYearTo() {
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

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
