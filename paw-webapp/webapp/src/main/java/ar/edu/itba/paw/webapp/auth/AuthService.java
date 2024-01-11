package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.ws.rs.core.NewCookie;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.AUTH_URL;
import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.LOCALHOST;

@Service
public class AuthService {

    @Value("${jwt.refresh-token.expiration}")
    private long REFRESH_EXPIRATION_TIME_MILLIS;
    private static final String REFRESH_TOKEN_COOKIE_NAME = "ClonedInRefreshToken";

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private AuthenticationManager authenticationManager ;
    @Autowired
    private UserDetailsService userDetailsService;



    public String generateAccessToken(CustomUserDetails userDetails){
        return jwtHelper.generateAccessToken(userDetails);
    }

    public String generateRefreshToken(CustomUserDetails userDetails, String ip){
        return jwtHelper.generateRefreshToken(userDetails, ip);
    }

    public NewCookie generateRefreshTokenCookie(CustomUserDetails userDetails, String ip){
        String refreshToken = generateRefreshToken(userDetails, ip);
        Date expiry = jwtHelper.extractExpiration(refreshToken);

        // TODO: Actualizar path y domain
        return new NewCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, AUTH_URL, LOCALHOST, 1,
                null, (int) REFRESH_EXPIRATION_TIME_MILLIS/1000, expiry, false, true);
    }

    public String getUsernameFromBasicHeader(String basicHeader){
        String basic = basicHeader.substring(6); // "Basic ".length()

        String credentials = new String(Base64.getDecoder().decode(basic), StandardCharsets.UTF_8);
        String[] splitCredentials = credentials.split(":", 2);

        return splitCredentials[0];
    }

    public boolean isRefreshTokenValid(String token, String ip){
        return jwtHelper.isRefreshTokenValid(token, ip);
    }

    public String getUsernameFromToken(String token){
        return jwtHelper.extractUsername(token);
    }
}
