package com.mateuszjanwojtyna.personaldeveloper.Configurations;

import com.mateuszjanwojtyna.personaldeveloper.Entities.Role;
import com.mateuszjanwojtyna.personaldeveloper.Entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mateuszjanwojtyna.personaldeveloper.Models.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static com.mateuszjanwojtyna.personaldeveloper.Models.Constants.SIGNING_KEY;

@Component
public class JwtTokenUtil implements Serializable {

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        return Optional.of(token)
                .map(this::getAllClaimsFromToken)
                .map(claimsResolver)
                .orElse(null);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenNonExpired(String token) {
        return Optional.of(token)
                .map(this::getExpirationDateFromToken)
                .map(expiration -> expiration.after(new Date()))
                .orElse(null);
    }

    public String generateToken(User user) {
        return Optional.of(user)
                .map(this::getAuthorities)
                .map(authorities -> doGenerateToken(user.getUsername(),authorities))
                .orElse(null);
    }

    public List<SimpleGrantedAuthority> getAuthorities(User user) {
        return user
                .getRoles()
                .stream()
                .map(Role::getRole)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private String doGenerateToken(String subject, List<SimpleGrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("scopes", authorities);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://devglan.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        return Optional.of(token)
                .filter(this::isTokenNonExpired)
                .map(this::getUsernameFromToken)
                .filter(username -> username.equals(userDetails.getUsername()))
                .isPresent();
    }

}
