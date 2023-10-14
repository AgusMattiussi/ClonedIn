package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDTO {

    //TODO: Completar con info relevante para usuarios
    private long id;
    private String username;
    private String name;
    private String location;
    private String currentPosition;
    private String description;
    private String educationLevel;
    private int visibility;
    private URI self;
    private URI image;
    private URI category;
    private URI experiences;
    private URI educations;
    private URI skills;

    //TODO: Rol?

    public static UserDTO fromUser(final UriInfo uriInfo, final User user) {
        final UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.username = user.getEmail();
        dto.name = user.getName();
        dto.location = user.getLocation();
        dto.currentPosition = user.getCurrentPosition();
        dto.description = user.getDescription();
        dto.educationLevel = user.getEducation();
        dto.visibility = user.getVisibility();

        //TODO: Revisar si hace falta eliminar el webapp_war para deployar
        final UriBuilder userUriBuilder = uriInfo.getBaseUriBuilder().replacePath("webapp_war/users").path(String.valueOf(user.getId()));
        dto.self = userUriBuilder.build();
        //final UriBuilder issuesUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("issues");
        //dto.assignedIssues = issuesUriBuilder.clone().queryParam("assignedTo", String.valueOf(user.getId())).build();
        //dto.reportedIssues = issuesUriBuilder.clone().queryParam("reportedTo", String.valueOf(user.getId())).build();
        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }



    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", self=" + self +
                '}';
    }
}
