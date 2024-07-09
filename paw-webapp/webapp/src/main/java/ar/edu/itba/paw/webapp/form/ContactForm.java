package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class ContactForm {

    private Long jobOfferId;

    private Long userId;

    @Size(max=100)
    private String message;

    public Long getJobOfferId() {
        return jobOfferId;
    }

    public void setJobOfferId(Long jobOfferId) {
        this.jobOfferId = jobOfferId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
