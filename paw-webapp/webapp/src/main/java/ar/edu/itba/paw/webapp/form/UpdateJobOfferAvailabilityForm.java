package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.JobOfferAvailability;

// TODO: Availability Validator
public class UpdateJobOfferAvailabilityForm {

    private String availability;

    public String getAvailability() {
        JobOfferAvailability.fromString(availability);
        return availability;
    }

    public void setAvailability(String availability) {
        JobOfferAvailability.fromString(availability);
        this.availability = availability;
    }

    public JobOfferAvailability getAvailabilityEnum() {
        return JobOfferAvailability.fromString(availability);
    }
}
