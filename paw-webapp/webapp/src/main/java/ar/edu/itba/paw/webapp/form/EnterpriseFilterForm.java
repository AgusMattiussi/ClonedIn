package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class EnterpriseFilterForm {
    private String category="";
    @Size(max = 50)
    private String location="";
    private String educationLevel="";
    @Size(max = 50)
    private String skill="";
    private int sortBy=0;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }
}
