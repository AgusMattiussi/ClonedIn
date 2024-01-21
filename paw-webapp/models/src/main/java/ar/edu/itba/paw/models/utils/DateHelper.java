package ar.edu.itba.paw.models.utils;

import ar.edu.itba.paw.models.enums.Month;
import ar.edu.itba.paw.models.exceptions.InvalidDateException;

public final class DateHelper {

    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2100;


    private DateHelper() {
    }

    public static void validateDate(Month monthFrom, Integer yearFrom, Month monthTo, Integer yearTo){
        if(yearFrom == null)
            throw new InvalidDateException("'yearFrom' cannot be null");

        if(monthFrom == null)
            throw new InvalidDateException("'monthFrom' cannot be null");

        if(yearFrom < MIN_YEAR || yearFrom > MAX_YEAR)
            throw new InvalidDateException(String.format("'yearFrom' cannot be before %d or after %d", MIN_YEAR, MAX_YEAR));


        if(yearTo != null && monthTo != null){
            if(yearTo < MIN_YEAR || yearTo > MAX_YEAR)
                throw new InvalidDateException(String.format("'yearTo' cannot be before %d or after %d", MIN_YEAR, MAX_YEAR));
            if(yearTo < yearFrom)
                throw new InvalidDateException("'yearTo' cannot be before 'yearFrom'");
            if(yearTo.equals(yearFrom) && monthTo.getNumber() < monthFrom.getNumber())
                throw new InvalidDateException("The 'monthTo' cannot be before 'monthFrom' for the same year");
        }

        if(yearTo == null && monthTo != null)
            throw new InvalidDateException("'yearTo' cannot be null if 'monthTo' is not null");

        if(yearTo != null && monthTo == null)
            throw new InvalidDateException("'monthTo' cannot be null if 'yearTo' is not null");
    }
}
