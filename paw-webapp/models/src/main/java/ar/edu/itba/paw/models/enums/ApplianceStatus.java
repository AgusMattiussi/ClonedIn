package ar.edu.itba.paw.models.enums;

public enum ApplianceStatus {
    PENDING("pendiente"),
    CANCELLED("cancelada"),
    ACCEPTED("aceptada"),
    DECLINED("rechazada");

    private final String status;

    ApplianceStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
