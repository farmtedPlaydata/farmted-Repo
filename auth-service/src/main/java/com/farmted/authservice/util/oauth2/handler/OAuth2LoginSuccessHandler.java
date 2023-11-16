package com.farmted.authservice.util.oauth2.handler;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.enums.RoleEnums;
import com.farmted.authservice.enums.TokenType;
import com.farmted.authservice.repository.PassRepository;
import com.farmted.authservice.util.jwt.JwtProdiver;
import com.farmted.authservice.util.oauth2.CustomOAuth2User;
import com.farmted.authservice.util.redis.RedisRepository;
import com.farmted.authservice.util.redis.RefreshToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Token;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProdiver jwtProdiver;
    private final RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            
            // Pass의 Role이 GUEST일 경우 회원가입이 완료되지 않은 회원이므로 회원 상세정보 적기 페이지로 리다이렉트
            if (oAuth2User.getRole() == RoleEnums.GUEST) {
                response.sendRedirect("/home");
                getAccessToken(oAuth2User, response);
                getRefreshToken(oAuth2User, response);
            } else {
                response.sendRedirect("/home");
                getAccessToken(oAuth2User, response);
                getRefreshToken(oAuth2User, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // AccessToken 발급
    private void getAccessToken(CustomOAuth2User oAuth2User, HttpServletResponse response) {
        // 사용자의 uuid, role을 기반으로 accessToken 생성
        String createdAccessToken = jwtProdiver.createToken(oAuth2User.getUuid(), oAuth2User.getRole(), TokenType.ACCESS);

        // 쿠키 생성
        // JS에서 쿠키에 접근하는 것을 방지, 쿠키가 HTTP 프로토콜을 통해서만 전송되도록 설정
        ResponseCookie cookie = ResponseCookie.from(
                        JwtProdiver.AUTH_HEADER,    // 쿠키의 이름
                        URLEncoder.encode(createdAccessToken, StandardCharsets.UTF_8))
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(JwtProdiver.ACCESS_TOKEN_TIME)
                .build();
        // 응답 헤더에 쿠키 추가
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // RefreshToken 발급
    private void getRefreshToken(CustomOAuth2User oAuth2User, HttpServletResponse response) {
        String createdRefreshToken = jwtProdiver.createToken(oAuth2User.getUuid(), oAuth2User.getRole(), TokenType.REFRESH);

        ResponseCookie cookie = ResponseCookie.from(
                        JwtProdiver.AUTH_HEADER,
                        URLEncoder.encode(createdRefreshToken, StandardCharsets.UTF_8))
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(JwtProdiver.REFRESH_TOKEN_TIME)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        // Redis에서 UUID로 조회, 있다면 새로운 리프레시 토큰으로 업데이트, 없는 경우 새로운 리프레시 토큰 저장
        redisRepository.findById(oAuth2User.getUuid())
                .ifPresentOrElse(
                        refreshToken -> {
                            refreshToken.updateToken(createdRefreshToken, JwtProdiver.REFRESH_TOKEN_TIME);
                            redisRepository.save(refreshToken);
                        },
                        () -> {
                            RefreshToken refreshToSave = RefreshToken.builder()
                                    .uuid(oAuth2User.getUuid())
                                    .refreshToken(createdRefreshToken)
                                    .expiration(JwtProdiver.REFRESH_TOKEN_TIME)
                                    .build();
                            redisRepository.save(refreshToSave);
                        }
                );
    }
}
