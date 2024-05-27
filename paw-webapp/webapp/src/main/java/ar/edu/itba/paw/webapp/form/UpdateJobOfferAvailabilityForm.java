package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.JobOfferAvailability;
import ar.edu.itba.paw.webapp.validators.JobOfferAvailabilityEnum;

import javax.validation.constraints.NotNull;

public class UpdateJobOfferAvailabilityForm {

    @NotNull
    @JobOfferAvailabilityEnum
    private String availability;

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public JobOfferAvailability getAvailabilityEnum() {
        return JobOfferAvailability.fromString(availability);
    }
}
