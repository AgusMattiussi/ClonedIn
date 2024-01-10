package ar.edu.itba.paw.models.enums;

public enum JobOfferStatus {
    PENDING("pendiente"),
    CLOSED("cerrada"),
    CANCELLED("cancelada"),
    ACCEPTED("aceptada"),
    DECLINED("rechazada");

    private final String status;

    JobOfferStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static JobOfferStatus fromString(String s) {
        for (JobOfferStatus status : JobOfferStatus.values()) {
            if (status.getStatus().equals(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException();
    }

}
