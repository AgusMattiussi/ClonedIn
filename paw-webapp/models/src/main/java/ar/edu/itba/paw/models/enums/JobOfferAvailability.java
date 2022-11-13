package ar.edu.itba.paw.models.enums;

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
}
