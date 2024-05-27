package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.ContactStatus;
import ar.edu.itba.paw.webapp.validators.ContactStatusEnum;

import javax.validation.constraints.NotNull;

public class UpdateContactStatusForm {

    @NotNull
    @ContactStatusEnum
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ContactStatus getStatusEnum() {
        return ContactStatus.fromString(status);
    }
}
