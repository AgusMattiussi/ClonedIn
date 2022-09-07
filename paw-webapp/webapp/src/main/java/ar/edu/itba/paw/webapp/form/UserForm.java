package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class UserForm {
    //@Size(min = 6, max = 100)
    //@Pattern(regexp = "[a-zA-Z0-9]+")
    private String username;
    //@Size(min = 6, max = 100)
    private String password;
    //@Size(min = 6, max = 100)
    private String repeatPassword;

   // @NotEmpty
    //@Email
    private String email;
    //@NotEmpty
    private String name;
    //FIX: no estoy segura como validar la foto o si va aca
    private String city;
    private String position;
    private String desc;
    private String rubro;
    private String college;
    private String degree;
    //@Pattern(regexp = "[0-9]+")
    private String years;
    private String company;
    private String job;
    private String jobdesc;
    //FIX: no estoy segura como poner el desde y el hasta
    private String lang;
    private String hability;
    private String more;
    private Date dated;
    private Date dateh;
    private Date datedesde;
    private Date datehasta;

//    @Size(min = 6, max = 100)
    private String name;

    private String description;

    public String getEmail() {
        return email;
    }
    public String getCompany() {
        return company;
    }
    public String getName() {
        return name;
    }
    public String getJob() {
        return job;
    }
    public String getRubro() {
        return rubro;
    }
    public String getYears() {
        return years;
    }
    public String getJobdesc() {
        return jobdesc;
    }
    public String getHability() {
        return hability;
    }
    public String getMore() {
        return more;
    }
    public String getLang() {
        return lang;
    }
    public Date getDated() {
        return dated;
    }
    public Date getDateh() {
        return dateh;
    }
    public Date getDatedesde() {
        return datedesde;
    }
    public Date getDatehasta() {
        return datehasta;
    }
    public String getPosition() {
        return position;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setYears(String years) {
        this.years = years;
    }
    public void setDated(Date dated) {
        this.dated = dated;
    }
    public void setDateh(Date dateh) {
        this.dateh = dateh;
    }
    public void setDatedesde(Date datedesde) {
        this.datedesde = datedesde;
    }
    public void setDatehasta(Date datehasta) {
        this.datehasta = datehasta;
    }
    public void setPosition(String position) {
        this.position = position;
    }


}
