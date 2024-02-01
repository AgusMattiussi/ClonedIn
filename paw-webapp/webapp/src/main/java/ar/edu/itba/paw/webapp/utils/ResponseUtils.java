package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public final class ResponseUtils {

    public static final String TOTAL_PAGES_HEADER = "X-Total-Pages";

    private ResponseUtils() {}

    public static Response paginatedOkResponse(UriInfo uriInfo, Response.ResponseBuilder responseBuilder, int currentPage, long maxPages) {
        if(currentPage > 1)
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", currentPage - 1).build(), "prev");
        if(currentPage < maxPages)
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", currentPage + 1).build(), "next");

        Response response = responseBuilder
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last")
                .build();

        response.getHeaders().add(TOTAL_PAGES_HEADER, maxPages);
        return response;
    }

    public static CacheControl imageCacheControl() {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoTransform(true);
        cacheControl.setMustRevalidate(true);
        return cacheControl;
    }
}
