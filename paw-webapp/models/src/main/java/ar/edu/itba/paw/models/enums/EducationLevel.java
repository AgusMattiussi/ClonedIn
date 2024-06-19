package ar.edu.itba.paw.models.enums;

public enum EducationLevel {
    NOT_SPECIFIED("No-especificado"),
    PRIMARY("Primario"),
    SECONDARY("Secundario"),
    TERTIARY("Terciario"),
    UNIVERSITY("Graduado"),
    POSTGRADUATE("Posgrado");


    private final String stringValue;
    private final String lowercased; // Used for case-insensitive comparison

    EducationLevel(String stringValue) {
        this.stringValue = stringValue;
        this.lowercased = stringValue.toLowerCase();
    }

    public String getStringValue() {
        return stringValue;
    }

    public String getLowercased() {
        return lowercased;
    }

    public static EducationLevel fromString(String level) {
        if(level != null && !level.isEmpty()) {
            String levelLowerCase = level.toLowerCase();
            for (EducationLevel levelEnum : EducationLevel.values()) {
                if (levelEnum.lowercased.equals(levelLowerCase)) {
                    return levelEnum;
                }
            }
        }
        throw new IllegalArgumentException(String.format("No constant level matching text '%s' found", level));
    }
}
