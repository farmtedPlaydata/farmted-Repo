package com.farmted.authservice.util.jwt;

import com.farmted.authservice.enums.RoleEnums;
import com.farmted.authservice.enums.TokenState;
import com.farmted.authservice.enums.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProdiver jwtProdiver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Http 요청에서 쿠키 추출
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie == null) {
                    continue;
                }

                // AccessToken과 RefreshToken을 쿠키에서 추출
                if (cookie.getName().equals(JwtProdiver.AUTH_HEADER)) {
                    accessToken = jwtProdiver.resolveToken(cookie);
                } else if (cookie.getName().equals(JwtProdiver.REFRESH_HEADER)) {
                    refreshToken = jwtProdiver.resolveToken(cookie);
                }
            }
        }

        if (accessToken != null) {
            TokenState state = jwtProdiver.validateToken(accessToken);
            if (TokenState.VALID.equals(state)) {
                // 유효한 AccessToken인 경우, 사용자 인증
                setAuthentication(jwtProdiver.getUserInfoFromToken(accessToken).getSubject());
            } else if (TokenState.EXPIRED.equals(state)) {
                if (jwtProdiver.validateRefreshToken(refreshToken)) {
                    // 만료된 AccessToken인 경우, RefreshToken을 검증하고 새로운 AccessToken 생성
                    Claims passInfo = jwtProdiver.getUserInfoFromToken(refreshToken);
                    String uuid = passInfo.getSubject();
                    String createdAccessToken = jwtProdiver.createToken(uuid, RoleEnums.GUEST, TokenType.ACCESS);
                    
                    // 생성된 AccessToken을 응답에 추가
                    ResponseCookie cookie = ResponseCookie.from(
                                JwtProdiver.AUTH_HEADER, URLEncoder.encode(createdAccessToken, StandardCharsets.UTF_8))
                            .path("/")
                            .httpOnly(true)
                            .sameSite("None")
                            .secure(true)
                            .maxAge(JwtProdiver.ACCESS_TOKEN_TIME)
                            .build();
                    response.addHeader("Set-Cookie", cookie.toString());
                    setAuthentication(uuid);
                }
            }
        } else if (refreshToken != null) {
            // AccessToken이 없고 RefreshToken만 있는 경우, RefreshToken을 검증하고 사용자를 인증
            if (jwtProdiver.validateRefreshToken(refreshToken)) {
                setAuthentication(jwtProdiver.getUserInfoFromToken(refreshToken).getSubject());
            }
        }
        filterChain.doFilter(request, response);
    }

    // JWT에서 추출한 UUID를 사용하여 SecurityContext에 사용자 인증 정보를 설정
    public void setAuthentication(String uuid) {
        // 빈 SecurityContext 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // JwtProvider의 createAuth를 사용하여 Authentication 객체 생성
        Authentication authentication = jwtProdiver.createAuth(uuid);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
}
