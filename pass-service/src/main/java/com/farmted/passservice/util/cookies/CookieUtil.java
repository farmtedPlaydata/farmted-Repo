package com.farmted.passservice.util.cookies;

import com.farmted.passservice.enums.TokenType;
import com.farmted.passservice.util.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CookieUtil {

    public void setToken(String token, TokenType tokenType, HttpServletResponse response) {
        long time = (tokenType == TokenType.ACCESS)
                ? JwtProvider.ACCESS_TOKEN_TIME
                : JwtProvider.REFRESH_TOKEN_TIME;

        if (tokenType.equals(TokenType.ACCESS)) {
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
        } else if (tokenType.equals(TokenType.REFRESH)) {
            ResponseCookie cookie = ResponseCookie.from(
                            JwtProvider.REFRESH_HEADER,
                            URLEncoder.encode(token, StandardCharsets.UTF_8))
                    .path("/")
                    .httpOnly(true)
                    .secure(true)
                    .maxAge(JwtProvider.REFRESH_TOKEN_TIME)
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
        }
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

    public void setRoleCookie(String role, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("ROLE", role)
                .path("/")
                .sameSite("None")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
