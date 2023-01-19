package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ExperienceDTO {
    
    private User user;
    private int monthFrom;
    private int yearFrom;
    private Integer monthTo;
    private Integer yearTo;
    private String position;
    private String enterpriseName;
    private String description;
    private URI self;
    
    public static ExperienceDTO fromExperience(final UriInfo uriInfo, final Experience experience) {
        final ExperienceDTO dto = new ExperienceDTO();
        dto.user = experience.getUser();
        dto.monthFrom = experience.getMonthFrom();
        dto.yearFrom = experience.getYearFrom();
        dto.monthTo = experience.getMonthTo();
        dto.yearTo = experience.getYearTo();
        dto.position = experience.getPosition();
        dto.enterpriseName = experience.getEnterpriseName();
        dto.description = experience.getDescription();

        //FIXME: Corregir para que tenga sentido
        final UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("experiences").path(String.valueOf(experience.getId()));
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

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
