package ar.edu.itba.paw.models.helpers;

import ar.edu.itba.paw.models.enums.SortBy;

public final class SortHelper {
    private SortHelper() {
    }

    public static SortBy getSortBy(int index){
        try{
            return SortBy.values()[index];
        }catch (ArrayIndexOutOfBoundsException e){
            return SortBy.ANY;
        }
    }
}
