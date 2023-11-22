package com.farmted.passservice.util.jwt;

import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.TokenState;
import com.farmted.passservice.enums.TokenType;
import com.farmted.passservice.config.security.UserDetailsImpl;
import com.farmted.passservice.config.security.UserDetailsServiceImpl;
import com.farmted.passservice.util.redis.RedisRepository;
import com.farmted.passservice.util.redis.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final RedisRepository redisRepository;

    public static final String AUTH_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    public static final String AUTH_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer-";

    public static final long ACCESS_TOKEN_TIME = 10 * 60 * 60 * 1000L;
    public static final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L;    // 2주


    @Value("${jwt-secret-key}")
    private String secretkey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct      // 클래스의 인스턴스 생성 후 호출
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretkey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header Token 가져오기
    public String resolveToken(Cookie cookie) throws UnsupportedEncodingException {
        // cookie에서 값을 가져온 뒤 URL 디코딩
        String bearerToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);  // 표준 상수 사용
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String uuid, RoleEnums role, TokenType tokenType) {
        long time = tokenType == TokenType.ACCESS ? ACCESS_TOKEN_TIME : REFRESH_TOKEN_TIME;

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(uuid)
                .claim(AUTH_KEY, role)
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setIssuedAt(new Date())
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public TokenState validateToken(String accessToken) {
        try {
            // setSigningKey로 토큰의 서명키 설정, parseClaimsJws(accessToken)을 호출해서 토큰을 파싱, 유효한 경우 TokenState.VALID를 반환
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return TokenState.VALID;
        } catch (ExpiredJwtException e) {
            return TokenState.EXPIRED;  // 만료된 경우 EXPIRED 반환. 검증실패
        }
    }

    
    
    // refreshToken 검증
    public Boolean validateRefreshToken(String accessToken) {
        try {
            // DB에 저장된 토큰과 비교
            Optional<RefreshToken> savedRefreshToken = redisRepository.findById(getUserInfoFromToken(accessToken).getSubject());

            return savedRefreshToken.isPresent() && accessToken.equals(savedRefreshToken.get().getRefreshToken().substring(7));

        } catch (ExpiredJwtException e) {
            log.info("이미 만료된 엑세스 토큰입니다.");
            return null;
        }
    }

    public Claims getUserInfoFromToken(String token) {
        // Jwts.parserBuilder를 통해 JWT parser 생성, 토큰의 서명키 설정, 토큰의 Cliam(토큰에 담긴 정보) 반환
        return  Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Authentication createAuth(String uuid) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(uuid);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public void setToken(String token, TokenType tokenType, HttpServletResponse response) {
        long time = (tokenType == TokenType.ACCESS)
                        ? JwtProvider.ACCESS_TOKEN_TIME
                        : JwtProvider.REFRESH_TOKEN_TIME;

        ResponseCookie cookie = ResponseCookie.from(
                        JwtProvider.AUTH_HEADER,    // 쿠키의 이름
                        URLEncoder.encode(token, StandardCharsets.UTF_8))
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(time)
                .build();
        // 응답 헤더에 쿠키 추가
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteCookie(HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                ResponseCookie responseCookie = ResponseCookie.from(cookie.getName(), null).
                        path("/").
                        httpOnly(true).
                        sameSite("None").
                        secure(true).
                        maxAge(1).
                        build();
                response.addHeader("Set-Cookie", responseCookie.toString());
            }
        }
    }
}
