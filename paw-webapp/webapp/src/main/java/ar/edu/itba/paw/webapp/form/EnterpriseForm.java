package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.NotExistingEmail;
import ar.edu.itba.paw.webapp.validators.StringMatches;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@StringMatches(string1 = "password", string2 = "repeatPassword")
public class EnterpriseForm {
    @NotEmpty
    @Email
    @NotExistingEmail
    @Size(max=100)
    private String email;

    @Size(min = 6, max = 20)
    private String password;

    @Size(min = 6, max = 20)
    private String repeatPassword;

    @NotEmpty
    @Size(max=50)
    private String name;

    @Size(max=50)
    private String city;

    @NotEmpty
    private String workers;

    @Pattern(regexp = "(1([0-9]{3}))?")
    private String year;

    @Size(max=600)
    private String link;

    @Size(max=600)
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

    public String getWorkers() {
        return workers;
    }

    public void setWorkers(String workers) {
        this.workers = workers;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

}
