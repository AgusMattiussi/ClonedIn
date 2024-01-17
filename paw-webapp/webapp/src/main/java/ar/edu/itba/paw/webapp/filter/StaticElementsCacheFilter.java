package ar.edu.itba.paw.webapp.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StaticElementsCacheFilter extends OncePerRequestFilter {

    @Value("${cache.static-elements-max-age-seconds}")
    private int MAX_AGE;
    private final String CACHE_CONTROL_VALUE = cacheControlValue();


    private String cacheControlValue() {
        StringBuilder builder = new StringBuilder();
        CacheControl basicCacheControl = CacheControl
                .maxAge(MAX_AGE, TimeUnit.SECONDS)
                .cachePublic();

        builder.append(basicCacheControl.getHeaderValue())
                .append(", immutable");

        return builder.toString();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getMethod().equals("GET"))
            response.setHeader(HttpHeaders.CACHE_CONTROL, CACHE_CONTROL_VALUE);

        filterChain.doFilter(request, response);
    }
}