package com.farmted.passservice.util.jwt;

import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.TokenState;
import com.farmted.passservice.enums.TokenType;
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

    private final JwtProvider jwtProvider;

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
                if (cookie.getName().equals(JwtProvider.AUTH_HEADER)) {
                    accessToken = jwtProvider.resolveToken(cookie);
                } else if (cookie.getName().equals(JwtProvider.REFRESH_HEADER)) {
                    refreshToken = jwtProvider.resolveToken(cookie);
                }
            }
        }

        if (accessToken != null) {
            TokenState state = jwtProvider.validateToken(accessToken);
            if (TokenState.VALID.equals(state)) {
                // 유효한 AccessToken인 경우, 사용자 인증
                setAuthentication(jwtProvider.getUserInfoFromToken(accessToken).getSubject());
            } else if (TokenState.EXPIRED.equals(state)) {
                if (jwtProvider.validateRefreshToken(accessToken)) {
                    // 만료된 AccessToken인 경우, RefreshToken을 검증하고 새로운 AccessToken 생성
                    Claims passInfo = jwtProvider.getUserInfoFromToken(accessToken);
                    String uuid = passInfo.getSubject();
                    String createdAccessToken = jwtProvider.createToken(uuid, RoleEnums.GUEST, TokenType.ACCESS);
                    
                    // 생성된 AccessToken을 응답에 추가
                    ResponseCookie cookie = ResponseCookie.from(
                                JwtProvider.AUTH_HEADER, URLEncoder.encode(createdAccessToken, StandardCharsets.UTF_8))
                            .path("/")
                            .httpOnly(true)
                            .sameSite("None")
                            .secure(true)
                            .maxAge(JwtProvider.ACCESS_TOKEN_TIME)
                            .build();
                    response.addHeader("Set-Cookie", cookie.toString());
                    setAuthentication(uuid);
                }
            }
            // accessToken이 null 인데 refreshToken이 있을 경우
        } else if (refreshToken != null) {
            if (jwtProvider.validateRefreshToken(refreshToken)) {
                setAuthentication(jwtProvider.getUserInfoFromToken(refreshToken).getSubject());
            }
        }
        filterChain.doFilter(request, response);
    }

    // JWT에서 추출한 UUID를 사용하여 SecurityContext에 사용자 인증 정보를 설정
    public void setAuthentication(String uuid) {
        // 빈 SecurityContext 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        /*
        * JwtProvider의 createAuth를 사용하여 Authentication 객체 생성
        * 생성된 Authentication 객체를 SecurityContext에 설정 후
        * SecurityContextHolder를 새로운 컨텍스트로 업데이트.
        *   -> 사용자를 인증된 사용자로 인식할 수 있음
        * */
        Authentication authentication = jwtProvider.createAuth(uuid);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
