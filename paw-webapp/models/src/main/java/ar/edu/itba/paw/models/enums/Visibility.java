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
        throw new IllegalArgumentException("Invalid visibility value: " + value);
    }

    public static Visibility fromString(String asString) {
        for (Visibility visibility : Visibility.values()) {
            if (visibility.asString.equals(asString)) {
                return visibility;
            }
        }
        throw new IllegalArgumentException("Invalid visibility string: " + asString);
    }

    @Override
    public String toString() {
        return "Visibility{" +
                "value=" + value +
                '}';
    }
}
