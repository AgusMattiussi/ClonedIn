package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.EmployeeRanges;
import ar.edu.itba.paw.webapp.validators.NotAfterCurrentYear;
import ar.edu.itba.paw.webapp.validators.NotExistingEmail;
import ar.edu.itba.paw.webapp.validators.StringMatches;
import ar.edu.itba.paw.webapp.validators.ValidEmployeeRange;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

@StringMatches(string1 = "password", string2 = "repeatPassword")
public class EnterpriseForm {

    @NotEmpty
    @Email
    @NotExistingEmail
    @Size(max=100)
    private String email;

    @NotEmpty
    @Size(min = 6, max = 20)
    private String password;

    //TODO: No tiene sentido este campo, resolverlo en el front
    @NotEmpty
    @Size(min = 6, max = 20)
    private String repeatPassword;

    @NotEmpty
    @Size(max=50)
    private String name;

    @Size(max=50)
    private String location;

    @NotNull
    @ValidEmployeeRange
    private String workers;

    @Min(1000)
    @NotAfterCurrentYear
    private Integer yearFounded;

    @Size(max=200)
    private String website;

    @Size(max=600)
    private String description;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkers() {
        return workers;
    }

    public void setWorkers(String workers) {
        this.workers = workers;
    }

    public Integer getYearFounded() {
        return yearFounded;
    }

    public void setYearFounded(Integer yearFounded) {
        this.yearFounded = yearFounded;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public EmployeeRanges getWorkersEnum() {
        return EmployeeRanges.fromString(workers);
    }

}
