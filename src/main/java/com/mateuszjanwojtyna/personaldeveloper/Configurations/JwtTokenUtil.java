package com.mateuszjanwojtyna.personaldeveloper.Configurations;

import com.mateuszjanwojtyna.personaldeveloper.Entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

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
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRole())));
        if(user.getRoles() != null) {
            System.out.println(user.getUsername() + user.getRoles().toString() + "role");
            user.getRoles().forEach(System.out::println);
        }
        return doGenerateToken(user.getUsername(),authorities);
    }

    private String doGenerateToken(String subject, List<SimpleGrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("scopes", authorities);

        //if(authorities != null && authorities.get(0) != null && authorities.get(0).getAuthority() != null)
        System.out.println(authorities);//.get(0).getAuthority());

        return Jwts.builder()
                .setClaims(claims)
                //.setPayload(authorities.get(0).getAuthority())
                .setIssuer("http://devglan.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }

}
