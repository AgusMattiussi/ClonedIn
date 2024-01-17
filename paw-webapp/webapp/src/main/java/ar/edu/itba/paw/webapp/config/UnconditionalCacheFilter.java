package ar.edu.itba.paw.webapp.config;

import com.sun.research.ws.wadl.HTTPMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class UnconditionalCacheFilter extends OncePerRequestFilter {

    @Value("${unconditionalCache.maxAge}")
    private int MAX_AGE;
    private final String CACHE_CONTROL_VALUE = String.format("public, max-age=%d, immutable", MAX_AGE);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getMethod().equals("GET"))
            response.setHeader(HttpHeaders.CACHE_CONTROL, CACHE_CONTROL_VALUE);

        filterChain.doFilter(request, response);
    }
}