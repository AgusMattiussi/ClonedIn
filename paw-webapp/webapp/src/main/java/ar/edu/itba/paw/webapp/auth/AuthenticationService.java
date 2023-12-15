package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.CustomUserDetails;
import ar.edu.itba.paw.webapp.form.AuthenticationRequest;
import ar.edu.itba.paw.webapp.form.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.ws.rs.core.NewCookie;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthenticationService {

    @Value("${jwt.refresh-token.expiration}")
    private long REFRESH_EXPIRATION_TIME_MILLIS;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private AuthenticationManager authenticationManager ;
    @Autowired
    private UserDetailsService userDetailsService;
    /*@Autowired
    private AuthUserDetailsService authUserDetailsService;*/

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthenticationException{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getEmail());
        String jwt = jwtHelper.generateAccessToken(user);

        return new AuthenticationResponse(jwt);
    }

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
        // String name, String value, String path, String domain, int version, String comment, int maxAge, Date expiry, boolean secure, boolean httpOnly
        return new NewCookie("ClonedInRefreshToken", refreshToken, "webapp_war/auth", "localhost", 1,
                null, (int) REFRESH_EXPIRATION_TIME_MILLIS/1000, expiry, false, true);
    }

    public String getUsernameFromBasicHeader(String basicHeader){
        String basic = basicHeader.substring(6); // "Basic ".length()

        String credentials = new String(Base64.getDecoder().decode(basic), StandardCharsets.UTF_8);
        String[] splitCredentials = credentials.split(":", 2);

        return splitCredentials[0];
    }
}
