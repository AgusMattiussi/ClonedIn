package ar.edu.itba.paw.models.enums;

public enum EmployeeRanges {

    NOT_SPECIFIED("No-especificado"),
    FROM_1_TO_10("1-10"),
    FROM_11_TO_50("11-50"),
    FROM_51_TO_100("51-100"),
    FROM_101_TO_200("101-200"),
    FROM_201_TO_500("201-500"),
    FROM_501_TO_1000("501-1000"),
    FROM_1001_TO_5000("1001-5000"),
    FROM_5001_TO_10000("5001-10000"),
    MORE_THAN_10000("10000+");

    private final String stringValue;

    EmployeeRanges(final String s) {
        this.stringValue = s;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static EmployeeRanges fromString(String text) {
        for (EmployeeRanges range : EmployeeRanges.values()) {
            if (range.stringValue.equals(text)) {
                return range;
            }
        }
        throw new IllegalArgumentException(String.format("No constant range matching text '%s' found", text));
    }
}
