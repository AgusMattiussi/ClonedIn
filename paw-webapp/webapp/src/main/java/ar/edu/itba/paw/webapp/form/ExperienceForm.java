package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ExperienceForm {
    @NotEmpty
    @Size(max=50)
    private String company;

    @NotEmpty
    @Size(max=50)
    private String job;

    @NotEmpty
    @Size(max=200)
    private String jobDesc;

    @Pattern(regexp = "(19|20)([0-9]{2})-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])")
    @NotEmpty
    private String dateFrom;

    @Pattern(regexp = "(19|20)([0-9]{2})-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])")
    @NotEmpty
    private String dateTo;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
}
