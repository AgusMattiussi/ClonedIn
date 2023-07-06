package ar.edu.itba.paw.webapp.auth;


import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

// Source: https://github.com/only2dhir/spring-boot-jwt
@Component
public class JwtHelper {

    private static final int EXPIRATION_TIME_MILLIS = 900000; // 15 minutos
    // TODO: Cambiar a tipo Key?
    private static final String JWT_SECRET = "ce0e75a3d00eb084f1b805b336f8f64c610ea346688a86a0fb91826b053e2527a434b70763a2499a9ba3e3eca8b673cf3c02e272e12fbe17aeb9289281db98916e7c092dff8a499b8c4e62c91b9443c1f07297106e62ecba2b30aee010d424250b630de24c4018334c20741668642f824d6cdc704e23691dd5df2c6699760bc6d513c2d9219c6e229d55a4c4c853d9f0945d9c3496de671106aab31946e5ae66ac2217e7c048d7f2bf1fa9eb5b0394f82b091430cda33aff0050eb2138c748231d0320cfa50376284464e67125f8346eb534606f91e720fc3d4227c16fea8bee26c6f73dff551bf1f157f4b9489710f062b789d306a720894023eba924ced57e912367a580cfc8af08740198517d87cd713bce1334c619b87f0e15394875dee4ee9aed7b495278f868e167b0fc36175632cc9129c9b2a9b805de3ca6369fcad7f9200d0c238be6a2d61f63f54c95b0650cd4c2703d3049aad3516fe4da2f0dc6e4dc48e78baaacd3ca25374cd5382b8b70311694f36da012ab7c8778bef401e8d12afae02f39a2f04220c8703f7638aa1ca381444e6c48f9565fc67546983a7eecf1738e21c9e2310161388a235ddd354ac20898c768cb77e518ee833b1ee728fe63976ac12baf0097babd9314d94532e12f5861ecd375089fc937461083ace8b9adb826da016d0a67b6021bb149cd9824e1180362287287aadb191d84cc45f5";

    /*@Autowired

    public JwtHelper() {
        String jwtSecretString;
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jwt-secret")))) {
            jwtSecretString = FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        this.signingKey = Keys.hmacShaKeyFor(jwtSecretString.getBytes());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        return doGenerateToken(user.getEmail());
    }

    private String doGenerateToken(String subject) {
        Claims claims = Jwts.claims();

        claims.setSubject(subject);
        claims.put("email", subject);
        // TODO: Manejar rol usuario o empresa?

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
              username.equals(userDetails.getUsername())
                    && !isTokenExpired(token));
    }*/

    /* Aunque usamos 'email' como forma de autenticacion, la sintaxis default para esto es 'username' */


    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512) // Eventualmente actualizar a SHA-3
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

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
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
