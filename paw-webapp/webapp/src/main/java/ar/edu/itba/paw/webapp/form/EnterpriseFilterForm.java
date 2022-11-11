package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class EnterpriseFilterForm {
    private String category="";
    private String educationLevel="";
//    @Size(max = 50)
//    private String minExperience ="";
//    @Size(max = 50)
//    private String maxExperience ="";
    private int sortBy=0;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

//        return minExperience;
//    }
//
//    public void setMinExperience(String minExperience) {
//        this.minExperience = minExperience;
//    }
//
//    public String getMaxExperience() {
//        return maxExperience;
//    }
//
//    public void setMaxExperience(String maxExperience) {
//        this.maxExperience = maxExperience;
//    }
    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }
}
