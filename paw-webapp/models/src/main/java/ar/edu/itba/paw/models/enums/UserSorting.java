package ar.edu.itba.paw.models.enums;
public enum UserSorting {

    RECENT("recientes", " ORDER BY u.id DESC"),
    OLDEST("antiguos", " ORDER BY u.id ASC"),
    EXP_ASC("expAsc", " ORDER BY SUM(COALESCE(exp.yearTo, YEAR(CURRENT_DATE)) - COALESCE(exp.yearFrom,YEAR(CURRENT_DATE))) ASC"),
    EXP_DESC("expDesc", " ORDER BY SUM(COALESCE(exp.yearTo, YEAR(CURRENT_DATE)) - COALESCE(exp.yearFrom,YEAR(CURRENT_DATE))) DESC"),
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

