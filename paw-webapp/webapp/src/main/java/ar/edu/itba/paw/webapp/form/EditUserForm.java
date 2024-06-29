package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.EducationLevel;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.webapp.validators.EducationLevelEnum;

import javax.validation.constraints.Size;

public class EditUserForm {
    @Size(max=100)
    private String name;

    @Size(max=50)
    private String location;

    @Size(max=50)
    private String currentPosition;

    @Size(max=600)
    private String description;

    private String category;

    @EducationLevelEnum
    private String educationLevel;

    private Boolean visible;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public EducationLevel getLevelEnum() {
        if(educationLevel == null)
            return null;
        return EducationLevel.fromString(educationLevel);
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Visibility getVisibilityAsEnum() {
        if(visible == null)
            return null;
        return Visibility.fromBoolean(visible);
    }
}
