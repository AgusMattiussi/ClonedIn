package ar.edu.itba.paw.webapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_HEADER_BEARER = "Bearer ";
    private static final int JWT_POSITION = 7; // Appears after 'Bearer '

    @Autowired
    private final JwtHelper jwtHelper;

    @Autowired
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtHelper jwtHelper, AuthUserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTH_HEADER);
        final String jwt;
        final String userEmail;

        System.out.println("\n\n ACA 1 \n\n");
        if(authHeader == null || !authHeader.startsWith(AUTH_HEADER_BEARER)){
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("\n\n ACA 2 \n\n");

        jwt = authHeader.substring(JWT_POSITION);
        userEmail = jwtHelper.extractUsername(jwt);
        System.out.println("\n\n ACA 3 \n\n");
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            System.out.println("\n\n ACA 4 \n\n");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            System.out.println("\n\n ACA 5 \n\n");
            if(jwtHelper.isTokenValid(jwt, userDetails)){
                System.out.println("\n\n ACA 6 \n\n");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        System.out.println("\n\n ACA 7 \n\n");
        filterChain.doFilter(request, response);
    }
}
