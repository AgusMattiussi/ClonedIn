package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.ExistingEmail;
import ar.edu.itba.paw.webapp.validators.StringMatches;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.io.File;

@StringMatches(string1 = "password", string2 = "repeatPassword")
public class UserForm {
    @ExistingEmail
    @Email
    @NotEmpty
    @Size(max=100)
    private String email;

    @Size(min = 6, max = 20)
    private String password;

    @Size(min = 6, max = 20)
    private String repeatPassword;

    @NotEmpty
    @Size(max=100)
    private String name;

    private File image;

    @Size(max=50)
    @NotEmpty
    private String city;

    @Size(max=50)
    @NotEmpty
    private String position;

    @Size(max=200)
    private String aboutMe;

    @NotEmpty
    private String category;

    @NotEmpty
    private String level;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
