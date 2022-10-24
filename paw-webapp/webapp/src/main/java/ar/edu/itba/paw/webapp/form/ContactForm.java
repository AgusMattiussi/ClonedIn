package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class ContactForm {
    @Size(max=100)
    private String message;

    private long jobOfferId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getJobOfferId() {
        return jobOfferId;
    }

    public void setJobOfferId(long jobOfferId) {
        this.jobOfferId = jobOfferId;
    }
}
