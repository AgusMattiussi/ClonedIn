package ar.edu.itba.paw.webapp.filter;

import ar.edu.itba.paw.models.exceptions.InvalidRefreshTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// This filter is used to catch the exception thrown when the access token is invalid.
// It tries to validate the Refresh Token (which should be in a cookie) in order to get a new Access Token.
@Component
public class RefreshTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtFilterUtils jwtFilterUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | InsufficientAuthenticationException e) {
            if(e.getClass() != ExpiredJwtException.class && e.getClass() != InsufficientAuthenticationException.class)
                throw e;

            String refreshToken = jwtFilterUtils.getRefreshTokenFromCookie(request);
            if(refreshToken == null)
                throw new InvalidRefreshTokenException();

            String email = jwtFilterUtils.refreshTokenAuthentication(request, refreshToken);
            jwtFilterUtils.addTokens(request, response, email);

            filterChain.doFilter(request, response);
        }
    }
}
