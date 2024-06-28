package ar.edu.itba.paw.models.enums;

public enum Month {
    JANUARY("Enero", 1),
    FEBRUARY("Febrero", 2),
    MARCH("Marzo", 3),
    APRIL("Abril", 4),
    MAY("Mayo", 5),
    JUNE("Junio", 6),
    JULY("Julio", 7),
    AUGUST("Agosto", 8),
    SEPTEMBER("Septiembre", 9),
    OCTOBER("Octubre", 10),
    NOVEMBER("Noviembre", 11),
    DECEMBER("Diciembre", 12);

    private final int number;
    private final String name;
    private final String nameLowerCase; // for case-insensitive comparison

    Month(String name,int number) {
        this.number = number;
        this.name = name;
        this.nameLowerCase = name.toLowerCase();
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public static Month fromNumber(int number) {
        if(number >= 1 && number <= 12)
            return Month.values()[number-1];
        throw new IllegalArgumentException(String.format("Invalid number '%d' for month", number));
    }

    public static Month fromString(String name) {
        if(name != null && !name.isEmpty()) {
            String nameLowerCase = name.toLowerCase();
            for (Month month : Month.values()) {
                if (month.nameLowerCase.equals(nameLowerCase)) {
                    return month;
                }
            }
        }
        throw new IllegalArgumentException();
    }
}
