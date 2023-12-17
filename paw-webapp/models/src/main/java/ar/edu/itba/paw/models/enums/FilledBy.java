package ar.edu.itba.paw.models.enums;

import java.security.InvalidParameterException;

public enum FilledBy {
    ENTERPRISE(0, "enterprise"),
    USER(1, "user"),
    ANY(2, "any");

    private final int value;
    private final String asString;


    FilledBy(int value, String asString) {
        this.value = value;
        this.asString = asString;
    }

    public int getValue() {
        return value;
    }

    public String getAsString() {
        return asString;
    }

    public static FilledBy fromString(String s) {
        for (FilledBy filledBy : FilledBy.values()) {
            if (filledBy.getAsString().equals(s)) {
                return filledBy;
            }
        }
        throw new InvalidParameterException();
    }

}
