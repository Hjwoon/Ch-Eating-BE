package com.ch_eatimg.ch_eating.security;

import com.ch_eatimg.ch_eating.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long validityInMilliseconds;

    @Value("${application.security.jwt.refresh-expiration}")
    private long refreshTokenValidityInMilliseconds;

    @PostConstruct
    protected void init() {
        // Ensure secret key is base64 encoded
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        // Map roles to role names (String)
        claims.put("auth", roles.stream()
                .map(role -> role.getRoleName().name()) // Convert Role to role name string
                .collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String username, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        // Map roles to role names (String)
        claims.put("auth", roles.stream()
                .map(role -> role.getRoleName().name()) // Convert Role to role name string
                .collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void setSecurityContext(String token) {
        Claims claims = getClaimsFromToken(token);
        List<String> roles = claims.get("auth", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token is invalid
        }
    }
}
