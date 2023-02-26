package com.webapp.InvoiceManagementApp.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenUtil {

  private static final int EXPIRATION_SECONDS = 5 * 60;

  private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public String generate(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuer("InvoiceManagementApp")
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(Instant.now().plusSeconds(EXPIRATION_SECONDS)))
        .signWith(key)
        .compact();
  }

  public boolean validate(String token) {
    return getUsername(token) != null && !isExpired(token);
  }

  public String getUsername(String token) {
    var claims = getClaims(token);
    return claims.getSubject();
  }

  private boolean isExpired(String token) {
    var claims = getClaims(token);
    return claims.getExpiration().before(Date.from(Instant.now()));
  }

  private Claims getClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
