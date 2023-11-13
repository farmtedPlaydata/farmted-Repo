package com.farmted.authservice.service;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.dto.request.RequestCreatePassDto;
import com.farmted.authservice.dto.request.RequestLoginDto;
import com.farmted.authservice.dto.response.ResponseLoginDto;
import com.farmted.authservice.enums.TokenType;
import com.farmted.authservice.repository.PassRepository;
import com.farmted.authservice.util.jwt.JwtProdiver;
import com.farmted.authservice.util.redis.RedisRepository;
import com.farmted.authservice.util.redis.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PassServiceImpl implements PassService {

    private final PassRepository passRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProdiver jwtProdiver;
    private final RedisRepository redisRepository;

    @Override
    public void createPass(RequestCreatePassDto dto) {
        duplicateUserCheck(dto);
        String password = dto.getPassword();
        dto.setPassword(passwordEncoder.encode(password));
        Pass pass = dto.toEntity();
        passRepository.save(pass);
    }

    @Override
    public ResponseEntity<ResponseLoginDto> login(RequestLoginDto dto) {
        checkPass(dto.getEmail(), dto.getPassword());


        return null;
    }


    // email 중복 검사
    private void duplicateUserCheck(RequestCreatePassDto dto) {
        Optional.ofNullable(
                passRepository.findByEmail(dto.getEmail())
        ).ifPresent(entity -> {
                    throw new RuntimeException("이미 존재하는 회원입니다.");}
        );
    }

    // 로그인 검증
    private void checkPass(String email, String password) {
        passRepository.findByEmail(email)
                .ifPresent(pass ->{
                    String checkPass = pass.getPassword();
                    if (!passwordEncoder.matches(password, checkPass)) {
                        throw new RuntimeException("이메일, 또는 비밀번호가 일치하지 않습니다.");
                    }
                });
        throw new RuntimeException("이메일, 또는 비밀번호가 일치하지 않습니다.");
    }

    // AccessToken 발급
    private void getAccessToken(Pass pass, HttpServletResponse response) {
        // 사용자의 uuid, role을 기반으로 accessToken 생성
        String createdAccessToken = jwtProdiver.createToken(pass.getUuid(), pass.getRole(), TokenType.ACCESS);

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
    private void getRefreshToken(Pass pass, HttpServletResponse response) {
        String createdRefreshToken = jwtProdiver.createToken(pass.getUuid(), pass.getRole(), TokenType.REFRESH);

        ResponseCookie cookie = ResponseCookie.from(
                JwtProdiver.AUTH_HEADER,
                URLEncoder.encode(createdRefreshToken, StandardCharsets.UTF_8))
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(JwtProdiver.REFRESH_TOKEN_TIME)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        // Redis에서 UUID로 조회, 있다면 새로운 리프레시 토큰으로 업데이트, 없는 경우 새로운 리프레스 토큰 저장
        redisRepository.findById(pass.getUuid())
                .ifPresentOrElse(
                        refreshToken -> {
                            refreshToken.updateToken(createdRefreshToken, JwtProdiver.REFRESH_TOKEN_TIME);
                            redisRepository.save(refreshToken);
                        },
                        () -> {
                            RefreshToken refreshToSave = RefreshToken.builder()
                                    .uuid(pass.getUuid())
                                    .refreshToken(createdRefreshToken)
                                    .expiration(JwtProdiver.REFRESH_TOKEN_TIME)
                                    .build();
                            redisRepository.save(refreshToSave);
                        }
                );
    }
}
