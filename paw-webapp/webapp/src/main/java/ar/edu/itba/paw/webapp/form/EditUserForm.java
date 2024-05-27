package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.EducationLevel;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.webapp.validators.EducationLevelEnum;
import ar.edu.itba.paw.webapp.validators.VisibilityEnum;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class EditUserForm {
    @Size(max=100)
    private String name;

    @Size(max=50)
    private String city;

    @Size(max=50)
    private String position;

    @Size(max=600)
    private String aboutMe;

    private String category;

    @EducationLevelEnum
    private String level;

    @VisibilityEnum
    private String visibility; //TODO: Boolean?

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
