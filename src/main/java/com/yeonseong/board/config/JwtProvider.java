package com.yeonseong.board.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final Key key;
  public long ACCESS_TOKEN_EXPIRE;
  public long REFRESH_TOKEN_EXPIRE;

  public JwtProvider(@Value("${jwt.key}") String key, @Value("${jwt.token.access-expire}") long accessExpire, @Value("${jwt.token.refresh-expire}") long refreshExpire) {
    byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(key.getBytes()));
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.ACCESS_TOKEN_EXPIRE = accessExpire;
    this.REFRESH_TOKEN_EXPIRE = refreshExpire;
  }

  public String createToken(Long memberId, long expireTime) {
    Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + expireTime))
        .signWith(key)
        .compact();
  }

  public Long getMemberId(String token) {
    String subject = Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody().getSubject();
    return Long.parseLong(subject);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
