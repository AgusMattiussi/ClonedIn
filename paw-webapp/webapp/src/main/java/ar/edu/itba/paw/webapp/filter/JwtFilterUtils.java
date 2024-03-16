package ar.edu.itba.paw.webapp.filter;

import ar.edu.itba.paw.models.ClonedInUserDetails;
import ar.edu.itba.paw.webapp.auth.AuthType;
import ar.edu.itba.paw.models.exceptions.InvalidRefreshTokenException;
import ar.edu.itba.paw.webapp.auth.AuthTypeWebAuthenticationDetails;
import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.auth.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
import java.util.Date;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.DOMAIN;
import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.REFRESH_TOKEN_COOKIE;

@Component
public class JwtFilterUtils {

    public static final String AUTH_HEADER_BEARER = "Bearer ";
    public static final String AUTH_HEADER_BASIC = "Basic ";
    public static final String ACCESS_TOKEN_HEADER = "X-Access-Token";

    @Value("${jwt.refresh-token.expiration}")
    private long REFRESH_EXPIRATION_TIME_MILLIS;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;

    public JwtFilterUtils(JwtHelper jwtHelper, AuthUserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    public void addTokens(HttpServletRequest request, HttpServletResponse response, String email){
        ClonedInUserDetails user = (ClonedInUserDetails) userDetailsService.loadUserByUsername(email);
        String accessToken = jwtHelper.generateAccessToken(user);
        NewCookie refreshTokenCookie = this.generateRefreshTokenCookie(user, request.getRemoteAddr());

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

        if (!jwtHelper.isAccessTokenValid(jwt))
            throw new InsufficientAuthenticationException("Invalid access token");

        setAuthentication(request, jwt, AuthType.JWT_ACCESS_TOKEN);
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

        if(!jwtHelper.isRefreshTokenValid(refreshToken, ip))
            throw new InvalidRefreshTokenException();

        return setAuthentication(request, refreshToken, AuthType.JWT_REFRESH_TOKEN);
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            return null;

        Optional<Cookie> refreshTokenCooie =  Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE))
                .findFirst();

        if (refreshTokenCooie.isPresent()){
            return refreshTokenCooie.get().getValue();
        }
        return null;
    }

    public NewCookie generateRefreshTokenCookie(ClonedInUserDetails userDetails, String ip){
        String refreshToken = jwtHelper.generateRefreshToken(userDetails, ip);
        Date expiry = jwtHelper.extractExpiration(refreshToken);

        return new NewCookie(REFRESH_TOKEN_COOKIE, refreshToken, "/", DOMAIN, 1,
                null, (int) REFRESH_EXPIRATION_TIME_MILLIS/1000, expiry, false, true);
    }

}
