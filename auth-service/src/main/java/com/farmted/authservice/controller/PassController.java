package com.farmted.authservice.controller;

import com.farmted.authservice.dto.request.RequestCreatePassDto;
import com.farmted.authservice.dto.request.RequestLoginDto;
import com.farmted.authservice.service.PassService;
import com.farmted.authservice.service.PassServiceImpl;
import com.farmted.authservice.util.jwt.JwtProdiver;
import com.farmted.authservice.util.oauth2.OAuthAttributes;
import com.farmted.authservice.util.oauth2.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/pass-service")
@RequiredArgsConstructor
public class PassController {

    private final PassService passService;

    @PostMapping("/passes")
    public ResponseEntity<?> createPass(@RequestBody RequestCreatePassDto dto) {
        passService.createPass(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginPass(@RequestBody RequestLoginDto dto, HttpServletResponse response) {
        String accessToken = passService.login(dto);
        setToken(accessToken, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void setToken(String accessToken, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(
                        JwtProdiver.AUTH_HEADER,    // 쿠키의 이름
                        URLEncoder.encode(accessToken, StandardCharsets.UTF_8))
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(JwtProdiver.ACCESS_TOKEN_TIME)
                .build();
        // 응답 헤더에 쿠키 추가
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
