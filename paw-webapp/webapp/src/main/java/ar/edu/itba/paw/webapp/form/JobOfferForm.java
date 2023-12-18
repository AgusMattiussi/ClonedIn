package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.JobOfferModality;
import ar.edu.itba.paw.webapp.validators.StringDiff;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@StringDiff(string1 = "skill1", string2 = "skill2")
public class JobOfferForm {

    @NotEmpty
    @Size(max=50)
    private String jobPosition;

    @Size(max=5000)
    private String jobDescription;

    @Min(1)
    @Max(1000000000)
    private BigDecimal salary;

    @NotEmpty
    private String category;

    @NotNull
    private JobOfferModality modality;

    @Size(max=50)
    private String skill1;

    @Size(max=50)
    private String skill2;

    @Size(max=50)
    private String skill3;

    @Size(max=50)
    private String skill4;

    public String getSkill1() {
        return skill1;
    }

    public void setSkill1(String skill1) {
        this.skill1 = skill1;
    }

    public String getSkill2() {
        return skill2;
    }

    public void setSkill2(String skill2) {
        this.skill2 = skill2;
    }

    public String getSkill3() {
        return skill3;
    }

    public void setSkill3(String skill3) {
        this.skill3 = skill3;
    }

    public String getSkill4() {
        return skill4;
    }

    public void setSkill4(String skill4) {
        this.skill4 = skill4;
    }


    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public JobOfferModality getModality() {
        return modality;
    }

    public void setModality(JobOfferModality modality) {
        this.modality = modality;
    }
}
