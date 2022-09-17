package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {
    @Size(min = 6, max = 20)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String username;

    @Size(min = 6, max = 20)
    private String password;

    @Size(min = 6, max = 20)
    private String repeatPassword;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String name;

    private String city;
    private String position;
    @NotEmpty
    private String desc;
    private String rubro;
    private String college;
    private String degree;
    private String company;
    private String job;
    private String jobdesc;
    private String lang;
    private String hability;
    private String comment;
    private String more;
    @Pattern(regexp = "(19|20)([0-9]{2})-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])")
    private String dated;
    @Pattern(regexp = "(19|20)([0-9]{2})-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])")
    private String dateh;
    @Pattern(regexp = "(19|20)([0-9]{2})-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])")
    private String datedesde;
    @Pattern(regexp = "(19|20)([0-9]{2})-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])")
    private String datehasta;
    private String description;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return "muydificil";
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

    public String getEmail() {
        return email;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

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

    public String getJobdesc() {
        return jobdesc;
    }

    public void setJobdesc(String jobdesc) {
        this.jobdesc = jobdesc;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getHability() {
        return hability;
    }

    public void setHability(String hability) {
        this.hability = hability;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getDateh() {
        return dateh;
    }

    public void setDateh(String dateh) {
        this.dateh = dateh;
    }

    public String getDatedesde() {
        return datedesde;
    }

    public void setDatedesde(String datedesde) {
        this.datedesde = datedesde;
    }

    public String getDatehasta() {
        return datehasta;
    }

    public void setDatehasta(String datehasta) {
        this.datehasta = datehasta;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
