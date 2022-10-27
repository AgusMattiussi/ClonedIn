package ar.edu.itba.paw.models.enums;

public enum JobOfferStatuses {
    PENDING("pendiente"),
    CLOSED("cerrada"),
    CANCELLED("cancelada"),
    ACCEPTED("aceptada"),
    DECLINED("rechazada");

    private final String status;

    JobOfferStatuses(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
