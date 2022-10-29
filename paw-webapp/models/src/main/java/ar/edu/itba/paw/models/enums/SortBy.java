package ar.edu.itba.paw.models.enums;

public enum SortBy {
    ANY(0),
    JOB_OFFER_POSITION(1),
    USERNAME(2),
    STATUS(3),
    DATE_ASC(4),
    DATE_DESC(5);

    private final int sortBy;

    SortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public int getSortBy() {
        return sortBy;
    }
}
