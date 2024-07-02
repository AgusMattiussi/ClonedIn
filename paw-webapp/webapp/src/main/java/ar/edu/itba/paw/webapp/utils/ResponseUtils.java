package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.*;

public final class ResponseUtils {

    public static final String TOTAL_PAGES_HEADER = "X-Total-Pages";
    public static final int CACHE_1_YEAR = 31536000;

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

    // Used for conditional caching
    public static Response getCachedResponse(Request request, EntityTag eTag) {
        Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(eTag);
        if (responseBuilder == null) {
            return null;
        }

        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        //response.cacheControl(cacheControl);
        return responseBuilder.build();
    }

    // Used for unconditional caching
    public static CacheControl unconditionalCache(int maxAgeSeconds) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(maxAgeSeconds);
        return cacheControl;
    }
}
