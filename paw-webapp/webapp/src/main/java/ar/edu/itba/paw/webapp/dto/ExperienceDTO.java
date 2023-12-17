package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ExperienceDTO {

    private static final String USERS_URL = "webapp_war/users";
    
    private URI user;
    private int monthFrom;
    private int yearFrom;
    private int monthTo;
    private int yearTo;
    private String position;
    private String enterpriseName;
    private String description;
    private Long id;
    
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

        final UriBuilder userUriBuilder = uriInfo.getBaseUriBuilder()
                .replacePath(USERS_URL)
                .path(String.valueOf(experience.getUser().getId()));
        dto.user = userUriBuilder.build();

        return dto;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
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

    public void setMonthTo(int monthTo) {
        this.monthTo = monthTo;
    }

    public int getYearTo() {
        return yearTo;
    }

    public void setYearTo(int yearTo) {
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
}
