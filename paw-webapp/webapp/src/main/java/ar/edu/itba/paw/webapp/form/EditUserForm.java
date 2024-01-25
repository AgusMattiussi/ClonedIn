package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.EducationLevel;
import ar.edu.itba.paw.models.enums.Visibility;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class EditUserForm {
    @Size(max=100)
    private String name;

    @Size(max=50)
    private String location;

    @Size(max=50)
    private String position;

    @Size(max=600)
    private String aboutMe;

    private String category;

    private String level;

    //TODO: Visibility validator?
    private String visibility;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //TODO: Validator
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = EducationLevel.fromString(level).getStringValue();
    }

    public EducationLevel getLevelEnum() {
        if(level == null)
            return null;
        return EducationLevel.fromString(level);
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Visibility getVisibilityAsEnum() {
        if(visibility == null)
            return null;
        return Visibility.fromString(visibility);
    }
}
