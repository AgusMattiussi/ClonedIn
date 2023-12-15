package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.CustomUserDetails;
import ar.edu.itba.paw.models.enums.JwtType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.glassfish.jersey.internal.inject.Custom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

    @Value("${jwt.expiration}")
    private long ACCESS_EXPIRATION_TIME_MILLIS;
    @Value("${jwt.refresh-token.expiration}")
    private long REFRESH_EXPIRATION_TIME_MILLIS;
    @Value("${jwt.secret-key}")
    private String JWT_SECRET;

    private static final String ISSUER = "ClonedIn";
    private static final String TOKEN_TYPE_CLAIM = "token-type";
    private static final String IP_CLAIM = "ip";
    private static final String ROLE_CLAIM = "role";
    private static final String ID_CLAIM = "id";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtHelper.class);

    /*public String generateToken(UserDetails userDetails){
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }*/


    public String generateAccessToken(CustomUserDetails userDetails){
        return generateTokenBuilder(userDetails, ACCESS_EXPIRATION_TIME_MILLIS, JwtType.ACCESS_TOKEN)
                .compact();
    }

    public String generateRefreshToken(CustomUserDetails userDetails, String ip){
        return generateTokenBuilder(userDetails, REFRESH_EXPIRATION_TIME_MILLIS, JwtType.REFRESH_TOKEN)
                .claim(IP_CLAIM, ip)
                .compact();
    }

    private JwtBuilder generateTokenBuilder(CustomUserDetails userDetails, long expirationTimeMillis, JwtType tokenType){
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer(ISSUER)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .claim(TOKEN_TYPE_CLAIM, tokenType.toString())
                .claim(ROLE_CLAIM, userDetails.getRole().name())
                .claim(ID_CLAIM, userDetails.getId())
                .signWith(getSignInKey(), SignatureAlgorithm.HS512);
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token) &&
                extractTokenType(token) == JwtType.ACCESS_TOKEN;
    }

    public boolean isAccessTokenValid(String token){
        return !isTokenExpired(token) &&
                extractTokenType(token) == JwtType.ACCESS_TOKEN;
    }


    public boolean isRefreshTokenValid(String token, String userIp){
        String tokenIp = extractIp(token);
        if(!tokenIp.equals(userIp)) {
            LOGGER.warn("An user with IP='{}' tried to use a refresh token with IP='{}'. Token may have been stolen.", tokenIp, userIp);
            throw new SecurityException(String.format("Refresh token IP (%s) does not match the requester IP (%s). Token may have been stolen.", tokenIp, userIp));
        }
        return extractTokenType(token) == JwtType.REFRESH_TOKEN && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public JwtType extractTokenType(String token) {
        String typeAsString  = extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class));
        return JwtType.fromString(typeAsString);
    }

    public String extractIp(String token) {
        return extractClaim(token, claims -> claims.get(IP_CLAIM, String.class));
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
