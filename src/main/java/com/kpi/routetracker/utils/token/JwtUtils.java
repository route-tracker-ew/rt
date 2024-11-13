package com.kpi.routetracker.utils.token;

import com.kpi.routetracker.model.user.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final SecretKey secretKey;

    @Autowired
    public JwtUtils(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToke(Account account, Map<String, Object> claims) {
        return createToken(account, claims);
    }

    private String createToken(Account account, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(account.getPhoneNumber())
                .claim("authorities", new HashSet<>())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String token, Account account) {
        String username = extractUsername(token);
        return (username.equals(account.getPhoneNumber()) && !isTokenExpired(token));
    }
}