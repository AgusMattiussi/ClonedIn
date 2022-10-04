package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

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

    @NotEmpty
    @Pattern(regexp = "(19|20)([0-9]{2})")
    private String yearFrom;

    @NotEmpty
    private String monthFrom;

    @Pattern(regexp = "((19|20)([0-9]{2}))?")
    private String yearTo;

    @NotEmpty
    private String monthTo;

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

    public String getMonthFrom() {
        return monthFrom;
    }

    public void setMonthFrom(String monthFrom) {
        this.monthFrom = monthFrom;
    }

    public String getMonthTo() {
        return monthTo;
    }

    public void setMonthTo(String monthTo) {
        this.monthTo = monthTo;
    }

    public String getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(String yearFrom) {
        this.yearFrom = yearFrom;
    }

    public String getYearTo() {
        return yearTo;
    }

    public void setYearTo(String yearTo) {
        this.yearTo = yearTo;
    }
}
