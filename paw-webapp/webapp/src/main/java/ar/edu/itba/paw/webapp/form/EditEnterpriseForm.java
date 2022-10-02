package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.ExistingEmail;
import ar.edu.itba.paw.webapp.validators.StringMatches;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.io.File;

public class EditEnterpriseForm {
    @NotEmpty
    @Size(max=50)
    private String name;

    private File image;

    @Size(max=50)
    private String city;

    @Size(max=200)
    private String aboutUs;

    @NotEmpty
    private String category;

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
