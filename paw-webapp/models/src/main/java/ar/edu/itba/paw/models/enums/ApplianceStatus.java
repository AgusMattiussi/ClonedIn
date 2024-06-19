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

    public static ApplianceStatus fromString(String status) {
        if(status != null && !status.isEmpty()) {
            String statusLowerCase = status.toLowerCase();
            for (ApplianceStatus enumStatus : ApplianceStatus.values()) {
                if (enumStatus.getStatus().equals(statusLowerCase)) {
                    return enumStatus;
                }
            }
        }
        throw new IllegalArgumentException();
    }
}
