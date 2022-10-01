package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.File;

public class EditUserForm {
    @NotEmpty
    @Size(max=100)
    private String name;

    private File image;

    @Size(max=50)
    private String city;

    @Size(max=50)
    private String position;

    @Size(max=200)
    private String aboutMe;

    @NotEmpty
    private String category;

    @NotEmpty
    private String level;

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

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
