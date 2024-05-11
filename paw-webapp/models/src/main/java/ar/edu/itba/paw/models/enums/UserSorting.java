package ar.edu.itba.paw.models.enums;
public enum UserSorting {

    RECENT("recientes", " ORDER BY u.id DESC"),
    //TODO: Cambiar a antiguOs
    OLDEST("antiguas", " ORDER BY u.id ASC"),
    EXP_ASC("expAsc", " ORDER BY SUM(COALESCE(exp.yearTo, YEAR(CURRENT_DATE)) - COALESCE(exp.yearFrom,YEAR(CURRENT_DATE))) ASC"),
    EXP_DESC("expDesc", " ORDER BY SUM(COALESCE(exp.yearTo, YEAR(CURRENT_DATE)) - COALESCE(exp.yearFrom,YEAR(CURRENT_DATE))) DESC"),
    EDUCATION_ASC("educAsc", " ORDER BY CASE" +
                                                 " WHEN u.education = 'Primario' then 1" +
                                                 " WHEN u.education = 'Secundario' then 2" +
                                                 " WHEN u.education = 'Terciario' then 3" +
                                                 " WHEN u.education = 'Graduado' then 4" +
                                                 " WHEN u.education = 'Posgrado' then 5" +
                                                 " WHEN u.education = 'No especificado' then 6" +
                                                 " WHEN u.education = 'No-especificado' then 6" +
                                                 " END ASC "),
    EDUCATION_DESC("educDesc", " ORDER BY CASE" +
                                                 " WHEN u.education = 'Primario' then 1" +
                                                 " WHEN u.education = 'Secundario' then 2" +
                                                 " WHEN u.education = 'Terciario' then 3" +
                                                 " WHEN u.education = 'Graduado' then 4" +
                                                 " WHEN u.education = 'Posgrado' then 5" +
                                                 " WHEN u.education = 'No especificado' then 0" +
                                                 " WHEN u.education = 'No-especificado' then 0" +
                                                 " END DESC "),
    DEFAULT("predeterminado", " ORDER BY u.id DESC")
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

