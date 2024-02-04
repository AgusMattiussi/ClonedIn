package ar.edu.itba.paw.models.enums;

public enum EducationLevel {
    NOT_SPECIFIED("No-especificado"),
    PRIMARY("Primario"),
    SECONDARY("Secundario"),
    TERTIARY("Terciario"),
    UNIVERSITY("Graduado"),
    POSTGRADUATE("Posgrado");


    private final String stringValue;

    EducationLevel(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static EducationLevel fromString(String text) {
        for (EducationLevel level : EducationLevel.values()) {
            if (level.stringValue.equals(text)) {
                return level;
            }
        }
        throw new IllegalArgumentException(String.format("No constant level matching text '%s' found", text));
    }
}
