package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Min;

public class ApplyToJobOfferForm {

    @Min(1)
    private long jobOfferId;

    public long getJobOfferId() {
        return jobOfferId;
    }

    public void setJobOfferId(long jobOfferId) {
        this.jobOfferId = jobOfferId;
    }
}
