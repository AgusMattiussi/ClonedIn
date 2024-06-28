package ar.edu.itba.paw.models.enums;

public enum UserSorting {

    RECENT("recientes", " ORDER BY u.id DESC"),
    OLDEST("antiguos", " ORDER BY u.id ASC"),
    EXP_ASC("expasc", " ORDER BY SUM(COALESCE(exp.yearTo, YEAR(CURRENT_DATE)) - COALESCE(exp.yearFrom,YEAR(CURRENT_DATE))) ASC"),
    EXP_DESC("expdesc", " ORDER BY SUM(COALESCE(exp.yearTo, YEAR(CURRENT_DATE)) - COALESCE(exp.yearFrom,YEAR(CURRENT_DATE))) DESC"),
    EDUCATION_ASC("educasc", " ORDER BY CASE" +
                                                 " WHEN u.education = 'Primario' then 1" +
                                                 " WHEN u.education = 'Secundario' then 2" +
                                                 " WHEN u.education = 'Terciario' then 3" +
                                                 " WHEN u.education = 'Graduado' then 4" +
                                                 " WHEN u.education = 'Posgrado' then 5" +
                                                 " WHEN u.education = 'No especificado' then 6" +
                                                 " WHEN u.education = 'No-especificado' then 6" +
                                                 " END ASC "),
    EDUCATION_DESC("educdesc", " ORDER BY CASE" +
                                                 " WHEN u.education = 'Primario' then 1" +
                                                 " WHEN u.education = 'Secundario' then 2" +
                                                 " WHEN u.education = 'Terciario' then 3" +
                                                 " WHEN u.education = 'Graduado' then 4" +
                                                 " WHEN u.education = 'Posgrado' then 5" +
                                                 " WHEN u.education = 'No especificado' then 0" +
                                                 " WHEN u.education = 'No-especificado' then 0" +
                                                 " END DESC "),
    DEFAULT("predeterminado", " ORDER BY u.id DESC");

    private final String stringValue;
    private final String asQuery;

    UserSorting(String stringValue, String asQuery) {
        this.stringValue = stringValue;
        this.asQuery = asQuery;
    }

    public String getAsQuery() {
        return asQuery;
    }

    public static UserSorting fromString(String sorting) {
        if(sorting != null && !sorting.isEmpty()) {
            String sortingLowerCase = sorting.toLowerCase();
            for (UserSorting sortingEnum : UserSorting.values()) {
                if (sortingEnum.stringValue.equals(sortingLowerCase)) {
                    return sortingEnum;
                }
            }
        }
        throw new IllegalArgumentException(String.format("No constant sorting matching text '%s' found", sorting));
    }
}
