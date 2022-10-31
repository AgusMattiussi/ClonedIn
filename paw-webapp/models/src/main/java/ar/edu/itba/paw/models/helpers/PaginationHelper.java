package ar.edu.itba.paw.models.helpers;

public class PaginationHelper {
    private PaginationHelper() {
    }

    public static long getMaxPages(long totalElements, int itemsPerPage){
        return totalElements%itemsPerPage==0 ? totalElements / itemsPerPage : totalElements / itemsPerPage + 1;
    }

}
