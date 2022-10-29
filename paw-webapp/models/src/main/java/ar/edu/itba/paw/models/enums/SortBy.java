package ar.edu.itba.paw.models.enums;

public enum SortBy {
    ANY(0),
    JOB_OFFER_POSITION(1),
    USERNAME(2),
    STATUS(3);

    private final int sortBy;

    SortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public int getSortBy() {
        return sortBy;
    }
}
