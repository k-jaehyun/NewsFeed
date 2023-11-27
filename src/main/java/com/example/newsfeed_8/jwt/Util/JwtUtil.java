package com.example.newsfeed_8.jwt.Util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private Key key;

    private final RedisTemplate<String , String > redisTemplate;

    public JwtUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getMemberInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String createToken(String userId){
        Date date = new Date();
        long TOKEN_TIME = 1000*60*60;
        Date expirationDate = new Date(date.getTime() + TOKEN_TIME);
        String token = BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userId)
                        .setExpiration(expirationDate)
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
        long remainingTime = expirationDate.getTime() - date.getTime();
        redisTemplate.opsForValue().set(token.substring(7), String.valueOf(remainingTime), TOKEN_TIME, TimeUnit.MILLISECONDS);
        System.out.println("토큰 저장");
        return token;
    }

    public boolean isTokenBlacklisted(String token){
        return redisTemplate.hasKey(token);
    }

    public void blacklistToken(String token){
        redisTemplate.delete(token);
    }

    public long getRemainingTime(String token){
        String remainingTimeStr = redisTemplate.opsForValue().get(token);
        if(remainingTimeStr != null){
            return Long.parseLong(remainingTimeStr);
        }

        return 0;
    }

}
