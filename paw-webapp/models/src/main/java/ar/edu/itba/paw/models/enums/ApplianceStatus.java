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

    public static ApplianceStatus fromString(String s) {
        if(s != null && !s.isEmpty()) {
            for (ApplianceStatus status : ApplianceStatus.values()) {
                if (status.getStatus().equals(s)) {
                    return status;
                }
            }
        }
        throw new IllegalArgumentException();
    }
}
