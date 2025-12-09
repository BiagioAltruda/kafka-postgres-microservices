package com.Anagrafe.AdminService.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.Anagrafe.entities.BaseUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private String secretKey;

  public JwtService() throws NoSuchAlgorithmException {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
      SecretKey tmp = keyGenerator.generateKey();
      this.secretKey = Base64.getEncoder().encodeToString(tmp.getEncoded());
    } catch (NoSuchAlgorithmException e) {
      throw new NoSuchAlgorithmException("HmacSHA256 not found");
    }
  }

  // method to generate token based on the username, and creation date, encoded in
  // Base64
  public String generateToken(BaseUser user) {
    Map<String, Object> claims = new HashMap<>();

    return Jwts.builder()
        .claims()
        .add(claims)
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 60 * 60))
        .and()
        .signWith(getKey())
        .compact();
  }

  private SecretKey getKey() {
    byte[] key = Decoders.BASE64.decode(this.secretKey);
    return Keys.hmacShaKeyFor(key);
  }

  // methods to very and validate JWT token
  public String getUsernameFromToken(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);

  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  // finally validating and chackign expiration date
  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
