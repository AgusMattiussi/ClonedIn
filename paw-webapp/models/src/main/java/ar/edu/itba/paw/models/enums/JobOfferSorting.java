package ar.edu.itba.paw.models.enums;

public enum JobOfferSorting {
    DEFAULT("predeterminado", " ORDER BY jo.available ASC"),
    RECENT("recientes", " ORDER BY jo.id DESC"),
    OLDEST("antiguas", " ORDER BY jo.id ASC"),
    SALARY_ASC("salarioAsc", " ORDER BY jo.salary ASC"),
    SALARY_DESC("salarioDesc", " ORDER BY jo.salary DESC"),

    ;

    private final String stringValue;
    private final String asQuery;

    JobOfferSorting(String stringValue, String asQuery) {
        this.stringValue = stringValue;
        this.asQuery = asQuery;
    }

    public String getStringValue() {
        return stringValue;
    }

    public String getAsQuery() {
        return asQuery;
    }

    public static JobOfferSorting fromString(String text) {
        if(text != null && !text.isEmpty()) {
            for (JobOfferSorting sorting : JobOfferSorting.values()) {
                if (sorting.stringValue.equals(text)) {
                    return sorting;
                }
            }
        }
        throw new IllegalArgumentException(String.format("No constant sorting matching text '%s' found", text));
    }
}
