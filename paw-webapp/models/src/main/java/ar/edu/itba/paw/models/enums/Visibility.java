package ar.edu.itba.paw.models.enums;

public enum Visibility {
    INVISIBLE(0),
    VISIBLE(1);

    private final int value;

    Visibility(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Visibility{" +
                "value=" + value +
                '}';
    }
}
