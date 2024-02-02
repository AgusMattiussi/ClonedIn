package ar.edu.itba.paw.models.enums;

public enum ContactSorting {
    ANY(0, ContactSorting.ANY_VALUE),
    JOB_OFFER_POSITION(1, ContactSorting.JOB_OFFER_POSITION_VALUE),
    USERNAME(2, ContactSorting.USERNAME_VALUE),
    STATUS(3, ContactSorting.STATUS_VALUE),
    DATE_ASC(4, ContactSorting.DATE_ASC_VALUE),
    DATE_DESC(5, ContactSorting.DATE_DESC_VALUE),
    YEARS_OF_EXPERIENCE(6, ContactSorting.YEARS_OF_EXPERIENCE_VALUE);

    public static final String ANY_VALUE = "any";
    public static final String JOB_OFFER_POSITION_VALUE = "jobOfferPosition";
    public static final String USERNAME_VALUE = "username";
    public static final String STATUS_VALUE = "status";
    public static final String DATE_ASC_VALUE = "dateAsc";
    public static final String DATE_DESC_VALUE = "dateDesc";
    public static final String YEARS_OF_EXPERIENCE_VALUE = "yearsOfExp";

    private final int value;
    private final String asString;

    ContactSorting(int value, String asString) {
        this.value = value;
        this.asString = asString;
    }

    public int getValue() {
        return value;
    }

    public String getAsString() {
        return asString;
    }

    public static ContactSorting fromString(String s) {
        for (ContactSorting sortBy : ContactSorting.values()) {
            if (sortBy.getAsString().equals(s)) {
                return sortBy;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return asString;
    }
}
