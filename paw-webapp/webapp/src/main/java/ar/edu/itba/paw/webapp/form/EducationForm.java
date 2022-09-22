package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EducationForm {
    @NotEmpty
    @Size(max=50)
    private String college;

    @NotEmpty
    @Size(max=50)
    private String degree;

    @Pattern(regexp = "(19|20)([0-9]{2})-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])")
    @NotEmpty
    private String dateFrom;

    @Pattern(regexp = "(19|20)([0-9]{2})-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])")
    @NotEmpty
    private String dateTo;

    @Size(max=100)
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
