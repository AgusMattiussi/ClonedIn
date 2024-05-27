package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.ContactStatus;

import javax.validation.constraints.NotNull;

public class UpdateContactStatusForm {

    //TODO: Validator
    @NotNull
    private String status;

    public String getStatus() {
        ContactStatus.fromString(status); //TODO: Validator
        return status;
    }

    public void setStatus(String status) {
        ContactStatus.fromString(status); //TODO: Validator
        this.status = status;
    }

    public ContactStatus getStatusEnum() {
        return ContactStatus.fromString(status);
    }
}
