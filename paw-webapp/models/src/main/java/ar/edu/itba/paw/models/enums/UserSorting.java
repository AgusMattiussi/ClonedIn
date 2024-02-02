package ar.edu.itba.paw.models.enums;

public enum UserSorting {

    RECENT("recientes", " ORDER BY u.id DESC"),
    OLDEST("antiguos", " ORDER BY u.id ASC"),
    ;

    private final String stringValue;
    private final String asQuery;

    UserSorting(String stringValue, String asQuery) {
        this.stringValue = stringValue;
        this.asQuery = asQuery;
    }

    public String getStringValue() {
        return stringValue;
    }

    public String getAsQuery() {
        return asQuery;
    }

    public static UserSorting fromString(String text) {
        if(text != null && !text.isEmpty()) {
            for (UserSorting sorting : UserSorting.values()) {
                if (sorting.stringValue.equals(text)) {
                    return sorting;
                }
            }
        }
        throw new IllegalArgumentException(String.format("No constant sorting matching text '%s' found", text));
    }
}

