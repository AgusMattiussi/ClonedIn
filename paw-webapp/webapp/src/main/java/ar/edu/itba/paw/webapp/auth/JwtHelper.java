package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.enums.JwtType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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
    private int ACCESS_EXPIRATION_TIME_MILLIS;
    @Value("${jwt.refresh-token.expiration}")
    private int REFRESH_EXPIRATION_TIME_MILLIS;
    @Value("${jwt.secret-key}")
    private String JWT_SECRET;

    private static final String TOKEN_TYPE_CLAIM = "token-type";
    private static final String IP_CLAIM = "ip";



    /*public String generateToken(UserDetails userDetails){
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }*/


    public String generateAccessToken(String email){
        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME_MILLIS))
                .claim(TOKEN_TYPE_CLAIM, JwtType.ACCESS_TOKEN.toString())
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(String email, String ip){
        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME_MILLIS))
                .claim(TOKEN_TYPE_CLAIM, JwtType.REFRESH_TOKEN.toString())
                .claim(IP_CLAIM, ip)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
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
