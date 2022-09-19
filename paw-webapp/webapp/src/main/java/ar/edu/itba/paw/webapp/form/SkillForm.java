package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

public class SkillForm {
//    @NotEmpty
    private String lang;
    @NotEmpty
    private String skill;
//    @NotEmpty
    private String more;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
}
