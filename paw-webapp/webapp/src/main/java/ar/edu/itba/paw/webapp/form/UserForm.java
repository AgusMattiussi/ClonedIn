package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.EducationLevel;
import ar.edu.itba.paw.webapp.validators.EducationLevelEnum;
import ar.edu.itba.paw.webapp.validators.NotExistingEmail;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

//TODO: Deberia coincidir con el Custom Mime Type del usuario
//@StringMatches(string1 = "password", string2 = "repeatPassword")
public class UserForm {
    @NotExistingEmail
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

    @Size(max=50)
    private String location;

    @Size(max=50)
    private String currentPosition;

    @Size(max=600)
    private String description;

    @NotEmpty
    private String category;

    @EducationLevelEnum
    private String educationLevel;

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

    public EducationLevel getLevelEnum() {
        if(educationLevel == null)
            return null;
        return EducationLevel.fromString(educationLevel);
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }
}
