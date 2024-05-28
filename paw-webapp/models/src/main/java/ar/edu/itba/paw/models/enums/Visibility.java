package ar.edu.itba.paw.models.enums;

public enum Visibility {
    INVISIBLE(0, "invisible"),
    VISIBLE(1, "visible");

    private final int value;
    private final String asString;

    Visibility(int value, String asString) {
        this.value = value;
        this.asString = asString;
    }

    public int getValue() {
        return value;
    }

    public String getAsString() {
        return asString;
    }

    public static Visibility fromValue(int value) {
        for (Visibility visibility : Visibility.values()) {
            if (visibility.value == value) {
                return visibility;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid visibility value: %s", value));
    }

    public static Visibility fromString(String visibility) {
        for (Visibility visibilityEnum : Visibility.values()) {
            String visibilityLowerCase = visibility.toLowerCase();
            if (visibilityEnum.asString.equals(visibilityLowerCase)) {
                return visibilityEnum;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid visibility string: %s", visibility));
    }

    public static Visibility fromBoolean(Boolean visibility) {
        if(visibility == null) {
            throw new IllegalArgumentException("Visibility cannot be null");
        }
        return visibility ? VISIBLE : INVISIBLE;
    }

    @Override
    public String toString() {
        return "Visibility{" +
                "value=" + value +
                '}';
    }
}
