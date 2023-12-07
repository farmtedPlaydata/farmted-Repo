package com.farmted.gatewayservice.config;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer-";


    @Value("${jwt-secret-key}")
    private String secretkey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // header Token 가져오기
    public String resolveToken(Cookie cookie) throws UnsupportedEncodingException {
        // cookie에서 값을 가져온 뒤 URL 디코딩
        String bearerToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);  // 표준 상수 사용
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }



}
