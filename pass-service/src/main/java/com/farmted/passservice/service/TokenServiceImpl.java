package com.farmted.passservice.service;

import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.TokenType;
import com.farmted.passservice.util.jwt.JwtProdiver;
import com.farmted.passservice.util.redis.RedisRepository;
import com.farmted.passservice.util.redis.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtProdiver jwtProdiver;
    private final RedisRepository redisRepository;

    public String createAccessToken(String uuid, RoleEnums role) {
        return jwtProdiver.createToken(uuid, role, TokenType.ACCESS);
    }

    @Override
    public String createRefreshToken(String uuid, RoleEnums role) {
        return jwtProdiver.createToken(uuid, role, TokenType.REFRESH);
    }

    @Override
    public void saveRefreshToken(String uuid, String refreshToken) {
        redisRepository.findById(uuid)
                .ifPresentOrElse(
                        refreshTokenEntity -> {
                            refreshTokenEntity.updateToken(refreshToken, JwtProdiver.REFRESH_TOKEN_TIME);
                            redisRepository.save(refreshTokenEntity);
                        },
                        () -> {
                            RefreshToken refreshToSave = RefreshToken.builder()
                                    .uuid(uuid)
                                    .refreshToken(refreshToken)
                                    .expiration(JwtProdiver.REFRESH_TOKEN_TIME)
                                    .build();
                            redisRepository.save(refreshToSave);
                        }
                );
    }

    @Override
    public void setToken(String token, TokenType tokenType, HttpServletResponse response) {
        long time = tokenType == TokenType.ACCESS ? JwtProdiver.ACCESS_TOKEN_TIME : JwtProdiver.REFRESH_TOKEN_TIME;

        ResponseCookie cookie = ResponseCookie.from(
                        JwtProdiver.AUTH_HEADER,    // 쿠키의 이름
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

    @Override
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
