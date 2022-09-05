package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CompanyForm {
    @Size(min = 6, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String cusername;
    @Size(min = 6, max = 100)
    private String cpassword;
    @Size(min = 6, max = 100)
    private String crepeatPassword;

    @NotEmpty
    @Email
    private String cemail;
    @NotEmpty private String cname;
    //FIX: no estoy segura como validar la foto o si va aca
    private String ccity;
    private String cdesc;
    private String crubro;
    private String cjob;
    private String cjobdesc;
    //FIX: no estoy segura como poner el desde y el hasta
    private String salary;
    private String crubro2;

    public String getCusername() {
        return cusername;
    }
    public void setCusername(String username) {
        this.cusername = username;
    }
    public String getCpassword() {
        return cpassword;
    }
    public void setCpassword(String password) {
        this.cpassword = password;
    }
    public String getCrepeatPassword() {
        return crepeatPassword;
    }
    public void setCrepeatPassword(String repeatPassword) {
        this.crepeatPassword = repeatPassword;
    }

    public String getCcity() {
        return ccity;
    }
    public String getCdesc() {
        return cdesc;
    }
    public String getCemail() {
        return cemail;
    }
    public String getCname() {
        return cname;
    }
    public String getCjob() {
        return cjob;
    }
    public String getCrubro() {
        return crubro;
    }
    public String getCjobdesc() {
        return cjobdesc;
    }
    public String getCrubro2() {
        return crubro2;
    }
    public String getSalary() {
        return salary;
    }

    public void setCcity(String city) {
        this.ccity = ccity;
    }
    public void setCdesc(String desc) {
        this.cdesc = cdesc;
    }
    public void setCemail(String email) {
        this.cemail = cemail;
    }
    public void setCjob(String job) {
        this.cjob = job;
    }
    public void setCjobdesc(String jobdesc) {
        this.cjobdesc = jobdesc;
    }
    public void setCname(String name) {
        this.cname = name;
    }
    public void setCrubro(String rubro) {
        this.crubro = rubro;
    }
    public void setSalary(String salary) {
        this.salary = salary;
    }
    public void setCrubro2(String crubro2) {
        this.crubro2 = crubro2;
    }
}
