package ar.edu.itba.paw.models.enums;

public enum SortBy {
    ANY(0, "any"),
    JOB_OFFER_POSITION(1, "jobOfferPosition"),
    USERNAME(2, "username"),
    STATUS(3, "status"),
    DATE_ASC(4, "dateAsc"),
    DATE_DESC(5, "dateDesc"),
    YEARS_OF_EXPERIENCE(6, "yearsOfExp"),;

    private final int value;
    private final String asString;

    SortBy(int value, String asString) {
        this.value = value;
        this.asString = asString;
    }

    public int getValue() {
        return value;
    }

    public String getAsString() {
        return asString;
    }

    public static SortBy fromString(String s) {
        for (SortBy sortBy : SortBy.values()) {
            if (sortBy.getAsString().equals(s)) {
                return sortBy;
            }
        }
        throw new IllegalArgumentException();
    }
}
