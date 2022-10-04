package ar.edu.itba.paw.models.enums;

public enum JobOfferStatus {
    ACTIVE("Activa"),
    CLOSED("Cerrada"),
    CANCELLED("Cancelada");

    private final String status;

    JobOfferStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
