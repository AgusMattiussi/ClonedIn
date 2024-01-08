package ar.edu.itba.paw.models.enums;

public enum JobOfferModality {
    REMOTE("Remoto"),
    IN_PERSON("Presencial"),
    MIXED("Mixto"),
    NOT_SPECIFIED("No especificado");

    private final String modality;

    JobOfferModality(String modality) {
        this.modality = modality;
    }

    public String getModality() {
        return modality;
    }

    public static JobOfferModality fromString(String modality) {
        for (JobOfferModality jobOfferModality : JobOfferModality.values()) {
            if (jobOfferModality.modality.equalsIgnoreCase(modality)) {
                return jobOfferModality;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid JobOfferModality: '%s'", modality));
    }
}