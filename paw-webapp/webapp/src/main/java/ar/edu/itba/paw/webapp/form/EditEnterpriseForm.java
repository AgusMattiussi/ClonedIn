package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.EmployeeRanges;
import ar.edu.itba.paw.webapp.validators.NotAfterCurrentYear;
import ar.edu.itba.paw.webapp.validators.ValidEmployeeRange;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class EditEnterpriseForm {
    @Size(max=50)
    private String name;

    @Size(max=50)
    private String location;

    @ValidEmployeeRange
    private String workers;

    @Min(1000)
    @NotAfterCurrentYear
    private Integer yearFounded;

    @Size(max=200)
    private String website;

    @Size(max=600)
    private String description;

    private String category;

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

    public EmployeeRanges getWorkersEnum() {
        if(workers == null)
            return null;
        return EmployeeRanges.fromString(workers);
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

}
