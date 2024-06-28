package ar.edu.itba.paw.models.enums;

public enum Role {
    USER("user"),
    ENTERPRISE("enterprise");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
