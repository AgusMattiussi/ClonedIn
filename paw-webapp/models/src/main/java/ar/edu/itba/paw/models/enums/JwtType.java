package ar.edu.itba.paw.models.enums;

public enum JwtType {
    ACCESS_TOKEN("access-token"),
    REFRESH_TOKEN("refresh-token");

    private final String name;

    JwtType(String name) {
        this.name = name;
    }

    public static JwtType fromString(String name) {
        for (JwtType type : JwtType.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
