package ar.edu.itba.paw.webapp.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ar.edu.itba.paw.webapp.filter.JwtFilterUtils.AUTH_HEADER_BASIC;
import static ar.edu.itba.paw.webapp.filter.JwtFilterUtils.AUTH_HEADER_BEARER;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtFilterUtils jwtFilterUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader != null && SecurityContextHolder.getContext().getAuthentication() == null){
            if(authHeader.startsWith(AUTH_HEADER_BEARER)) {
                jwtFilterUtils.accessTokenAuthentication(authHeader, request);
            } else if(authHeader.startsWith(AUTH_HEADER_BASIC)) {
                String userEmail = jwtFilterUtils.basicAuthentication(authHeader, request);
                jwtFilterUtils.addTokens(request, response, userEmail);
            }
        }
        filterChain.doFilter(request, response);
    }

}
