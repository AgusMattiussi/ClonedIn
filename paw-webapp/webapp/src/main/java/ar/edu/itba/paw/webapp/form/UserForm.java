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

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    // TODO: Cambiar esto
    public String getPassword() {
        return "contraseniaDificil";
        //return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRepeatPassword() {
        return repeatPassword;
    }
    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getCity() {
        return city;
    }
    public String getCollege() {
        return college;
    }
    public String getDegree() {
        return degree;
    }
    public String getDesc() {
        return desc;
    }
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

    public void setCity(String city) {
        this.city = city;
    }
    public void setCollege(String college) {
        this.college = college;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public void setDegree(String degree) {
        this.degree = degree;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public void setHability(String hability) {
        this.hability = hability;
    }
    public void setJobdesc(String jobdesc) {
        this.jobdesc = jobdesc;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMore(String more) {
        this.more = more;
    }
    public void setRubro(String rubro) {
        this.rubro = rubro;
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
