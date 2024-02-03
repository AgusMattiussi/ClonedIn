package ar.edu.itba.paw.models.utils;

import ar.edu.itba.paw.models.enums.Month;
import ar.edu.itba.paw.models.exceptions.InvalidDateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateHelper.class);

    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2100;

    private DateHelper() {
    }

    public static void validateDate(Integer monthFrom, Integer yearFrom, Integer monthTo, Integer yearTo){
        Month toMonth = monthTo != null ? Month.fromNumber(monthTo) : null;
        validateDate(Month.fromNumber(monthFrom), yearFrom, toMonth, yearTo);
    }

    public static void validateDate(Month monthFrom, Integer yearFrom, Month monthTo, Integer yearTo){
        if(yearFrom == null) {
            LOGGER.error("'yearFrom' cannot be null - validateDate");
            throw new InvalidDateException("'yearFrom' cannot be null");
        }

        if(monthFrom == null) {
            LOGGER.error("'monthFrom' cannot be null - validateDate");
            throw new InvalidDateException("'monthFrom' cannot be null");
        }

        if(yearFrom < MIN_YEAR || yearFrom > MAX_YEAR) {
            LOGGER.error("'yearFrom' cannot be before {} or after {} - validateDate", MIN_YEAR, MAX_YEAR);
            throw new InvalidDateException(String.format("'yearFrom' cannot be before %d or after %d", MIN_YEAR, MAX_YEAR));
        }


        if(yearTo != null && monthTo != null){
            if(yearTo < MIN_YEAR || yearTo > MAX_YEAR) {
                LOGGER.error("'yearTo' cannot be before {} or after {} - validateDate", MIN_YEAR, MAX_YEAR);
                throw new InvalidDateException(String.format("'yearTo' cannot be before %d or after %d", MIN_YEAR, MAX_YEAR));
            }
            if(yearTo < yearFrom) {
                LOGGER.error("'yearTo' cannot be before 'yearFrom' - validateDate");
                throw new InvalidDateException("'yearTo' cannot be before 'yearFrom'");
            }
            if(yearTo.equals(yearFrom) && monthTo.getNumber() < monthFrom.getNumber()) {
                LOGGER.error("The 'monthTo' cannot be before 'monthFrom' for the same year - validateDate");
                throw new InvalidDateException("The 'monthTo' cannot be before 'monthFrom' for the same year");
            }
        }

        if(yearTo == null && monthTo != null) {
            LOGGER.error("'yearTo' cannot be null if 'monthTo' is not null - validateDate");
            throw new InvalidDateException("'yearTo' cannot be null if 'monthTo' is not null");
        }

        if(yearTo != null && monthTo == null) {
            LOGGER.error("'monthTo' cannot be null if 'yearTo' is not null - validateDate");
            throw new InvalidDateException("'monthTo' cannot be null if 'yearTo' is not null");
        }
    }
}
