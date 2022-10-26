package ar.edu.itba.paw.models.enums;

public enum JobOfferAvailability {
    ACTIVE("Activa"),
    CLOSED("Cerrada"),
    CANCELLED("Cancelada");

    //FIXME: HACERLO ASI AHORA
    /*ACTIVE("Activa"),
    INACTIVE("Inactiva");*/

    private final String status;

    JobOfferAvailability(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
