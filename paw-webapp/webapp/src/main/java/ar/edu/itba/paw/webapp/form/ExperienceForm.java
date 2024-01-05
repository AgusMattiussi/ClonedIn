package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.Month;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

public class ExperienceForm {

    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2100;

    @NotEmpty
    @Size(max=50)
    private String company;

    @NotEmpty
    @Size(max=50)
    private String job;

    @NotEmpty
    @Size(max=600)
    private String jobDesc;

    @NotNull
    @Min(MIN_YEAR)
    @Max(MAX_YEAR)
    private Integer yearFrom;

    @NotNull
    private String monthFrom;

    @Min(MIN_YEAR)
    @Max(MAX_YEAR)
    private Integer yearTo;

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

    public Integer getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(Integer yearFrom) {
        this.yearFrom = yearFrom;
    }

    public Integer getYearTo() {
        return yearTo;
    }

    public void setYearTo(Integer yearTo) {
        this.yearTo = yearTo;
    }
}
