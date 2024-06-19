package ar.edu.itba.paw.models.enums;

public enum JobOfferModality {
    REMOTE("Remoto"),
    IN_PERSON("Presencial"),
    MIXED("Mixto");

    private final String modality;
    private final String lowercased; // Used for case-insensitive comparison

    JobOfferModality(String modality) {
        this.modality = modality;
        this.lowercased = modality.toLowerCase();
    }

    public String getModality() {
        return modality;
    }

    public String getLowercased() {
        return lowercased;
    }

    public static JobOfferModality fromString(String modality) {
        if(modality != null && !modality.isEmpty()) {
            String modalityLowerCase = modality.toLowerCase();
            for (JobOfferModality modalityEnum : JobOfferModality.values()) {
                if (modalityEnum.lowercased.equals(modalityLowerCase)) {
                    return modalityEnum;
                }
            }
        }
        throw new IllegalArgumentException(String.format("Invalid JobOfferModality: '%s'", modality));
    }
}