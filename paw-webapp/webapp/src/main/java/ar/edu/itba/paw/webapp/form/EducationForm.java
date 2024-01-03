package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.Month;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

public class EducationForm {

    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2100;

    @NotEmpty
    @Size(max=50)
    private String college;

    @NotEmpty
    @Size(max=50)
    private String degree;

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

    @Size(max=200)
    private String comment;

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
