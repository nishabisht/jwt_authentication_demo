package com.demo.jwt_auth.service;

import com.demo.jwt_auth.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//step 5
@Service
public class JwtService {

    //step 8
    //private String secretKey = "c838493845";// exception that it is not safe

    //step 9
    private String secretKey="";

    // step 10 check jwt token on jwt.io

    public JwtService() {
        try {
            KeyGenerator keyGenerator=KeyGenerator.getInstance("HmacSHA256");
            SecretKey key= keyGenerator.generateKey();
            secretKey= Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //step 6
    public String generateToken(Users user) {
        Map<String,Object> claims= new HashMap<>();

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getKey())
                .compact();
    }

    //step 7
    private Key getKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //step 18 implementation of extractUsername and validToken of bearer token authentication
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Step 19 creating extractClaim
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims=extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    //step 20
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }

    //step 21 implement validToken
    public boolean validToken(String token, UserDetails userDetails) {
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    //step 22
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //step 23
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
