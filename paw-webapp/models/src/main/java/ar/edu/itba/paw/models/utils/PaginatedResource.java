package ar.edu.itba.paw.models.utils;

import java.security.InvalidParameterException;
import java.util.List;

public class PaginatedResource<R> {

    private final List<R> page;
    private final int currentPage;
    private final long maxPages;

    public PaginatedResource(List<R> page, int currentPage, long maxPages) {
        if(currentPage < 1)
            throw new InvalidParameterException("The page number cannot be less than 1");
        if(currentPage > maxPages)
            throw new InvalidParameterException("The page number cannot be greater than the max number of pages");

        this.page = page;
        this.currentPage = currentPage;
        this.maxPages = maxPages;
    }

    public List<R> getPage() {
        return page;
    }

    public int getPageNumber() {
        return currentPage;
    }

    public long getMaxPages() {
        return maxPages;
    }

    public boolean hasNextPage() {
        return currentPage < maxPages;
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }
}
