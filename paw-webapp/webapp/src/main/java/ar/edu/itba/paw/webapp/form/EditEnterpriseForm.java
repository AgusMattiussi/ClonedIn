package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.EmployeeRanges;
import ar.edu.itba.paw.webapp.validators.NotAfterCurrentYear;
import ar.edu.itba.paw.webapp.validators.ValidEmployeeRange;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Calendar;

public class EditEnterpriseForm {
    @Size(max=50)
    private String name;

    @Size(max=50)
    private String city;

    @ValidEmployeeRange
    private String workers;

    @Min(1000)
    @NotAfterCurrentYear
    private Integer year;

    @Size(max=200)
    private String link;

    @Size(max=600)
    private String aboutUs;

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

    public String getWorkers() {
        return workers;
    }

    public EmployeeRanges getWorkersEnum() {
        if(workers == null)
            return null;
        return EmployeeRanges.fromString(workers);
    }

    public void setWorkers(String workers) {
        this.workers = workers;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
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
