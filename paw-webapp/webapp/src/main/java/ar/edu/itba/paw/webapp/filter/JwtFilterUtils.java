package ar.edu.itba.paw.webapp.filter;

import ar.edu.itba.paw.models.CustomUserDetails;
import ar.edu.itba.paw.models.enums.AuthType;
import ar.edu.itba.paw.webapp.auth.AuthService;
import ar.edu.itba.paw.webapp.auth.AuthTypeWebAuthenticationDetails;
import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.auth.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.NewCookie;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@Component
public class JwtFilterUtils {

    public static final String AUTH_HEADER_BEARER = "Bearer ";
    public static final String AUTH_HEADER_BASIC = "Basic ";
    public static final String ACCESS_TOKEN_HEADER = "X-Access-Token";

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthService authService;


    public JwtFilterUtils(JwtHelper jwtHelper, AuthUserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    public void addTokens(HttpServletRequest request, HttpServletResponse response, String email){
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        String accessToken = authService.generateAccessToken(user);
        NewCookie refreshTokenCookie = authService.generateRefreshTokenCookie(user, request.getRemoteAddr());

        response.addHeader(ACCESS_TOKEN_HEADER, accessToken);
        // We set the cookie header manually, since we need an HTTP Only cookie and javax.servlet.http.Cookie
        // doesn't support it in the current version of the Servlet API (2.5)
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private String setAuthentication(HttpServletRequest request, String token, AuthType tokenType){
        String email = jwtHelper.extractUsername(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        authToken.setDetails(new AuthTypeWebAuthenticationDetails(request, tokenType));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return email;
    }

    // In case this method throws an InsufficientAuthenticationException, it will be handled by the ExpiredAccesTokenFilter
    public void accessTokenAuthentication(String authHeader, HttpServletRequest request) {
        String jwt = authHeader.substring(7); // "Bearer ".length()

        if (jwtHelper.isAccessTokenValid(jwt)) {
            setAuthentication(request, jwt, AuthType.JWT_ACCESS_TOKEN);
        } else {
            throw new InsufficientAuthenticationException("Invalid access token");
        }
    }

    public String basicAuthentication(String authHeader, HttpServletRequest request){
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

    public String refreshTokenAuthentication(HttpServletRequest request, String refreshToken){
        String ip = request.getRemoteAddr();
        if(jwtHelper.isRefreshTokenValid(refreshToken, ip)){
            setAuthentication(request, refreshToken, AuthType.JWT_REFRESH_TOKEN);
        }
        return setAuthentication(request, refreshToken, AuthType.JWT_REFRESH_TOKEN);
    }

    public String getRefreshTokenCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            return null;

        Optional<Cookie> refreshTokenCooie =  Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("ClonedInRefreshToken"))
                .findFirst();

        if (refreshTokenCooie.isPresent()){
            return refreshTokenCooie.get().getValue();
        }
        return null;
    }
}
