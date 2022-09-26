package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.ExistingEmail;
import ar.edu.itba.paw.webapp.validators.StringMatches;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


import javax.validation.constraints.Size;
import java.io.File;

@StringMatches(string1 = "password", string2 = "repeatPassword")
public class EnterpriseForm {
    @NotEmpty
    @Email
    @ExistingEmail
    @Size(max=100)
    private String email;

    @Size(min = 6, max = 20)
    private String password;

    @Size(min = 6, max = 20)
    private String repeatPassword;

    @NotEmpty
    @Size(max=50)
    private String name;

    private File image;

    @NotEmpty
    @Size(max=50)
    private String city;

    @Size(max=200)
    private String aboutUs;

    @NotEmpty
    private String category;

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

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
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
}
