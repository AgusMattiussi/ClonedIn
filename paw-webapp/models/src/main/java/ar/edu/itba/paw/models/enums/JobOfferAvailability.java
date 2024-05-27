package ar.edu.itba.paw.models.enums;

//TODO: Case insensitive
public enum JobOfferAvailability {
    ACTIVE("Activa"),
    CLOSED("Cerrada"),
    CANCELLED("Cancelada");

    private final String status;
    private final String lowercased; // Used for case-insensitive comparison

    JobOfferAvailability(String status) {
        this.status = status;
        this.lowercased = status.toLowerCase();
    }

    public String getStatus() {
        return status;
    }

    public String getLowercased() {
        return lowercased;
    }

    public static JobOfferAvailability fromString(String availability) {
        if(availability != null && !availability.isEmpty()) {
            String availabilityLowerCase = availability.toLowerCase();
            for (JobOfferAvailability availabilityEnum : JobOfferAvailability.values()) {
                if (availabilityEnum.lowercased.equals(availabilityLowerCase)) {
                    return availabilityEnum;
                }
            }
        }
        throw new IllegalArgumentException(String.format("Invalid JobOfferAvailability: '%s'", availability));
    }
}
