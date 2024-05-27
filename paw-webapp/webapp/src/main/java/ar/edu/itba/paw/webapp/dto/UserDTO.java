package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.*;

// TODO: Matchear los forms con estos nombres de variables
public class UserDTO {

    private long id;
    private String email;
    private String name;
    private String location;
    private String currentPosition;
    private String description;
    private String educationLevel;
    private int visibility;
    private UserDTOLinks links;
    private Integer yearsOfExperience;

    public static UserDTO fromUser(final UriInfo uriInfo, final User user) {
        final UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.email = user.getEmail();
        dto.name = user.getName();
        dto.location = user.getLocation();
        dto.currentPosition = user.getCurrentPosition();
        dto.description = user.getDescription();
        dto.educationLevel = user.getEducation();
        dto.visibility = user.getVisibility();
        dto.yearsOfExperience = user.getYearsOfExperience();

        dto.links = new UserDTOLinks(uriInfo, user);
        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDTOLinks getLinks() {
        return links;
    }

    public void setLinks(UserDTOLinks links) {
        this.links = links;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public static class UserDTOLinks {
        private URI self;
        private URI image;
        private URI category;
        private URI experiences;
        private URI educations;
        private URI skills;
        private URI applications;
        private URI notifications;

        public UserDTOLinks() {
        }

        public UserDTOLinks(final UriInfo uriInfo, final User user) {
            final UriBuilder userUriBuilder = uriInfo.getBaseUriBuilder()
                    .replacePath(USERS_URL)
                    .path(String.valueOf(user.getId()));

            this.self = userUriBuilder.build();
            this.image = userUriBuilder.clone().path(IMAGE_SUBDIRECTORY).build();
            this.experiences = userUriBuilder.clone().path(EXPERIENCES_SUBDIRECTORY).build();
            this.educations = userUriBuilder.clone().path(EDUCATIONS_SUBDIRECTORY).build();
            this.skills = userUriBuilder.clone().path(SKILLS_SUBDIRECTORY).build();
            this.applications = userUriBuilder.clone().path(APPLICATIONS_SUBDIRECTORY).build();
            this.notifications = userUriBuilder.clone().path(NOTIFICATIONS_SUBDIRECTORY).build();

            final UriBuilder categoryUriBuilder = uriInfo.getBaseUriBuilder()
                    .replacePath(CATEGORIES_URL)
                    .path(String.valueOf(user.getCategory().getId()));
            this.category = categoryUriBuilder.build();
        }

        public URI getSelf() {
            return self;
        }

        public void setSelf(URI self) {
            this.self = self;
        }

        public URI getImage() {
            return image;
        }

        public void setImage(URI image) {
            this.image = image;
        }

        public URI getCategory() {
            return category;
        }

        public void setCategory(URI category) {
            this.category = category;
        }

        public URI getExperiences() {
            return experiences;
        }

        public void setExperiences(URI experiences) {
            this.experiences = experiences;
        }

        public URI getEducations() {
            return educations;
        }

        public void setEducations(URI educations) {
            this.educations = educations;
        }

        public URI getSkills() {
            return skills;
        }

        public void setSkills(URI skills) {
            this.skills = skills;
        }

        public URI getApplications() {
            return applications;
        }

        public void setApplications(URI applications) {
            this.applications = applications;
        }

        public URI getNotifications() {
            return notifications;
        }

        public void setNotifications(URI notifications) {
            this.notifications = notifications;
        }
    }
}
