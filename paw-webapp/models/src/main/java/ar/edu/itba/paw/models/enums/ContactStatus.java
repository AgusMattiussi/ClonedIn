package ar.edu.itba.paw.models.enums;

public enum ContactStatus {
    PENDING("pendiente"),
    CLOSED("cerrada"),
    CANCELLED("cancelada"),
    ACCEPTED("aceptada"),
    DECLINED("rechazada");

    private final String status;

    ContactStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ContactStatus fromString(String s) {
        if(s != null && !s.isEmpty()) {
            String statusLowerCase = s.toLowerCase();
            for (ContactStatus enumStatus : ContactStatus.values()) {
                if (enumStatus.getStatus().equals(statusLowerCase)) {
                    return enumStatus;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContactStatus{");
        sb.append("status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
