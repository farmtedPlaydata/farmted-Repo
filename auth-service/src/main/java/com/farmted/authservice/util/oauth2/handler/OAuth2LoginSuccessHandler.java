package com.farmted.authservice.util.oauth2.handler;

import com.farmted.authservice.enums.RoleEnums;
import com.farmted.authservice.repository.PassRepository;
import com.farmted.authservice.util.jwt.JwtProdiver;
import com.farmted.authservice.util.oauth2.CustomOAuth2User;
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
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            
            // Pass의 Role이 GUEST일 경우 회원가입이 완료되지 않은 회원이므로 회원 상세정보 적기 페이지로 리다이렉트
            if (oAuth2User.getRole() == RoleEnums.GUEST)
                response.sendRedirect("상세정보 추가 페이지");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
