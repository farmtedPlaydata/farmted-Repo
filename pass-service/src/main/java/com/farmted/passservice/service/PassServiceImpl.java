package com.farmted.passservice.service;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;
import com.farmted.passservice.repository.PassRepository;
import com.farmted.passservice.util.jwt.JwtProdiver;
import com.farmted.passservice.util.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PassServiceImpl implements PassService {

    private final PassRepository passRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProdiver jwtProdiver;
    private final RedisRepository redisRepository;
    private final TokenService tokenService;

    @Override
    public void createPass(RequestCreatePassDto dto) {
        duplicateUserCheck(dto);
        String password = dto.getPassword();
        dto.setPassword(passwordEncoder.encode(password));
        Pass pass = dto.toEntity();
        passRepository.save(pass);
    }

    @Override
    public String login(RequestLoginDto dto) {
        Optional<Pass> pass = passRepository.findByEmail(dto.getEmail());

        if (pass.isPresent()) {
            checkPass(dto);
            String accessTokenInfo = tokenService.createAccessToken(pass.get().getUuid(), pass.get().getRole());
            String refreshTokenInfo = tokenService.createRefreshToken(pass.get().getUuid(), pass.get().getRole());

            tokenService.saveRefreshToken(pass.get().getUuid(), refreshTokenInfo);

            log.info("로그인 성공");

            return accessTokenInfo;
        } else {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
    }


    // email 중복 검사
    private void duplicateUserCheck(RequestCreatePassDto dto) {
        Optional<Pass> pass = passRepository.findByEmail(dto.getEmail());

        if(pass.isPresent()) throw new RuntimeException("이미 존재하는 회원입니다.");
    }

    // 로그인 검증
    private void checkPass(RequestLoginDto dto) {
        Optional<Pass> pass = passRepository.findByEmail(dto.getEmail());
        if (pass.isPresent()) {
            passwordEncoder.matches(dto.getPassword(), pass.get().getPassword());
            log.info("비밀번호 체크 성공");
        } else {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

//    // AccessToken 발급
//    private void createAccessToken(Optional<Pass> pass, HttpServletResponse response) {
//        if (pass.isPresent()){
//            // 사용자의 uuid, role을 기반으로 accessToken 생성
//            String createdAccessToken = jwtProdiver.createToken(pass.get().getUuid(), pass.get().getRole(), TokenType.ACCESS);
//
//            // 쿠키 생성
//            // JS에서 쿠키에 접근하는 것을 방지, 쿠키가 HTTP 프로토콜을 통해서만 전송되도록 설정
//            ResponseCookie cookie = ResponseCookie.from(
//                            JwtProdiver.AUTH_HEADER,    // 쿠키의 이름
//                            URLEncoder.encode(createdAccessToken, StandardCharsets.UTF_8))
//                    .path("/")
//                    .httpOnly(true)
//                    .sameSite("None")
//                    .secure(true)
//                    .maxAge(JwtProdiver.ACCESS_TOKEN_TIME)
//                    .build();
//            // 응답 헤더에 쿠키 추가
//            response.addHeader("Set-Cookie", cookie.toString());
//        }
//    }
//
//    // RefreshToken 발급
//    private void createRefreshToken(Optional<Pass> pass, HttpServletResponse response) {
//        if (pass.isPresent()) {
//            String createdRefreshToken = jwtProdiver.createToken(pass.get().getUuid(), pass.get().getRole(), TokenType.REFRESH);
//
//            ResponseCookie cookie = ResponseCookie.from(
//                            JwtProdiver.AUTH_HEADER,
//                            URLEncoder.encode(createdRefreshToken, StandardCharsets.UTF_8))
//                    .path("/")
//                    .httpOnly(true)
//                    .secure(true)
//                    .maxAge(JwtProdiver.REFRESH_TOKEN_TIME)
//                    .build();
//            response.addHeader("Set-Cookie", cookie.toString());
//
//            // Redis에서 UUID로 조회, 있다면 새로운 리프레시 토큰으로 업데이트, 없는 경우 새로운 리프레시 토큰 저장
//            redisRepository.findById(pass.get().getUuid())
//                    .ifPresentOrElse(
//                            refreshToken -> {
//                                refreshToken.updateToken(createdRefreshToken, JwtProdiver.REFRESH_TOKEN_TIME);
//                                redisRepository.save(refreshToken);
//                            },
//                            () -> {
//                                RefreshToken refreshToSave = RefreshToken.builder()
//                                        .uuid(pass.get().getUuid())
//                                        .refreshToken(createdRefreshToken)
//                                        .expiration(JwtProdiver.REFRESH_TOKEN_TIME)
//                                        .build();
//                                redisRepository.save(refreshToSave);
//                            }
//                    );
//        }
//    }

}
