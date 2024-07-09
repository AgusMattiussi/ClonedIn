package ar.edu.itba.paw.models.enums;

public enum ContactSorting {
    ANY("any"),
    JOB_OFFER_POSITION("jobOfferPosition"),
    USERNAME("username"),
    STATUS("status"),
    DATE_ASC("dateAsc"),
    DATE_DESC("dateDesc");

    private final String asString;

    ContactSorting(String asString) {
        this.asString = asString;
    }

    public String getAsString() {
        return asString;
    }

    public static ContactSorting fromString(String sorting) {
        if(sorting != null && !sorting.isEmpty()) {
            String sortingLowercase = sorting.toLowerCase();
            for (ContactSorting sortBy : ContactSorting.values()) {
                if (sortBy.getAsString().toLowerCase().equals(sortingLowercase)) {
                    return sortBy;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return asString;
    }
}
