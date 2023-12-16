package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.CustomUserDetails;
import ar.edu.itba.paw.models.enums.AuthType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_BEARER = "Bearer ";
    private static final String AUTH_HEADER_BASIC = "Basic ";
    private static final String ACCESS_TOKEN_HEADER = "X-Access-Token";

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthService authService;


    public JwtAuthenticationFilter(JwtHelper jwtHelper, AuthUserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader != null && SecurityContextHolder.getContext().getAuthentication() == null){
            if(authHeader.startsWith(AUTH_HEADER_BEARER)) {
                jwtAuthentication(authHeader, request);
            } else if(authHeader.startsWith(AUTH_HEADER_BASIC)) {
                String userEmail = basicAuthentication(authHeader, request);

                CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(userEmail);
                String accessToken = authService.generateAccessToken(user);
                NewCookie refreshTokenCookie = authService.generateRefreshTokenCookie(user, request.getRemoteAddr());

                response.addHeader(ACCESS_TOKEN_HEADER, accessToken);
                // We manually set the cookie header, since we need an HTTP Only cookie and javax.servlet.http.Cookie
                // doesn't support it in the current version of the Servlet API (2.5)
                response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
            }
        }
        filterChain.doFilter(request, response);
    }


    private void jwtAuthentication(String authHeader, HttpServletRequest request){
        String jwt = authHeader.substring(7); // "Bearer ".length()
        String email = jwtHelper.extractUsername(jwt);
        if (jwtHelper.isAccessTokenValid(jwt)) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            authToken.setDetails(new AuthTypeWebAuthenticationDetails(request, AuthType.JWT_ACCESS_TOKEN));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }


    private String basicAuthentication(String authHeader, HttpServletRequest request){
        String basic = authHeader.substring(6); // "Basic ".length()

        String credentials = new String(Base64.getDecoder().decode(basic), StandardCharsets.UTF_8);
        String[] splitCredentials = credentials.split(":", 2);

        String email = splitCredentials[0];
        String password = splitCredentials[1];

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        authToken.setDetails(new AuthTypeWebAuthenticationDetails(request, AuthType.BASIC));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return email;
    }
}
