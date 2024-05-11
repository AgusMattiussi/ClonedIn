package ar.edu.itba.paw.models.enums;

//TODO: Case insensitive
public enum JobOfferAvailability {
    ACTIVE("Activa"),
    CLOSED("Cerrada"),
    CANCELLED("Cancelada");

    private final String status;

    JobOfferAvailability(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static JobOfferAvailability fromString(String status) {
        for (JobOfferAvailability jobOfferAvailability : JobOfferAvailability.values()) {
            if (jobOfferAvailability.status.equalsIgnoreCase(status)) {
                return jobOfferAvailability;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid JobOfferAvailability: '%s'", status));
    }
}
