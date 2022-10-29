package ar.edu.itba.paw.models.enums;

public enum FilledBy {
    ENTERPRISE(0),
    USER(1),
    ANY(2);

    private final int filledBy;

    FilledBy(int filledBy) {
        this.filledBy = filledBy;
    }

    public int getFilledBy() {
        return filledBy;
    }

}
