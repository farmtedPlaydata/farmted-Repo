package com.farmted.passservice.service;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;
import com.farmted.passservice.repository.PassRepository;
import com.farmted.passservice.util.jwt.JwtProvider;
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

    @Override
    public void logout(String uuid) {
        
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

}
