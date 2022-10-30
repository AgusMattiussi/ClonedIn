package ar.edu.itba.paw.models.helpers;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class DateHelper {

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

    public static Integer monthToNumber(String monthName){
        if(!monthToNumber.containsKey(monthName))
            throw new InvalidParameterException();
        return monthToNumber.get(monthName);
    }
}
