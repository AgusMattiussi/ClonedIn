package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ContactForm {
    @Size(max=100)
    private String message;

    @Min(1)
    private long jobOfferId;
    @Min(1)
    private long userId;

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
