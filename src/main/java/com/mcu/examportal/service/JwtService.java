package com.mcu.examportal.service;

import com.mcu.examportal.entity.UserEntity;
import com.mcu.examportal.exception.UserNotFoundException;
import com.mcu.examportal.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    public static final String SECRET="79a9ef48bc06cb4b9c9ac9867a3197fb13a98e0684b478b97cf931b6afdb6ad2";
    @Autowired
    private UserRepository repository;

    // generate token
    public String generateToken(String email) {
        Optional<UserEntity> userEntityOptional = repository.findByEmail(email);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            String roles = userEntity.getRoles();

            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", roles); // Add the roles information to the claims

            return createToken(claims, email);
        } else {
            // Handle the case when the user does not exist in the database
            throw new UserNotFoundException("User not found for email: " + email);
        }
    }

    // create a token
    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*5))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    // VERIFY SIGNATURE (secret key with base64 decode and return HMAC-SHA256)
    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // extract user name/ email
    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // extract expiration time
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserEmail(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
