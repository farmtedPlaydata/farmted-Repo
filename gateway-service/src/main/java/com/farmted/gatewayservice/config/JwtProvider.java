package com.farmted.gatewayservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer-";


    @Value("${jwt-secret-key}")
    private String secretkey;
    private Key key;

    @PostConstruct      // 클래스의 인스턴스 생성 후 호출
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretkey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public boolean validateToken(String accessToken) {
        try {
            // setSigningKey로 토큰의 서명키 설정, parseClaimsJws(accessToken)을 호출해서 토큰을 파싱, 유효한 경우 true 반환
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public Claims getUserInfoFromToken(String token) {
        // Jwts.parserBuilder를 통해 JWT parser 생성, 토큰의 서명키 설정, 토큰의 Cliam(토큰에 담긴 정보) 반환
        return  Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
