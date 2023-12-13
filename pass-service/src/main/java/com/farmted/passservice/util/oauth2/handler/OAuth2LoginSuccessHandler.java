package com.farmted.passservice.util.oauth2.handler;

import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.TokenType;
import com.farmted.passservice.util.cookies.CookieUtil;
import com.farmted.passservice.util.jwt.JwtProvider;
import com.farmted.passservice.util.oauth2.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            String uuid = oAuth2User.getUuid();
            RoleEnums role = oAuth2User.getRole();
            String token = jwtProvider.createToken(uuid, role, TokenType.ACCESS);

            // Pass의 Role이 GUEST일 경우 회원가입이 완료되지 않은 회원이므로 회원 상세정보 적기 페이지로 리다이렉트
            if (role == RoleEnums.GUEST) {
                cookieUtil.setToken(token, TokenType.ACCESS, response);
                String refreshToken = jwtProvider.createToken(uuid, role, TokenType.REFRESH);
                jwtProvider.saveRefreshToken(uuid, refreshToken);
                response.sendRedirect("/home");
            } else {
                cookieUtil.setToken(token, TokenType.ACCESS, response);
                jwtProvider.saveRefreshToken(uuid, token);
                response.sendRedirect("/home");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
