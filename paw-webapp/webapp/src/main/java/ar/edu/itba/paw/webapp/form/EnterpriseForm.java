package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class EnterpriseForm {

    private String enterpriseName;
    private String enterpriseMessage;

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseMessage() {
        return enterpriseMessage;
    }

    public void setEnterpriseMessage(String enterpriseMessage) {
        this.enterpriseMessage = enterpriseMessage;
    }
}
