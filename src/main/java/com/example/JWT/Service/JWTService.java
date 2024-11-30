package com.example.JWT.Service;

import com.example.JWT.Model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class JWTService {
    private String Secret_key="";
    public JWTService() throws NoSuchAlgorithmException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk= keyGenerator.generateKey();
       Secret_key= Base64.getEncoder().encodeToString(sk.getEncoded());
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException();
        }
    }
    public String generateToken(Users user) {
        System.out.println(user.getRoles());
        Map<String, Object> claims = new HashMap<>();

        // Add roles as a claim to the token (converted to a list of strings)
        claims.put("roles", user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+60*60*10))
                .and()
                .signWith(getKey())
                .compact();
    }
    private SecretKey getKey(){
        byte [] keybytes= Decoders.BASE64.decode(Secret_key);
        return Keys.hmacShaKeyFor(keybytes);
    }

    public String extractUsername(String token) {

        return extractClaims(token, Claims::getSubject);
    }


    private <T>T extractClaims(String token, Function<Claims,T> claimResolver) {
   final Claims claims= extractAllClaims(token);
   return claimResolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getKey())
                .build().parseSignedClaims(token).getPayload();

    }

    public boolean validateToken(String token, UserDetails userDetails) {
   final String userName= extractUsername(token);
   return (userName.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
   return extractClaims(token,Claims::getExpiration);
    }
}
