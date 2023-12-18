package ar.edu.itba.paw.models.enums;

public enum JobOfferModality {
    REMOTE("Remoto"),
    IN_PERSON("Presencial"),
    MIXED("Mixto"),
    NOT_SPECIFIED("No especificado");

    private String modality;

    JobOfferModality(String modality) {
        this.modality = modality;
    }

    public String getModality() {
        return modality;
    }
}