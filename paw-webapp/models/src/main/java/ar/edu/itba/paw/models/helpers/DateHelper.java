package ar.edu.itba.paw.models.helpers;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public final class DateHelper {

    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2100;
    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;

    private static final Map<String, Integer> monthToNumber = new HashMap<>();
    static {
        monthToNumber.put("No-especificado", 0);
        monthToNumber.put("Enero", 1);
        monthToNumber.put("Febrero", 2);
        monthToNumber.put("Marzo", 3);
        monthToNumber.put("Abril", 4);
        monthToNumber.put("Mayo", 5);
        monthToNumber.put("Junio", 6);
        monthToNumber.put("Julio", 7);
        monthToNumber.put("Agosto", 8);
        monthToNumber.put("Septiembre", 9);
        monthToNumber.put("Octubre", 10);
        monthToNumber.put("Noviembre", 11);
        monthToNumber.put("Diciembre", 12);
    }

    private DateHelper() {
    }

    public static Integer monthToNumber(String monthName){
        if(!monthToNumber.containsKey(monthName)) {
            throw new InvalidParameterException();
        }
        return monthToNumber.get(monthName);
    }


    public static boolean isIntervalValid(int from, int to){
        return to >= from;
    }

    public static boolean isIntervalValid(String from, String to){
        int intFrom;
        int intTo;

        try {
            intFrom = Integer.parseInt(from);
            intTo = Integer.parseInt(to);
        } catch (NumberFormatException e){
            return false;
        }

        return isIntervalValid(intFrom, intTo);
    }


    public static boolean isYearValid(int year){
        return year >= MIN_YEAR && year <= MAX_YEAR;
    }

    public static boolean isMonthValid(int month){
        return month >= MIN_MONTH && month <= MAX_MONTH;
    }

    public static boolean isDateValid(int monthFrom, int yearFrom, int monthTo, int yearTo){
        if(!isMonthValid(monthTo) || !isMonthValid(monthFrom) || !isYearValid(yearTo) || !isYearValid(yearFrom)) {
            return false;
        }
        return yearTo > yearFrom || yearTo == yearFrom && monthTo >= monthFrom;
    }
}
