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

    Month(String name,int number) {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public static Month fromNumber(int number) {
        for (Month month : Month.values()) {
            if (month.number == number) {
                return month;
            }
        }
        return null;
    }

    public static Month fromString(String name) {
        for (Month month : Month.values()) {
            if (month.name.equals(name)) {
                return month;
            }
        }
        return null;
    }
}
