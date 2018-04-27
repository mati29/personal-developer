package com.mateuszjanwojtyna.personaldeveloper.Configurations;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.mateuszjanwojtyna.personaldeveloper.Models.Constants.HEADER_STRING;
import static com.mateuszjanwojtyna.personaldeveloper.Models.Constants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Optional<String> authToken = Optional.of(HEADER_STRING)
                .map(req::getHeader)
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(header -> header.replace(TOKEN_PREFIX,""));

        authToken
                .map(jwtTokenUtil::getUsernameFromToken)
                .filter( auth-> SecurityContextHolder.getContext().getAuthentication()== null)
                .map(userDetailsService::loadUserByUsername)
                .filter(userDetails -> jwtTokenUtil.validateToken(authToken.get(), userDetails))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()))
                .map(authentication -> {
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    return authentication;
                })
                .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
        chain.doFilter(req, res);
    }
}
