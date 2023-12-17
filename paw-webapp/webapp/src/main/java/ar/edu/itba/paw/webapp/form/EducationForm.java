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

    @NotEmpty
    private Month monthFrom;

    @Min(MIN_YEAR)
    @Max(MAX_YEAR)
    private Integer yearTo;

    private Month monthTo;

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

    public Month getMonthFrom() {
        return monthFrom;
    }

    public void setMonthFrom(Month monthFrom) {
        this.monthFrom = monthFrom;
    }

    public Integer getYearTo() {
        return yearTo;
    }

    public void setYearTo(Integer yearTo) {
        this.yearTo = yearTo;
    }

    public Month getMonthTo() {
        return monthTo;
    }

    public void setMonthTo(Month monthTo) {
        this.monthTo = monthTo;
    }
}
