package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;

public class JOForm {

    @NotEmpty
    private String job;

    @NotEmpty
    private String jobdesc;

    @NotEmpty
    private BigDecimal salary;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobdesc() {
        return jobdesc;
    }

    public void setJobdesc(String jobdesc) {
        this.jobdesc = jobdesc;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }


}
